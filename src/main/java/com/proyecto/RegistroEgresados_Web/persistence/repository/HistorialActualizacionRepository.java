package com.proyecto.RegistroEgresados_Web.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.RegistroEgresados_Web.persistence.model.Egresado;
import com.proyecto.RegistroEgresados_Web.persistence.model.HistorialActualizacion;

@Repository
public interface HistorialActualizacionRepository extends JpaRepository<HistorialActualizacion, Integer>{
    List<HistorialActualizacion> findAllByEgresado(Egresado egresado);
}
