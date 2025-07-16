package com.proyecto.RegistroEgresados_Web.persistence.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.proyecto.RegistroEgresados_Web.persistence.model.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Egresado implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String apellido;
    private String email;
    private String contraseña;
    private String carrera;

    private String universidad;
    private String facultad;
    private String especialidad;

    private Date fechaNacimiento;
    private Date fechaIngreso;
    private Date fechaEgreso;
    private float ponderado;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToMany(mappedBy = "egresado", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ExperienciaLaboral> idExperienciaLaboral;

    @OneToMany(mappedBy = "egresado", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<HistorialActualizacion> historialActualizacion;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rol.getAuthorities();
    }


    @Override
    public String getPassword() {
        return this.contraseña;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
