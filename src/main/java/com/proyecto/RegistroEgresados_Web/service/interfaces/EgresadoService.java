package com.proyecto.RegistroEgresados_Web.service.interfaces;

import com.proyecto.RegistroEgresados_Web.dto.CredencialesDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.proyecto.RegistroEgresados_Web.dto.EgresadoDTO;


@Service
public interface EgresadoService {
    ResponseEntity<?> crear(EgresadoDTO egresadoDTO);
    ResponseEntity<?> obtenerPorId(int id);
    ResponseEntity<?> obtenerTodosLosEgresados();
    ResponseEntity<?> actualizar(EgresadoDTO egresadoDTO);
    ResponseEntity<?> eliminar(int id);
    ResponseEntity<?> login(CredencialesDTO credencialesDTO);
}
