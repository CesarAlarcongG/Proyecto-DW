package com.proyecto.RegistroEgresados_Web.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienciaLaboralDTO {
    private int id;
    private String empresa;
    private String cargo;
    private Date fechaIngreso;
    private Date fechaSalida;
}
