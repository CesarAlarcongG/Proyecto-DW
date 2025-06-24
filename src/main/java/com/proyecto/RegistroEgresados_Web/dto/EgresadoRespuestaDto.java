package com.proyecto.RegistroEgresados_Web.dto;

import com.proyecto.RegistroEgresados_Web.persistence.model.Egresado;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EgresadoRespuestaDto {
    private Egresado egresado;
    private String token;
}
