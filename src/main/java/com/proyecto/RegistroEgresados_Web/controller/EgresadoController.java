package com.proyecto.RegistroEgresados_Web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyecto.RegistroEgresados_Web.dto.EgresadoDTO;
import com.proyecto.RegistroEgresados_Web.service.interfaces.EgresadoService;

@Controller
@RequestMapping("/egresado")
public class EgresadoController {
    @Autowired
    private EgresadoService egresadoService;

    //Create
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody EgresadoDTO egresadoDTO){
        return egresadoService.crear(egresadoDTO);
    }

    //obtener por id
    @GetMapping("/obtener/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id){
        return egresadoService.obtenerPorId(id);
    }
    //obtener todos
    @GetMapping("/obtener/todos")
    public ResponseEntity<?> obtenerPorId(){
        return egresadoService.obtenerTodosLosEgresados();
    }

    //ACtualizar
    @PutMapping("/actualizar")
    public ResponseEntity<?> obtenerPorId(@RequestBody EgresadoDTO egresadoDTO){
        return egresadoService.actualizar(egresadoDTO);
    }

}
