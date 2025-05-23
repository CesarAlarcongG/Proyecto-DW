package com.proyecto.RegistroEgresados_Web.persistence.model;

import com.proyecto.RegistroEgresados_Web.persistence.model.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String apellido;
    private String email;
    private String contraseña;
    private int telefono;

    @OneToMany(mappedBy = "usuario")
    private List<HistorialActualizacion> historialActualizacions;

    @Enumerated(EnumType.STRING)
    private Rol rol;


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
