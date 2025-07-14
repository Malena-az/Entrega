package com.calyma.backend.controller;

import com.calyma.backend.model.*;
import com.calyma.backend.service.ProductoService;
import com.calyma.backend.repository.CategoriaRepository;
import com.calyma.backend.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/productos")
public class ProductController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    // GET-obtener todos los productos
    @GetMapping
    public List<Producto> obtenerProductos(@RequestParam(required = false) Long categoriaId) {
        if (categoriaId != null) {
            return productoService.findByCategoriaId(categoriaId);
        } else {
            return productoService.findAll();
        }
    }
    
    // GET-obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        try {
            Producto producto = productoService.obtenerProductoPorId(id);
            return ResponseEntity.ok(producto);
        } catch (ProductoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET-buscar productos por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarProductosPorNombre(@RequestParam String nombre) {
        try {
            List<Producto> productos = productoService.buscarProductosPorNombre(nombre);
            return ResponseEntity.ok(productos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // POST-crear nuevo producto
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Map<String, Object> payload) {
        try {
            // extraer datos 
            String nombre = (String) payload.get("nombre");
            String descripcion = (String) payload.get("descripcion");
            Double precio = Double.valueOf(payload.get("precio").toString());
            Integer stock = Integer.valueOf(payload.get("stock").toString());
            Integer idCategoria = Integer.valueOf(payload.get("id_categoria").toString());
            String presentacion = (String) payload.get("presentacion");
            String tipo = (String) payload.get("tipo");
            String imagen = (String) payload.get("imagen");
            
            // buscar la categoria por ID
            Categoria categoria = categoriaRepository.findById(Long.valueOf(idCategoria))
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada con ID: " + idCategoria));
            
            // crear el producto
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setCategoria(categoria);
            producto.setPresentacion(presentacion);
            producto.setTipo(tipo);
            producto.setImagen(imagen);
            
            Producto nuevoProducto = productoService.crearProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (ProductoDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al crear producto: " + e.getMessage()));
        }
    }
    
    // PUT-Actualizar producto
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            // Extraer datos
            String nombre = (String) payload.get("nombre");
            String descripcion = (String) payload.get("descripcion");
            Double precio = Double.valueOf(payload.get("precio").toString());
            Integer stock = Integer.valueOf(payload.get("stock").toString());
            Integer idCategoria = Integer.valueOf(payload.get("id_categoria").toString());
            String presentacion = (String) payload.get("presentacion");
            String tipo = (String) payload.get("tipo");
            String imagen = (String) payload.get("imagen");
            
            // Buscar la categoria por ID
            Categoria categoria = categoriaRepository.findById(Long.valueOf(idCategoria))
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada con ID: " + idCategoria));
            
            // Crear el producto con los datos actualizados
            Producto producto = new Producto();
            producto.setId(id); 
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setCategoria(categoria);
            producto.setPresentacion(presentacion);
            producto.setTipo(tipo);
            producto.setImagen(imagen);
            
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (ProductoNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ProductoDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al actualizar producto: " + e.getMessage()));
        }
    }
    
    // DELETE-Eliminar producto
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } catch (ProductoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    
}
