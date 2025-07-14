package com.calyma.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Table(name = "pedidos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<LineaPedido> lineas = new ArrayList<>();

    @ManyToOne
    private Usuario usuario;
    
    @Column(length = 200)
    private String observaciones;
    
    public void agregarProducto(Producto producto, Integer cantidad) {
        LineaPedido linea = new LineaPedido();
        linea.setProducto(producto);
        linea.setCantidad(cantidad);
        linea.setPedido(this); 
        this.lineas.add(linea);
    }
    
    public Pedido(String observaciones) {
        this.observaciones = observaciones;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
        this.lineas = new ArrayList<>();
    }

    public void confirmar() {
        this.estado = EstadoPedido.CONFIRMADO;
    }

    public void cancelar() {
        this.estado = EstadoPedido.CANCELADO;
    }
    
    public double getCostoTotal() {
        return lineas.stream()
            .mapToDouble(LineaPedido::getSubtotal)
            .sum();
    }
    
    // estado del pedido
    public enum EstadoPedido {
        PENDIENTE, CONFIRMADO, EN_PROCESO, ENVIADO, ENTREGADO, CANCELADO
    }
    
} 