package com.calyma.backend.controller;

import com.calyma.backend.model.Categoria;
import com.calyma.backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> crearCategoria(@RequestBody Map<String, String> payload) {
        try {
            String nombre = payload.get("nombre");
            if (nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El nombre de la categoría es requerido"));
            }
            
            // Verificar si ya existe una categoria con ese nombre
            if (categoriaRepository.findByNombreCategoria(nombre).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe una categoría con ese nombre"));
            }
            
            Categoria nuevaCategoria = new Categoria();
            nuevaCategoria.setNombreCategoria(nombre);
            Categoria categoriaGuardada = categoriaRepository.save(nuevaCategoria);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaGuardada);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al crear la categoría: " + e.getMessage()));
        }
    }
} 