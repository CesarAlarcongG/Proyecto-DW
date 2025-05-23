package com.proyecto.RegistroEgresados_Web.service.interfaces;

import com.proyecto.RegistroEgresados_Web.dto.CredencialesDTO;
import com.proyecto.RegistroEgresados_Web.dto.UsuarioDTO;
import org.springframework.http.ResponseEntity;


public interface UsuarioService {
    ResponseEntity<?> registrarUsuario(UsuarioDTO usuarioDto);
    ResponseEntity<?> login(CredencialesDTO credencialesDTO);
}
