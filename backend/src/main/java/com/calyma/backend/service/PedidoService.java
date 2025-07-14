package com.calyma.backend.service;

import com.calyma.backend.model.*;
import com.calyma.backend.repository.PedidoRepository;
import com.calyma.backend.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoService productoService;
    
    // Crear un nuevo pedido
    public Pedido crearPedido(Pedido pedido) {
        if (pedido.getLineas() == null || pedido.getLineas().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un producto");
        }
        
        // Validar stock y reducir inventario
        for (LineaPedido linea : pedido.getLineas()) {
            Producto producto = linea.getProducto();
            if (producto == null || producto.getId() == null) {
                throw new IllegalArgumentException("Cada linea debe tener un producto valido");
            }
            
            // Verificar stock disponible
            Producto productoEnBD = productoService.obtenerProductoPorId(producto.getId());
            if (productoEnBD.getStock() < linea.getCantidad()) {
                throw new StockInsuficienteException(
                    "Stock insuficiente para el producto: " + productoEnBD.getNombre() +
                    ". Disponible: " + productoEnBD.getStock() + ", Solicitado: " + linea.getCantidad());
            }
            
            // Reducir stock
            productoService.reducirStock(producto.getId(), linea.getCantidad());
            
            // Actualizar el producto en la linea con los datos de la BD
            linea.setProducto(productoEnBD);
        }
        
        return pedidoRepository.save(pedido);
    }
    
    // Obtener todos los pedidos
    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAllByOrderByFechaCreacionDesc();
    }
    
    // Obtener pedido por ID
    @Transactional(readOnly = true)
    public Pedido obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado con ID: " + id));
    }
} 