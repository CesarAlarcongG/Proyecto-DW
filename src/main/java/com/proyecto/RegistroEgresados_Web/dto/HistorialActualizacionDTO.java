package com.proyecto.RegistroEgresados_Web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialActualizacionDTO {
    private int id;
    private int idUsuario;
    private String descripción;

}
