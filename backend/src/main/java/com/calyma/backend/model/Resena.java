package com.calyma.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String texto;

    @ManyToOne(optional = false)
    private Producto producto;

    @ManyToOne(optional = false)
    private Usuario usuario;

    private LocalDateTime fecha = LocalDateTime.now();
} 