package com.obras.calculadora.dto;

import com.obras.calculadora.model.TipoAbertura;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * DTOs de entrada e saída para os endpoints de materiais.
 */
public class MateriaisDTO {

    // =============================================
    // DTO de uma aresta (parede) para os requests
    // =============================================
    @Schema(description = "Dados de uma parede (aresta do grafo)")
    public record ArestaRequest(

            @Schema(description = "Identificador da aresta", example = "a12")
            @NotNull(message = "O nome da aresta é obrigatório")
            String nome,

            @Schema(description = "Vértice de origem", example = "V1")
            @NotNull String verticeOrigem,

            @Schema(description = "Vértice de destino", example = "V2")
            @NotNull String verticeDestino,

            @Schema(description = "Comprimento da parede em metros", example = "4.5")
            @NotNull @Positive Double comprimento,

            @Schema(description = "Largura/espessura da parede em metros", example = "0.15")
            @NotNull @Positive Double largura,

            @Schema(description = "Altura da parede em metros", example = "2.8")
            @NotNull @Positive Double altura,

            @Schema(description = "Tipo de abertura na parede", example = "JANELA")
            TipoAbertura tipoAbertura,

            @Schema(description = "Altura da abertura (janela/porta) em metros", example = "1.2")
            Double alturaAbertura,

            @Schema(description = "Comprimento da abertura (janela/porta) em metros", example = "1.0")
            Double comprimentoAbertura
    ) {}

    // =============================================
    // REQUEST — Concreto (Etapa 2)
    // =============================================
    @Schema(description = "Requisição para calcular volume de concreto das vigas baldrame")
    public record ConcreteRequest(

            @Schema(description = "Lista de arestas (paredes) da planta")
            @NotEmpty(message = "A lista de arestas não pode ser vazia")
            @Valid List<ArestaRequest> arestas,

            @Schema(description = "Altura da viga baldrame em metros", example = "0.4")
            @NotNull @Positive Double alturaViga
    ) {}

    // =============================================
    // RESPONSE — Concreto (Etapa 2)
    // =============================================
    @Schema(description = "Resultado do cálculo de volume de concreto")
    public record ConcreteResponse(

            @Schema(description = "Volume total de concreto em m³")
            Double volumeTotalM3,

            @Schema(description = "Detalhamento por aresta")
            List<DetalheConcreto> detalhes
    ) {}

    @Schema(description = "Volume de concreto por aresta")
    public record DetalheConcreto(
            String aresta,
            Double largura,
            Double alturaViga,
            Double comprimento,
            Double volumeM3
    ) {}

    // =============================================
    // REQUEST — Tijolos (Etapa 3)
    // =============================================
    @Schema(description = "Requisição para calcular quantidade de tijolos das paredes")
    public record TijolosRequest(

            @Schema(description = "Lista de arestas (paredes) da planta")
            @NotEmpty(message = "A lista de arestas não pode ser vazia")
            @Valid List<ArestaRequest> arestas,

            @Schema(description = "Altura do tijolo em metros", example = "0.057")
            @NotNull @Positive Double alturaTijolo,

            @Schema(description = "Comprimento do tijolo em metros", example = "0.19")
            @NotNull @Positive Double comprimentoTijolo,

            @Schema(description = "Largura do tijolo em metros (para alvenaria)", example = "0.09")
            @NotNull @Positive Double larguraTijolo,

            @Schema(description = "Espessura da junta de argamassa em metros", example = "0.01")
            @NotNull @Positive Double espessuraJunta
    ) {}

    // =============================================
    // RESPONSE — Tijolos (Etapa 3)
    // =============================================
    @Schema(description = "Resultado do cálculo de quantidade de tijolos")
    public record TijolosResponse(

            @Schema(description = "Quantidade total de tijolos")
            Integer quantidadeTotalTijolos,

            @Schema(description = "Detalhamento por aresta")
            List<DetalheTijolo> detalhes
    ) {}

    @Schema(description = "Quantidade de tijolos por aresta/parede")
    public record DetalheTijolo(
            String aresta,
            Double areaParede,
            Double areaAberturas,
            Double areaLiquida,
            Double areaTijolo,
            Integer quantidadeTijolos
    ) {}
}
