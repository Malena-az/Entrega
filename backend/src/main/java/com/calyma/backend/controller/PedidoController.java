package com.calyma.backend.controller;

import com.calyma.backend.model.*;
import com.calyma.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import org.springframework.security.core.Authentication;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/pedidos")
@PreAuthorize("isAuthenticated()")
public class PedidoController {
    @Autowired
    private PedidoRepository pedidoRepo;
    @Autowired
    private ProductoRepository productoRepo;
    @Autowired
    private UserRepository userRepo;

    //pedidos del usuario autenticado
    @GetMapping
    public List<Map<String, Object>> getPedidosUsuarioAutenticado(Authentication auth) {
        Usuario usuario = userRepo.findByUsername(auth.getName()).orElseThrow();
        List<Pedido> pedidos = pedidoRepo.findByUsuarioId(usuario.getId());

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            Map<String, Object> pedidoMap = new HashMap<>();
            pedidoMap.put("id", pedido.getId());
            pedidoMap.put("estado", pedido.getEstado());
            pedidoMap.put("fechaCreacion", pedido.getFechaCreacion());
            pedidoMap.put("costoTotal", pedido.getCostoTotal());

            // mapear las lineas del pedido
            List<Map<String, Object>> lineas = new ArrayList<>();
            for (LineaPedido linea : pedido.getLineas()) {
                Map<String, Object> lineaMap = new HashMap<>();
                lineaMap.put("cantidad", linea.getCantidad());
                lineaMap.put("subtotal", linea.getSubtotal());

                // mapear el producto
                Map<String, Object> productoMap = new HashMap<>();
                productoMap.put("nombre", linea.getProducto().getNombre());
                productoMap.put("precio", linea.getProducto().getPrecio());
                productoMap.put("imagen", linea.getProducto().getImagen());
                lineaMap.put("producto", productoMap);

                lineas.add(lineaMap);
            }
            pedidoMap.put("lineas", lineas);

            resultado.add(pedidoMap);
        }
        return resultado;
    }

    //todos los pedidos (solo ADMIN)
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Map<String, Object>> getAllPedidos() {
        List<Pedido> pedidos = pedidoRepo.findAll();

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            Map<String, Object> pedidoMap = new HashMap<>();
            pedidoMap.put("id", pedido.getId());
            pedidoMap.put("estado", pedido.getEstado());
            pedidoMap.put("fechaCreacion", pedido.getFechaCreacion());
            pedidoMap.put("costoTotal", pedido.getCostoTotal());
            pedidoMap.put("usuario", pedido.getUsuario().getUsername());

            // mapear las lineas del pedido
            List<Map<String, Object>> lineas = new ArrayList<>();
            for (LineaPedido linea : pedido.getLineas()) {
                Map<String, Object> lineaMap = new HashMap<>();
                lineaMap.put("cantidad", linea.getCantidad());
                lineaMap.put("subtotal", linea.getSubtotal());

                // mapear el producto
                Map<String, Object> productoMap = new HashMap<>();
                productoMap.put("nombre", linea.getProducto().getNombre());
                productoMap.put("precio", linea.getProducto().getPrecio());
                productoMap.put("imagen", linea.getProducto().getImagen());
                lineaMap.put("producto", productoMap);

                lineas.add(lineaMap);
            }
            pedidoMap.put("lineas", lineas);

            resultado.add(pedidoMap);
        }
        return resultado;
    }

    // Cambiar estado de un pedido (solo ADMIN)
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> cambiarEstadoPedido(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            String nuevoEstado = payload.get("estado");
            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El estado es requerido"));
            }

            Pedido pedido = pedidoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + id));

            // Convertir String a enum
            Pedido.EstadoPedido estadoEnum;
            try {
                estadoEnum = Pedido.EstadoPedido.valueOf(nuevoEstado.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Estado invalido. Estados validos: PENDIENTE, CONFIRMADO, EN_PROCESO, ENVIADO, ENTREGADO, CANCELADO"));
            }

            pedido.setEstado(estadoEnum);
            pedidoRepo.save(pedido);

            return ResponseEntity.ok(Map.of("message", "Estado actualizado correctamente", "estado", nuevoEstado));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al actualizar estado: " + e.getMessage()));
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Map<String, Object> payload, Principal principal) {
        Usuario usuario = userRepo.findByUsername(principal.getName()).orElseThrow();
        List<?> itemsRaw = (List<?>) payload.get("items");
        if (itemsRaw == null || itemsRaw.isEmpty()) {
            //error en formato JSON
            return ResponseEntity
                .badRequest()
                .body(Collections.singletonMap("error", "El carrito esta vacio"));
        }
        List<Map<String, Object>> items = itemsRaw.stream()
            .map(item -> (Map<String, Object>) item)
            .toList();
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        for (Map<String, Object> item : items) {
            Long productoId = Long.valueOf(item.get("id").toString());
            Integer cantidad = Integer.valueOf(item.get("cantidad").toString());
            Producto producto = productoRepo.findById(productoId).orElseThrow();
            LineaPedido linea = new LineaPedido();
            linea.setProducto(producto);
            linea.setCantidad(cantidad);
            linea.setPedido(pedido);
            pedido.getLineas().add(linea);
        }
        pedidoRepo.save(pedido);

        //respuesta como en el GET
        Map<String, Object> pedidoMap = new HashMap<>();
        pedidoMap.put("id", pedido.getId());
        pedidoMap.put("estado", pedido.getEstado());
        pedidoMap.put("fechaCreacion", pedido.getFechaCreacion());
        pedidoMap.put("costoTotal", pedido.getCostoTotal());

        List<Map<String, Object>> lineas = new ArrayList<>();
        for (LineaPedido linea : pedido.getLineas()) {
            Map<String, Object> lineaMap = new HashMap<>();
            lineaMap.put("cantidad", linea.getCantidad());
            lineaMap.put("subtotal", linea.getSubtotal());

            Map<String, Object> productoMap = new HashMap<>();
            productoMap.put("nombre", linea.getProducto().getNombre());
            productoMap.put("precio", linea.getProducto().getPrecio());
            productoMap.put("imagen", linea.getProducto().getImagen());
            lineaMap.put("producto", productoMap);

            lineas.add(lineaMap);
        }
        pedidoMap.put("lineas", lineas);

        return ResponseEntity.ok(pedidoMap);
    }
} 