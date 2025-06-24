package com.proyecto.RegistroEgresados_Web.exception;

import lombok.Getter;

@Getter
public class NoEncontradoException extends RuntimeException{

    public NoEncontradoException(String message) {
        super(message);
    }
}
