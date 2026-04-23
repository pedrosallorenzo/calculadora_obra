package com.obras.calculadora.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa uma aresta do grafo G=(V,A).
 * Cada aresta é uma parede entre dois vértices (pilares).
 * A parede possui espessura, comprimento, altura e pode ter janela ou porta.
 */
@Entity
@Table(name = "arestas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aresta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome/identificador da aresta é obrigatório")
    @Column(nullable = false, unique = true)
    private String nome; // ex: a12, a23, a34...

    // Vértice de origem
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vertice_origem_id", nullable = false)
    private Vertice verticeOrigem;

    // Vértice de destino
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vertice_destino_id", nullable = false)
    private Vertice verticeDestino;

    // Dimensões da parede (em metros)
    @Positive(message = "O comprimento deve ser positivo")
    @Column(nullable = false)
    private Double comprimento; // C — comprimento da parede

    @Positive(message = "A largura deve ser positiva")
    @Column(nullable = false)
    private Double largura; // L — espessura da parede

    @Positive(message = "A altura deve ser positiva")
    @Column(nullable = false)
    private Double altura; // A — altura da parede

    // Abertura (janela ou porta)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAbertura tipoAbertura = TipoAbertura.NENHUMA;

    // Dimensões da abertura (se houver)
    private Double alturaAbertura;    // altura da janela ou porta
    private Double comprimentoAbertura; // largura da janela ou porta

    // Relacionamento com cômodo (opcional)
    @ManyToOne
    @JoinColumn(name = "comodo_id")
    private Comodo comodo;
}
