package com.calyma.backend.service;

import com.calyma.backend.model.Producto;
import com.calyma.backend.repository.ProductoRepository;
import com.calyma.backend.exception.ProductoNotFoundException;
import com.calyma.backend.exception.ProductoDuplicadoException;
import com.calyma.backend.exception.StockInsuficienteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.calyma.backend.model.Categoria;
import com.calyma.backend.repository.CategoriaRepository;

@Service
@Transactional
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    // Crear un nuevo producto
    public Producto crearProducto(Producto producto) {
        // Buscar la categoria existente por ID
        Long idCategoria = producto.getCategoria().getId();
        Categoria categoria = categoriaRepository.findById(idCategoria)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        // Asociar la categoria 
        producto.setCategoria(categoria);

        // Validar que no exista un producto con el mismo nombre
        if (productoRepository.existsByNombre(producto.getNombre())) {
            throw new ProductoDuplicadoException("Ya existe un producto con el nombre: " + producto.getNombre());
        }
        
        // Validar que el precio sea positivo
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        
        // Validar que el stock no sea negativo
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        
        return productoRepository.save(producto);
    }
    
    // Obtener producto por ID
    @Transactional(readOnly = true)
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
    }
    
    // Buscar productos por nombre
    @Transactional(readOnly = true)
    public List<Producto> buscarProductosPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }
        return productoRepository.findByNombreContainingIgnoreCase(nombre.trim());
    }
    
    
    // Actualizar producto
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto productoExistente = obtenerProductoPorId(id);
        
        // Validar que el nuevo nombre no exista en otro producto
        if (!productoExistente.getNombre().equals(productoActualizado.getNombre()) &&
            productoRepository.existsByNombre(productoActualizado.getNombre())) {
            throw new ProductoDuplicadoException("Ya existe un producto con el nombre: " + productoActualizado.getNombre());
        }
        
        // Actualizar campos
        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setStock(productoActualizado.getStock());
        productoExistente.setDescripcion(productoActualizado.getDescripcion());
        
        return productoRepository.save(productoExistente);
    }
    
    // Eliminar producto
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }
    
    // Reducir stock de un producto
    public void reducirStock(Long productoId, Integer cantidad) {
        Producto producto = obtenerProductoPorId(productoId);
        
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException(
                "Stock insuficiente. Disponible: " + producto.getStock() + ", Solicitado: " + cantidad);
        }
        
        producto.reducirStock(cantidad);
        productoRepository.save(producto);
    }
    
    // Aumentar stock de un producto
    public void aumentarStock(Long productoId, Integer cantidad) {
        Producto producto = obtenerProductoPorId(productoId);
        producto.aumentarStock(cantidad);
        productoRepository.save(producto);
    }
    
    
    public List<Producto> findByCategoriaId(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }
} 