package com.calyma.backend.controller;

import com.calyma.backend.model.Usuario;
import com.calyma.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import com.calyma.backend.model.Role;
import java.util.List;
import jakarta.servlet.http.Cookie;
import com.calyma.backend.service.JwtService;
import com.calyma.backend.repository.UserRepository;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El nombre de usuario es requerido"));
            }
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "La contraseña es requerida"));
            }
            
            Usuario newUser = authService.registerUser(usuario.getUsername(), usuario.getPassword());
            return ResponseEntity.ok(Map.of("message", "Usuario creado exitosamente", "username", newUser.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "El usuario ya existe"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario user) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // para obtener usuario desde la base de datos con los roles
        Usuario usuarioDB = userRepo.findByUsernameWithRoles(user.getUsername()).orElseThrow();

        String jwt = jwtService.generateToken(Map.of(), user.getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("rol", usuarioDB.getRoles().get(0).getName());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status")
    public ResponseEntity<?> getAuthStatus(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", true);
            response.put("username", auth.getName());
            response.put("isAdmin", auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN")));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();

            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            cookie.setSecure(false); // O true si uso HTTPS
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of("message", "Sesión cerrada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("message", "Sesión cerrada"));
        }
    }
    
    @PostMapping("/setup")
    public ResponseEntity<?> setupInitialData() {
        try {
            // Crear roles si no existen
            Role userRole = authService.getOrCreateRole("USER");
            Role adminRole = authService.getOrCreateRole("ADMIN");
            
            // Crear usuario admin
            Usuario admin = authService.createUserIfNotExists("admin", "admin123", List.of(adminRole));
            
            // Crear usuario normal
            Usuario user = authService.createUserIfNotExists("user", "user123", List.of(userRole));
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Datos iniciales configurados");
            response.put("admin", Map.of("username", admin.getUsername(), "password", "admin123"));
            response.put("user", Map.of("username", user.getUsername(), "password", "user123"));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al configurar datos iniciales: " + e.getMessage()));
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of("message", "Auth controller funcionando correctamente"));
    }
}
