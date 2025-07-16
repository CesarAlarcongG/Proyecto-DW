package com.proyecto.RegistroEgresados_Web.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.RegistroEgresados_Web.dto.ExperienciaLaboralDTO;
import com.proyecto.RegistroEgresados_Web.persistence.model.Egresado;
import com.proyecto.RegistroEgresados_Web.persistence.model.ExperienciaLaboral;
import com.proyecto.RegistroEgresados_Web.persistence.repository.ExperienciaLaboralRepository;
import com.proyecto.RegistroEgresados_Web.service.interfaces.ExperienciaLaboralService;

@Service
public class ExperienciaLaboralServiceImpl implements ExperienciaLaboralService{
    @Autowired
    private ExperienciaLaboralRepository experienciaLaboralRepository;


    public List<ExperienciaLaboral> mapearExperienciaLaboral(List<ExperienciaLaboralDTO> experienciaLaboralDTO, Egresado egresado) {
        List<ExperienciaLaboral> lista = new ArrayList<>();

        for (ExperienciaLaboralDTO experiencia : experienciaLaboralDTO) {
            ExperienciaLaboral exp = ExperienciaLaboral.builder()
                    .empresa(experiencia.getEmpresa())
                    .cargo(experiencia.getCargo())
                    .fechaIngreso(experiencia.getFechaIngreso())
                    .fechaSalida(experiencia.getFechaSalida())
                    .numerocontacto(experiencia.getNumerocontacto())
                    .numeroDeEmpresa(experiencia.getNumeroDeEmpresa())
                    .paginaWebEmpresa(experiencia.getPaginaWebEmpresa())
                    .direccion(experiencia.getDireccion())
                    .egresado(egresado) // Establece la relación con el egresado
                    .build();
            lista.add(exp);
        }
        return lista;
    }

    public List<ExperienciaLaboral> findAllByEgresado(Egresado egresado){
        return experienciaLaboralRepository.findAllByEgresado(egresado);
    }

    @Override
    public List<ExperienciaLaboral> saveAll(List<ExperienciaLaboral> experienciaLaborals) {
        return experienciaLaboralRepository.saveAll(experienciaLaborals);
    }

}
