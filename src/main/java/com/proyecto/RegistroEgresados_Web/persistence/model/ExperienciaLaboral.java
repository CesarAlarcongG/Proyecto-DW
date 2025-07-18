package com.proyecto.RegistroEgresados_Web.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienciaLaboral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String empresa;
    private String cargo;
    private Date fechaIngreso;
    private Date fechaSalida;

    private int numerocontacto;
    private int numeroDeEmpresa;
    private String paginaWebEmpresa;
    private String direccion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_egresado", referencedColumnName = "id")
    @JsonBackReference
    private Egresado egresado;

}

