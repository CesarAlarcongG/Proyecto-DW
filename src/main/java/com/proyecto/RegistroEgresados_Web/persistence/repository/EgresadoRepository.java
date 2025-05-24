package com.proyecto.RegistroEgresados_Web.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.RegistroEgresados_Web.persistence.model.Egresado;

@Repository
public interface EgresadoRepository extends JpaRepository<Egresado, Integer> {
    @EntityGraph(value = "egresado-con-experiencias", type = EntityGraphType.FETCH)
    List<Egresado> findAll();
    
    Optional<Egresado> findByEmail(String email);
}
