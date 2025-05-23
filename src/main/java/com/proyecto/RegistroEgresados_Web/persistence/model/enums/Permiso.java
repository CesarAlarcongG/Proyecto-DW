package com.proyecto.RegistroEgresados_Web.persistence.model.enums;

import lombok.Getter;

public enum Permiso {
    USUARIO_CREATE,  // ADMIN
    USUARIO_EDIT,    // ADMIN
    USUARIO_DELETE,  // ADMIN
    USUARIO_VIEW,    // PROF
    ALUMNO_EDIT;

    @Getter
    private String permisos;
}
