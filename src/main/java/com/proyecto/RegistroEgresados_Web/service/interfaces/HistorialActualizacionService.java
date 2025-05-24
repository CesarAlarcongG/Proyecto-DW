package com.proyecto.RegistroEgresados_Web.service.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.RegistroEgresados_Web.dto.HistorialActualizacionDTO;
import com.proyecto.RegistroEgresados_Web.persistence.model.Egresado;
import com.proyecto.RegistroEgresados_Web.persistence.model.HistorialActualizacion;

@Service
public interface HistorialActualizacionService {
    HistorialActualizacion mapearHistorialYGuardar(HistorialActualizacionDTO hActualizacion, Egresado egresado);
    List<HistorialActualizacion> findAllByEgresado(Egresado egresado);
}
