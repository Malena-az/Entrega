package com.calyma.backend.repository;

import com.calyma.backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombreCategoria(String nombreCategoria);
} 