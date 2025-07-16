package com.proyecto.RegistroEgresados_Web.service.implement;

import com.proyecto.RegistroEgresados_Web.dto.CredencialesDTO;
import com.proyecto.RegistroEgresados_Web.dto.EgresadoDTO;
import com.proyecto.RegistroEgresados_Web.dto.EgresadoRespuestaDto;
import com.proyecto.RegistroEgresados_Web.dto.ExperienciaLaboralDTO;
import com.proyecto.RegistroEgresados_Web.persistence.model.Egresado;
import com.proyecto.RegistroEgresados_Web.persistence.model.ExperienciaLaboral;
import com.proyecto.RegistroEgresados_Web.persistence.model.HistorialActualizacion;
import com.proyecto.RegistroEgresados_Web.persistence.model.enums.Rol;
import com.proyecto.RegistroEgresados_Web.persistence.repository.EgresadoRepository;
import com.proyecto.RegistroEgresados_Web.service.JwtService;
import com.proyecto.RegistroEgresados_Web.service.interfaces.EgresadoService;
import com.proyecto.RegistroEgresados_Web.service.interfaces.ExperienciaLaboralService;
import com.proyecto.RegistroEgresados_Web.service.interfaces.HistorialActualizacionService;
import com.proyecto.RegistroEgresados_Web.util.ExcelExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class EgresadoServiceImpl implements EgresadoService {

    @Autowired
    private EgresadoRepository egresadoRepository;
    @Autowired
    private ExperienciaLaboralService experienciaLaboralService;
    @Autowired
    private HistorialActualizacionService historialActualizacionService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ExcelExporter excelExporter;


    @Override
    public ResponseEntity<?> crear(EgresadoDTO egresadoDTO) {
        //1. Verificar si ya hay otro estudiante con el correo 
        if (verificarExistenciaPorEmail(egresadoDTO.getEmail())) {
            System.out.println(verificarExistenciaPorEmail(egresadoDTO.getEmail()));
            return new ResponseEntity<>("El correo ya fue creado con otra cuenta", HttpStatus.CONFLICT);
        }

        //2. Crear un objeto usuario con la información del DTO
        Egresado egresadoDAtos = mapearEgresado(egresadoDTO);

        //3. Almacenamos en la BD
        Egresado egresado = egresadoRepository.save(egresadoDAtos);

        //3. Verificamos si tiene nuevos campos de experiencia laboral
        if (!egresadoDTO.getExperienciaLaboralDTO().isEmpty()) {
            añadirExperienciaLaboral(egresadoDTO.getExperienciaLaboralDTO(), egresadoDAtos);
        }

        //4. Almacenar en la BD
        Egresado egresadoEnLaBD = egresadoRepository.save(egresadoDAtos);
        if (egresadoEnLaBD == null) {
            return new ResponseEntity<>("No se pudo crear el egresado en la BD", HttpStatus.CONFLICT);
        }


        return new ResponseEntity<>(egresado, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> login(CredencialesDTO credencialesDto) {
        try {
            // 1. Autenticación
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credencialesDto.getEmail(),
                            credencialesDto.getContraseña()
                    )
            );

            // 2. Obtener usuario
            Egresado egresado = egresadoRepository.findByEmail(credencialesDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            // 3. Generar token
            String token = jwtService.getToken(egresado);
            egresado.setContraseña("hackthisballs");

            return ResponseEntity.ok(mapearRespuesta(egresado, token));

        } catch (BadCredentialsException e) {
            // Credenciales inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));

        } catch (DisabledException e) {
            // Usuario deshabilitado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario deshabilitado"));

        } catch (LockedException e) {
            // Cuenta bloqueada
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Cuenta bloqueada"));

        } catch (Exception e) {
            // Error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en el servidor: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> obtenerPorId(int id) {
        Egresado egresado = egresadoRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Egresado no encontrado"));

        return new ResponseEntity<>(egresado, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> obtenerTodosLosEgresados() {
        List<Egresado> egresados = egresadoRepository.findAll();

        if (egresados.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay egresados registrados");
        }

        return ResponseEntity.ok(egresados);
    }

    @Override
    public ResponseEntity<?> actualizar(EgresadoDTO egresadoDTO) {
        //1. Obtenemos al egresado de la BD
        Egresado egresado = egresadoRepository.findById(egresadoDTO.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Egresado no encontrado"));

        //2. Obtenemos el historial de actualizaciones
        List<HistorialActualizacion> historialActualizacionList =
                historialActualizacionService.findAllByEgresado(egresado);

        //3. Mapeamos la nueva actualización y lo guardamos en la BD

        HistorialActualizacion nuevaActualización =
                historialActualizacionService.mapearHistorialYGuardar(egresadoDTO.getHistorialActualizaciones(), egresado);

        //4. Agregamos la actualización a la lista
        historialActualizacionList.add(nuevaActualización);

        //5. Agregamos la lista de actualizaciones al egresado
        egresado.setHistorialActualizacion(historialActualizacionList);

        //6. Varificamos si hay una nueva experiencia laboral
        if (egresadoDTO.getExperienciaLaboralDTO() != null) {
            añadirExperienciaLaboral(egresadoDTO.getExperienciaLaboralDTO(), egresado);
        }

        //7. Cambioamos los campos en caso haya actualizaciones
        Egresado egresadoActualizado = actualizarCampos(egresado, egresadoDTO);

        //8. Almacenamos en la BD
        if (egresadoRepository.save(egresadoActualizado) == null) {
            return new ResponseEntity<>("El egresado no se puedo actualizar", HttpStatus.NOT_ACCEPTABLE);

        }

        return new ResponseEntity<>(egresadoActualizado, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> eliminar(int id) {
        egresadoRepository.deleteById(id);
        return new ResponseEntity<>("Egresado eliminado", HttpStatus.OK);
    }

    //Excel
    @Override
    public ResponseEntity<?> exportarAExcel() {
        try {
            List<Egresado> egresados = egresadoRepository.findAll();
            byte[] excel = excelExporter.exportarEgresado(egresados);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=egresados.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excel);
        } catch (Exception e) {
            System.out.println("Problemas con el excel: " + e);

        }


        return null;
    }

    @Override
    public ResponseEntity<?> exportarEgresadoYExperienciaAExcel() {
        try {
            List<Egresado> egresados = egresadoRepository.findAll();
            byte[] excel = excelExporter.exportarEgresadosYExperienciaLaboral(egresados);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=egresados.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excel);
        } catch (Exception e) {
            System.out.println("Problemas con el excel: " + e);

        }
        return null;
    }

    private boolean verificarExistenciaPorEmail(String email) {
        return egresadoRepository.findByEmail(email).isPresent();

    }

    private Egresado mapearEgresado(EgresadoDTO egresadoDTO) {

        return Egresado.builder()
                .nombre(egresadoDTO.getNombre())
                .apellido(egresadoDTO.getApellido())
                .email(egresadoDTO.getEmail())
                .contraseña(passwordEncoder.encode(egresadoDTO.getContraseña()))
                .carrera(egresadoDTO.getCarrera())
                .universidad(egresadoDTO.getUniversidad())
                .facultad(egresadoDTO.getFacultad())
                .especialidad(egresadoDTO.getEspecialidad())
                .rol(Rol.ADMINISTRADOR)
                .fechaNacimiento(egresadoDTO.getFechaNacimiento())
                .fechaIngreso(egresadoDTO.getFechaIngreso())
                .fechaEgreso(egresadoDTO.getFechaEgreso())
                .ponderado(egresadoDTO.getPonderado())
                .build();
    }

    private Egresado ObetenerPorEmail(String email) {
        return egresadoRepository.findByEmail(email).get();
    }

    public void añadirExperienciaLaboral(List<ExperienciaLaboralDTO> experienciaLaboralDTO, Egresado egresado) {
        List<ExperienciaLaboral> experienciaPrevia;
        //1. Verificamos is es un nuevo usuario

        experienciaPrevia = experienciaLaboralService.findAllByEgresado(egresado);


        //2. Mapeamos la experiencia nueva y relacionamos con el ususario
        List<ExperienciaLaboral> experienciaLaboral = experienciaLaboralService.mapearExperienciaLaboral(experienciaLaboralDTO, egresado);
        //3. Guardar la experiencia en la BD
        if (experienciaLaboralService.saveAll(experienciaLaboral).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se pudo almacenar la experiencia en la BD");
        }
        //3. Combinamos ambas tablas
        experienciaPrevia.addAll(experienciaLaboral);

        //4. Relacionamos la nueva lista al usuario
        egresado.setIdExperienciaLaboral(experienciaPrevia);

    }

    private Egresado actualizarCampos(Egresado egresado, EgresadoDTO egresadoDTO) {
        return Egresado.builder()
                .id(egresado.getId())
                .nombre(egresadoDTO.getNombre())
                .email(egresadoDTO.getEmail())
                .contraseña(passwordEncoder.encode(egresadoDTO.getContraseña()))
                .apellido(egresadoDTO.getApellido())
                .carrera(egresadoDTO.getCarrera())
                .fechaNacimiento(egresadoDTO.getFechaNacimiento())
                .fechaIngreso(egresadoDTO.getFechaIngreso())
                .fechaEgreso(egresadoDTO.getFechaEgreso())
                .rol(Rol.ADMINISTRADOR)
                .ponderado(egresadoDTO.getPonderado())
                .historialActualizacion(egresado.getHistorialActualizacion())
                .idExperienciaLaboral(egresado.getIdExperienciaLaboral())
                .build();

    }

    private EgresadoRespuestaDto mapearRespuesta(Egresado egresado, String token) {
        return EgresadoRespuestaDto.builder()
                .egresado(egresado)
                .token(token)
                .build();
    }


}
