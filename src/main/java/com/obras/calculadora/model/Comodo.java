package com.obras.calculadora.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um cômodo da planta baixa.
 * Cada cômodo é formado por um conjunto de paredes (arestas).
 */
@Entity
@Table(name = "comodos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do cômodo é obrigatório")
    @Column(nullable = false)
    private String nome; // ex: Sala, Cozinha, Quarto...

    @Positive(message = "A largura deve ser positiva")
    @Column(nullable = false)
    private Double largura; // metros

    @Positive(message = "O comprimento deve ser positivo")
    @Column(nullable = false)
    private Double comprimento; // metros

    @Positive(message = "A altura deve ser positiva")
    @Column(nullable = false)
    private Double altura; // metros

    @OneToMany(mappedBy = "comodo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Aresta> paredes = new ArrayList<>();
}
