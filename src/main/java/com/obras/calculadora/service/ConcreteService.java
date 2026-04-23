package com.obras.calculadora.service;

import com.obras.calculadora.dto.MateriaisDTO;
import com.obras.calculadora.model.Aresta;
import com.obras.calculadora.model.TipoAbertura;
import com.obras.calculadora.model.Vertice;
import com.obras.calculadora.repository.ArestaRepository;
import com.obras.calculadora.repository.VerticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pelo cálculo do volume de concreto nas vigas baldrame.
 *
 * Fórmula: Volume = Largura × AlturaViga × Comprimento  (por aresta/parede)
 */
@Service
public class ConcreteService {

    private final ArestaRepository arestaRepository;
    private final VerticeRepository verticeRepository;

    public ConcreteService(ArestaRepository arestaRepository,
                           VerticeRepository verticeRepository) {
        this.arestaRepository = arestaRepository;
        this.verticeRepository = verticeRepository;
    }

    @Transactional
    public MateriaisDTO.ConcreteResponse calcularVolumeConcreto(MateriaisDTO.ConcreteRequest request) {

        List<MateriaisDTO.DetalheConcreto> detalhes = new ArrayList<>();
        double volumeTotal = 0.0;

        for (MateriaisDTO.ArestaRequest arestaReq : request.arestas()) {

            // Persiste vértices (reutiliza se já existir pelo nome)
            Vertice origem = verticeRepository.findByNome(arestaReq.verticeOrigem())
                    .orElseGet(() -> verticeRepository.save(new Vertice(null, arestaReq.verticeOrigem())));

            Vertice destino = verticeRepository.findByNome(arestaReq.verticeDestino())
                    .orElseGet(() -> verticeRepository.save(new Vertice(null, arestaReq.verticeDestino())));

            // Monta e persiste a aresta
            Aresta aresta = new Aresta();
            aresta.setNome(arestaReq.nome());
            aresta.setVerticeOrigem(origem);
            aresta.setVerticeDestino(destino);
            aresta.setComprimento(arestaReq.comprimento());
            aresta.setLargura(arestaReq.largura());
            aresta.setAltura(arestaReq.altura());
            aresta.setTipoAbertura(
                    arestaReq.tipoAbertura() != null ? arestaReq.tipoAbertura() : TipoAbertura.NENHUMA
            );
            aresta.setAlturaAbertura(arestaReq.alturaAbertura());
            aresta.setComprimentoAbertura(arestaReq.comprimentoAbertura());

            arestaRepository.save(aresta);

            // Cálculo: V = L × AlturaViga × C
            double volume = arestaReq.largura() * request.alturaViga() * arestaReq.comprimento();
            volume = Math.round(volume * 1000.0) / 1000.0; // arredonda em 3 casas
            volumeTotal += volume;

            detalhes.add(new MateriaisDTO.DetalheConcreto(
                    arestaReq.nome(),
                    arestaReq.largura(),
                    request.alturaViga(),
                    arestaReq.comprimento(),
                    volume
            ));
        }

        double volumeTotalArredondado = Math.round(volumeTotal * 1000.0) / 1000.0;

        return new MateriaisDTO.ConcreteResponse(volumeTotalArredondado, detalhes);
    }
}
