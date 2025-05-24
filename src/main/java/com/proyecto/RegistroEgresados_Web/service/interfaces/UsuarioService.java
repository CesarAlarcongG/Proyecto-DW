package com.proyecto.RegistroEgresados_Web.service.interfaces;

import com.proyecto.RegistroEgresados_Web.dto.CredencialesDTO;
import com.proyecto.RegistroEgresados_Web.dto.UsuarioDTO;
import com.proyecto.RegistroEgresados_Web.persistence.model.Usuario;

import java.util.Optional;

import org.springframework.http.ResponseEntity;


public interface UsuarioService {
    ResponseEntity<?> registrarUsuario(UsuarioDTO usuarioDto);
    ResponseEntity<?> login(CredencialesDTO credencialesDTO);
    Optional<Usuario> findById(int id);
}
