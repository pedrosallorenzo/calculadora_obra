package com.obras.calculadora;

import com.obras.calculadora.dto.MateriaisDTO;
import com.obras.calculadora.model.TipoAbertura;
import com.obras.calculadora.service.TijoloService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TijoloServiceTest {

    @Autowired
    private TijoloService tijoloService;

    // Tijolo padrão: 19×9×5.7cm, junta 1cm
    private static final double COMP_TIJOLO   = 0.19;
    private static final double ALTURA_TIJOLO = 0.057;
    private static final double LARG_TIJOLO   = 0.09;
    private static final double JUNTA         = 0.01;

    @Test
    @DisplayName("Deve calcular tijolos para uma parede sem abertura")
    void deveCalcularTijolosSemAbertura() {
        // Parede: 4.0m × 2.8m = 11.2 m²
        // Área tijolo: (0.19+0.01) × (0.057+0.01) = 0.20 × 0.067 = 0.0134 m²
        // Qtd = ceil(11.2 / 0.0134) = ceil(835.82) = 836

        var aresta = new MateriaisDTO.ArestaRequest(
                "a_test1", "V1", "V2",
                4.0, 0.15, 2.8,
                TipoAbertura.NENHUMA, null, null
        );

        var request = new MateriaisDTO.TijolosRequest(
                List.of(aresta),
                ALTURA_TIJOLO, COMP_TIJOLO, LARG_TIJOLO, JUNTA
        );

        var response = tijoloService.calcularQuantidadeTijolos(request);

        assertNotNull(response);
        assertEquals(1, response.detalhes().size());

        var detalhe = response.detalhes().get(0);
        assertEquals(11.2, detalhe.areaParede(), 0.01);
        assertEquals(0.0, detalhe.areaAberturas(), 0.001);
        assertTrue(response.quantidadeTotalTijolos() > 0);
    }

    @Test
    @DisplayName("Deve descontar área de janela no cálculo de tijolos")
    void deveDescontarJanelaNoCálculo() {
        // Parede: 4.0 × 2.8 = 11.2 m²
        // Janela: 1.2 × 1.0 = 1.2 m²
        // Área líquida: 10.0 m²

        var aresta = new MateriaisDTO.ArestaRequest(
                "a_test2", "V3", "V4",
                4.0, 0.15, 2.8,
                TipoAbertura.JANELA, 1.2, 1.0
        );

        var request = new MateriaisDTO.TijolosRequest(
                List.of(aresta),
                ALTURA_TIJOLO, COMP_TIJOLO, LARG_TIJOLO, JUNTA
        );

        var response = tijoloService.calcularQuantidadeTijolos(request);

        assertNotNull(response);
        var detalhe = response.detalhes().get(0);
        assertEquals(1.2, detalhe.areaAberturas(), 0.01);
        assertEquals(10.0, detalhe.areaLiquida(), 0.01);
        assertTrue(response.quantidadeTotalTijolos() > 0);
    }

    @Test
    @DisplayName("Deve descontar área de porta no cálculo de tijolos")
    void deveDescontarPortaNoCálculo() {
        // Parede: 3.0 × 2.8 = 8.4 m²
        // Porta: 2.1 × 0.9 = 1.89 m²
        // Área líquida: 6.51 m²

        var aresta = new MateriaisDTO.ArestaRequest(
                "a_test3", "V5", "V6",
                3.0, 0.15, 2.8,
                TipoAbertura.PORTA, 2.1, 0.9
        );

        var request = new MateriaisDTO.TijolosRequest(
                List.of(aresta),
                ALTURA_TIJOLO, COMP_TIJOLO, LARG_TIJOLO, JUNTA
        );

        var response = tijoloService.calcularQuantidadeTijolos(request);

        assertNotNull(response);
        var detalhe = response.detalhes().get(0);
        assertEquals(1.89, detalhe.areaAberturas(), 0.01);
        assertEquals(6.51, detalhe.areaLiquida(), 0.01);
    }
}
