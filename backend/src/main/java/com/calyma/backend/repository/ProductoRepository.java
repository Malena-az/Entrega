package com.calyma.backend.repository;

import com.calyma.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Buscar por nombre (ignorando mayúsculas/minúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar productos con stock disponible
    List<Producto> findByStockGreaterThan(Integer stock);
    
    // Buscar productos por nombre usando JPQL
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Producto> buscarPorNombre(@Param("nombre") String nombre);
    
    // Verificar si existe un producto por nombre
    boolean existsByNombre(String nombre);
    
    // Buscar productos ordenados por precio ascendente
    List<Producto> findAllByOrderByPrecioAsc();
    
    // Buscar productos ordenados por precio descendente
    List<Producto> findAllByOrderByPrecioDesc();
    
    // Buscar productos ordenados por nombre
    List<Producto> findAllByOrderByNombreAsc();

    List<Producto> findByCategoriaId(Long categoriaId);
} 