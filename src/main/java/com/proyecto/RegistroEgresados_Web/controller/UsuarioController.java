package com.proyecto.RegistroEgresados_Web.controller;

import com.proyecto.RegistroEgresados_Web.dto.CredencialesDTO;
import com.proyecto.RegistroEgresados_Web.dto.UsuarioDTO;
import com.proyecto.RegistroEgresados_Web.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(UsuarioDTO usuarioDTO){
        return usuarioService.registrarUsuario(usuarioDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(CredencialesDTO credencialesDTO){
        return usuarioService.login(credencialesDTO);
    }
}
