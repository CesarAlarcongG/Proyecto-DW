package com.proyecto.RegistroEgresados_Web.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@ToString
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

    @ManyToOne
    @JoinColumn(name = "id_egresado", referencedColumnName = "id")
    private Egresado egresado;

}

