package com.proyecto.RegistroEgresados_Web.service.implement;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proyecto.RegistroEgresados_Web.dto.HistorialActualizacionDTO;
import com.proyecto.RegistroEgresados_Web.persistence.model.Egresado;
import com.proyecto.RegistroEgresados_Web.persistence.model.HistorialActualizacion;
import com.proyecto.RegistroEgresados_Web.persistence.model.Usuario;
import com.proyecto.RegistroEgresados_Web.persistence.repository.HistorialActualizacionRepository;
import com.proyecto.RegistroEgresados_Web.service.interfaces.HistorialActualizacionService;
import com.proyecto.RegistroEgresados_Web.service.interfaces.UsuarioService;

@Service
public class HistorialActualizacionServiceImpl implements HistorialActualizacionService{
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private HistorialActualizacionRepository historialActualizacionRepository;

    public HistorialActualizacion mapearHistorialYGuardar(HistorialActualizacionDTO hActualizacion, Egresado egresado){

        Usuario usuario = usuarioService.findById(hActualizacion.getIdUsuario())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con id "+hActualizacion.getIdUsuario()+" no encontrado"));

        HistorialActualizacion historialActualizacion = HistorialActualizacion.builder()
                .fechaActualizacion(new Date())
                .descripcion(hActualizacion.getDescripción())
                .usuario(usuario)
                .build();

        return historialActualizacionRepository.save(historialActualizacion);

    }

    public List<HistorialActualizacion> findAllByEgresado(Egresado egresado){
        return historialActualizacionRepository.findAllByEgresado(egresado);
    }
}
