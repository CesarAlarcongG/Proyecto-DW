package com.proyecto.RegistroEgresados_Web.service.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.RegistroEgresados_Web.dto.ExperienciaLaboralDTO;
import com.proyecto.RegistroEgresados_Web.persistence.model.Egresado;
import com.proyecto.RegistroEgresados_Web.persistence.model.ExperienciaLaboral;

@Service
public interface ExperienciaLaboralService {
    List<ExperienciaLaboral> mapearExperienciaLaboral(List<ExperienciaLaboralDTO> experienciaLaboralDTO, Egresado egresado);
    List<ExperienciaLaboral> findAllByEgresado(Egresado egresado);
    List<ExperienciaLaboral> saveAll(List<ExperienciaLaboral> experienciaLaborals);
}
