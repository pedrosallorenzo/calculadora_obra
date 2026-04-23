package com.obras.calculadora.repository;

import com.obras.calculadora.model.Aresta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArestaRepository extends JpaRepository<Aresta, Long> {
}
