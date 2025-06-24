package com.proyecto.RegistroEgresados_Web.dto;

import java.util.Date;
import java.util.List;

import lombok.*;
import org.springframework.lang.NonNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgresadoDTO {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String contraseña;
    private String carrera;
    private Date fechaNacimiento;
    private Date fechaIngreso;
    private Date fechaEgreso;
    private float ponderado;
    private List<ExperienciaLaboralDTO> experienciaLaboralDTO;
    private HistorialActualizacionDTO historialActualizaciones;
}
