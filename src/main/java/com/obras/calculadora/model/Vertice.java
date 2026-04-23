package com.obras.calculadora.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um vértice do grafo G=(V,A).
 * Cada vértice é um encontro de paredes onde será posicionado um pilar estrutural.
 */
@Entity
@Table(name = "vertices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vertice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do vértice é obrigatório")
    @Column(nullable = false, unique = true)
    private String nome; // ex: V1, V2, V3...
}
