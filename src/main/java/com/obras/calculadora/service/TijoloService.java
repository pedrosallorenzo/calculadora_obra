package com.obras.calculadora.service;

import com.obras.calculadora.dto.MateriaisDTO;
import com.obras.calculadora.model.TipoAbertura;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pelo cálculo da quantidade de tijolos nas paredes.
 *
 * Fórmula:
 *   Área da parede = Comprimento × Altura
 *   Área da abertura = ComprimentoAbertura × AlturaAbertura  (se houver)
 *   Área líquida = Área da parede − Área da abertura
 *   Área do tijolo = (ComprimentoTijolo + EspessuraJunta) × (AlturaTijolo + EspessuraJunta)
 *   Quantidade = ceil(Área líquida / Área do tijolo)
 */
@Service
public class TijoloService {

    public MateriaisDTO.TijolosResponse calcularQuantidadeTijolos(MateriaisDTO.TijolosRequest request) {

        List<MateriaisDTO.DetalheTijolo> detalhes = new ArrayList<>();
        int totalTijolos = 0;

        // Área de um tijolo considerando a junta de argamassa
        double areaTijolo = (request.comprimentoTijolo() + request.espessuraJunta())
                          * (request.alturaTijolo() + request.espessuraJunta());

        for (MateriaisDTO.ArestaRequest arestaReq : request.arestas()) {

            // Área bruta da parede
            double areaParede = arestaReq.comprimento() * arestaReq.altura();

            // Área das aberturas (janela ou porta)
            double areaAberturas = 0.0;
            if (arestaReq.tipoAbertura() != null
                    && arestaReq.tipoAbertura() != TipoAbertura.NENHUMA
                    && arestaReq.alturaAbertura() != null
                    && arestaReq.comprimentoAbertura() != null) {

                areaAberturas = arestaReq.alturaAbertura() * arestaReq.comprimentoAbertura();
            }

            // Área líquida (descontando aberturas)
            double areaLiquida = areaParede - areaAberturas;
            if (areaLiquida < 0) areaLiquida = 0;

            // Quantidade de tijolos (arredonda para cima)
            int quantidade = (int) Math.ceil(areaLiquida / areaTijolo);
            totalTijolos += quantidade;

            detalhes.add(new MateriaisDTO.DetalheTijolo(
                    arestaReq.nome(),
                    Math.round(areaParede * 100.0) / 100.0,
                    Math.round(areaAberturas * 100.0) / 100.0,
                    Math.round(areaLiquida * 100.0) / 100.0,
                    Math.round(areaTijolo * 10000.0) / 10000.0,
                    quantidade
            ));
        }

        return new MateriaisDTO.TijolosResponse(totalTijolos, detalhes);
    }
}
