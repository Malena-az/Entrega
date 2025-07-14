package com.calyma.backend.controller;

import com.calyma.backend.model.Resena;
import com.calyma.backend.model.Producto;
import com.calyma.backend.model.Usuario;
import com.calyma.backend.repository.ResenaRepository;
import com.calyma.backend.repository.ProductoRepository;
import com.calyma.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/resenas")
public class ResenaController {
    @Autowired
    private ResenaRepository resenaRepo;
    @Autowired
    private ProductoRepository productoRepo;
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/producto/{productoId}")
    public List<Resena> listarPorProducto(@PathVariable Long productoId) {
        return resenaRepo.findByProductoId(productoId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> agregarResena(@RequestBody Resena resena, Principal principal) {
        Usuario usuario = userRepo.findByUsername(principal.getName()).orElseThrow();
        Producto producto = productoRepo.findById(resena.getProducto().getId()).orElseThrow();
        resena.setUsuario(usuario);
        resena.setProducto(producto);
        resenaRepo.save(resena);
        return ResponseEntity.ok(resena);
    }
} 