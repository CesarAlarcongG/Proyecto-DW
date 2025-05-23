package com.proyecto.RegistroEgresados_Web.persistence.model.enums;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Rol {

    ASMINISTRADOR(Set.of(
            Permiso.USUARIO_CREATE,
            Permiso.USUARIO_VIEW,
            Permiso.USUARIO_EDIT,
            Permiso.USUARIO_DELETE
    )
    );

    @Getter
    private final Set<Permiso> permisos;

    Rol(Set<Permiso> permisos) {
        this.permisos = permisos;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Agregar el rol como autoridad (prefijo ROLE_)
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        // Agregar los permisos como autoridades
        authorities.addAll(
                permisos.stream()
                        .map(permiso -> new SimpleGrantedAuthority(permiso.name()))
                        .toList()
        );

        return authorities;
    }


}
