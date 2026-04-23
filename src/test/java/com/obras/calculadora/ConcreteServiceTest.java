package com.obras.calculadora;

import com.obras.calculadora.dto.MateriaisDTO;
import com.obras.calculadora.model.TipoAbertura;
import com.obras.calculadora.repository.ArestaRepository;
import com.obras.calculadora.repository.VerticeRepository;
import com.obras.calculadora.service.ConcreteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConcreteServiceTest {

    @Autowired
    private ConcreteService concreteService;

    @Autowired
    private ArestaRepository arestaRepository;

    @Autowired
    private VerticeRepository verticeRepository;

    @BeforeEach
    void limparBanco() {
        arestaRepository.deleteAll();
        verticeRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve calcular corretamente o volume de concreto de uma única aresta")
    void deveCalcularVolumeUmaAresta() {
        // Parede: comprimento=5.0m, largura=0.15m, alturaViga=0.40m
        // Esperado: V = 0.15 × 0.40 × 5.0 = 0.300 m³

        var aresta = new MateriaisDTO.ArestaRequest(
                "a12", "V1", "V2",
                5.0, 0.15, 2.8,
                TipoAbertura.NENHUMA, null, null
        );

        var request = new MateriaisDTO.ConcreteRequest(List.of(aresta), 0.40);
        var response = concreteService.calcularVolumeConcreto(request);

        assertNotNull(response);
        assertEquals(1, response.detalhes().size());
        assertEquals(0.300, response.volumeTotalM3(), 0.001);
        assertEquals(0.300, response.detalhes().get(0).volumeM3(), 0.001);
    }

    @Test
    @DisplayName("Deve calcular corretamente o volume total de múltiplas arestas")
    void deveCalcularVolumeMultiplasArestas() {
        // a12: 4.0 × 0.15 × 0.40 = 0.240
        // a23: 3.0 × 0.15 × 0.40 = 0.180
        // Total esperado = 0.420 m³

        var a1 = new MateriaisDTO.ArestaRequest(
                "a12b", "V1b", "V2b",
                4.0, 0.15, 2.8,
                TipoAbertura.NENHUMA, null, null
        );
        var a2 = new MateriaisDTO.ArestaRequest(
                "a23b", "V2b", "V3b",
                3.0, 0.15, 2.8,
                TipoAbertura.NENHUMA, null, null
        );

        var request = new MateriaisDTO.ConcreteRequest(List.of(a1, a2), 0.40);
        var response = concreteService.calcularVolumeConcreto(request);

        assertNotNull(response);
        assertEquals(2, response.detalhes().size());
        assertEquals(0.420, response.volumeTotalM3(), 0.001);
    }
}
