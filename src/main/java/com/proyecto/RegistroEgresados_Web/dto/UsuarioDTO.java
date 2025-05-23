package com.proyecto.RegistroEgresados_Web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String contraseña;
    private int telefono;
    private String rol;
    private Token token;
}
