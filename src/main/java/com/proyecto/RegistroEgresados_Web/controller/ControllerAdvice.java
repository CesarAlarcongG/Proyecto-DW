package com.proyecto.RegistroEgresados_Web.controller;


import com.proyecto.RegistroEgresados_Web.exception.NoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(value = NoEncontradoException.class)
    public ResponseEntity<String> entityExceptionHandler(NoEncontradoException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
