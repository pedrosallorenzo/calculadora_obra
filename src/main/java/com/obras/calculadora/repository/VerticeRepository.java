package com.obras.calculadora.repository;

import com.obras.calculadora.model.Vertice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerticeRepository extends JpaRepository<Vertice, Long> {
    Optional<Vertice> findByNome(String nome);
}
