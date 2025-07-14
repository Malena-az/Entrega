package com.calyma.backend.model;

import jakarta.persistence.*;
import lombok.*;


@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;


}
