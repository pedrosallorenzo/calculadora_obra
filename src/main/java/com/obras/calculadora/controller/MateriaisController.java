package com.obras.calculadora.controller;

import com.obras.calculadora.dto.MateriaisDTO;
import com.obras.calculadora.service.ConcreteService;
import com.obras.calculadora.service.TijoloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para cálculo de materiais de obra residencial.
 *
 * Endpoints:
 *   POST /api/materiais/concreto  — volume de concreto nas vigas baldrame
 *   POST /api/materiais/tijolos   — quantidade de tijolos nas paredes
 */
@RestController
@RequestMapping("/api/materiais")
@Tag(name = "Materiais", description = "Cálculo de materiais para obra residencial")
public class MateriaisController {

    private final ConcreteService concreteService;
    private final TijoloService tijoloService;

    public MateriaisController(ConcreteService concreteService, TijoloService tijoloService) {
        this.concreteService = concreteService;
        this.tijoloService = tijoloService;
    }

    // ----------------------------------------------------------------
    // Etapa 2 — Volume de Concreto
    // ----------------------------------------------------------------
    @PostMapping("/concreto")
    @Operation(
        summary = "Calcular volume de concreto",
        description = "Recebe uma lista de arestas (paredes) e a altura da viga baldrame. "
                    + "Retorna o volume total de concreto e o detalhamento por parede. "
                    + "Fórmula: V = Largura × AlturaViga × Comprimento"
    )
    public ResponseEntity<MateriaisDTO.ConcreteResponse> calcularConcreto(
            @Valid @RequestBody MateriaisDTO.ConcreteRequest request) {

        MateriaisDTO.ConcreteResponse response = concreteService.calcularVolumeConcreto(request);
        return ResponseEntity.ok(response);
    }

    // ----------------------------------------------------------------
    // Etapa 3 — Quantidade de Tijolos
    // ----------------------------------------------------------------
    @PostMapping("/tijolos")
    @Operation(
        summary = "Calcular quantidade de tijolos",
        description = "Recebe uma lista de arestas (paredes) e as dimensões do tijolo. "
                    + "Retorna a quantidade total de tijolos e o detalhamento por parede. "
                    + "Descontando aberturas (janelas e portas)."
    )
    public ResponseEntity<MateriaisDTO.TijolosResponse> calcularTijolos(
            @Valid @RequestBody MateriaisDTO.TijolosRequest request) {

        MateriaisDTO.TijolosResponse response = tijoloService.calcularQuantidadeTijolos(request);
        return ResponseEntity.ok(response);
    }
}
