package com.obras.calculadora.repository;

import com.obras.calculadora.model.Comodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComodoRepository extends JpaRepository<Comodo, Long> {
}
