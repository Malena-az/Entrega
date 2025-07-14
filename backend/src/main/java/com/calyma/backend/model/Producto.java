package com.calyma.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    @Column(nullable = false)
    private Double precio;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;
    
    @Column(length = 500)
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    
    private String imagen;
    
    @Column(length = 100)
    private String presentacion;
    
    @Column(length = 100)
    private String tipo;
    
    //para modificar el stock
    public void reducirStock(Integer cantidad) {
        this.stock -= cantidad;
    }

    public void aumentarStock(Integer cantidad) {
        this.stock += cantidad;
    }
} 