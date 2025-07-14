package com.calyma.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Table(name = "lineas_pedido")
public class LineaPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer cantidad;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference
    private Pedido pedido;
    
    public double getSubtotal() {
        return this.getCantidad() * this.getProducto().getPrecio();
    }
} 