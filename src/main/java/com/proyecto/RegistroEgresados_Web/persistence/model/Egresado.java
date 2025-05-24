package com.proyecto.RegistroEgresados_Web.persistence.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(mappedBy = "egresado", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ExperienciaLaboral> idExperienciaLaboral;

    @OneToMany(mappedBy = "egresado", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<HistorialActualizacion> historialActualizacion;

}
