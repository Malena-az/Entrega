package com.calyma.backend.controller;

import com.calyma.backend.model.CartItem;
import com.calyma.backend.model.Producto;
import com.calyma.backend.model.Usuario;
import com.calyma.backend.repository.CartItemRepository;
import com.calyma.backend.repository.ProductoRepository;
import com.calyma.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/carrito")
@PreAuthorize("isAuthenticated()")
public class CartController {
    @Autowired
    private CartItemRepository cartRepo;
    @Autowired
    private ProductoRepository productoRepo;
    @Autowired
    private UserRepository userRepo;

    @GetMapping
    public List<CartItem> verCarrito(Principal principal) {
        Usuario usuario = userRepo.findByUsername(principal.getName()).orElseThrow();
        return cartRepo.findByUsuarioId(usuario.getId());
    }

    @PostMapping("/agregar")
    public ResponseEntity<?> agregarAlCarrito(@RequestParam Long productoId, @RequestParam Integer cantidad, Principal principal) {
        Usuario usuario = userRepo.findByUsername(principal.getName()).orElseThrow();
        Producto producto = productoRepo.findById(productoId).orElseThrow();
        CartItem item = new CartItem(null, producto, usuario, cantidad);
        cartRepo.save(item);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/vaciar")
    public ResponseEntity<?> vaciarCarrito(Principal principal) {
        Usuario usuario = userRepo.findByUsername(principal.getName()).orElseThrow();
        cartRepo.deleteByUsuarioId(usuario.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarDelCarrito(@RequestParam Long productoId, Principal principal) {
        Usuario usuario = userRepo.findByUsername(principal.getName()).orElseThrow();
        cartRepo.deleteByUsuarioIdAndProductoId(usuario.getId(), productoId);
        return ResponseEntity.ok().build();
    }
} 