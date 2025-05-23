package com.proyecto.RegistroEgresados_Web.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Egresado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String apellido;
    private String email;
    private String carrera;
    private Date fechaNacimiento;
    private Date fechaIngreso;
    private Date fechaEgreso;
    private float ponderado;

    @OneToMany(mappedBy = "egresado")
    private List<ExperienciaLaboral> idExperienciaLaboral;

    @OneToMany(mappedBy = "egresado")
    private List<HistorialActualizacion> historialActualizacion;

}
