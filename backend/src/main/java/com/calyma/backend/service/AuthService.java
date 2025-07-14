package com.calyma.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.calyma.backend.model.Usuario;
import com.calyma.backend.model.Role;
import com.calyma.backend.repository.UserRepository;
import com.calyma.backend.repository.RoleRepository;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registerUser(String username, String rawPassword) {
        Role userRole = roleRepo.findByName("USER")
                .orElseGet(() -> roleRepo.save(new Role(null, "USER")));

        Usuario user = new Usuario();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(List.of(userRole));

        return userRepo.save(user);
    }

    public Optional<Usuario> authenticate(String username, String password) {
        Optional<Usuario> userOpt = userRepo.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return userOpt;
        }
        return Optional.empty();
    }
    
    public Role getOrCreateRole(String roleName) {
        return roleRepo.findByName(roleName)
                .orElseGet(() -> roleRepo.save(new Role(null, roleName)));
    }
    
    public Usuario createUserIfNotExists(String username, String password, List<Role> roles) {
        Optional<Usuario> existingUser = userRepo.findByUsername(username);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        
        Usuario user = new Usuario();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        
        return userRepo.save(user);
    }
}
