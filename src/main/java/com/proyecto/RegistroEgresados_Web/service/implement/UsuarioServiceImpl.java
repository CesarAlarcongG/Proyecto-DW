package com.proyecto.RegistroEgresados_Web.service.implement;

import com.proyecto.RegistroEgresados_Web.dto.CredencialesDTO;
import com.proyecto.RegistroEgresados_Web.dto.Token;
import com.proyecto.RegistroEgresados_Web.persistence.model.Usuario;
import com.proyecto.RegistroEgresados_Web.persistence.model.enums.Rol;
import com.proyecto.RegistroEgresados_Web.persistence.repository.UsuarioRepository;
import com.proyecto.RegistroEgresados_Web.service.JwtService;
import com.proyecto.RegistroEgresados_Web.service.interfaces.UsuarioService;
import com.proyecto.RegistroEgresados_Web.dto.UsuarioDTO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @Override
    public ResponseEntity<?> registrarUsuario(UsuarioDTO ususarioDto){
        //1. Validar si el correo ya fue registrado en otra cuenta
        if (validarExistenciaPorCorreo(ususarioDto.getEmail())){
           return new ResponseEntity<>("El correo ya fue registrado en otra cuenta", HttpStatus.CONFLICT);
        }

        //2. Convertir el dto en objeto Usuario y codificamos la contraseña
        Usuario usuario = convertirAUsuario(ususarioDto);

        //3. Almacenamos el usuario en la BD
        Usuario usuarioAlmacenado = guargarUsuario(usuario);
        if(usuarioAlmacenado == null){
            return new ResponseEntity<>("Hay un problema al almacenar en la BD", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //4. Quitamos la contraseña del usuario
        usuarioAlmacenado.setContraseña(null);
        usuarioAlmacenado.setRol(Rol.ADMINISTRADOR);

        return new ResponseEntity<>(usuarioAlmacenado, HttpStatus.OK);
    }



    @Override
    public ResponseEntity<?> login( CredencialesDTO credencialesDto) {
        try {
            // 1. Autenticación
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credencialesDto.getEmail(),
                            credencialesDto.getContraseña()
                    )
            );

            // 2. Obtener usuario
            Usuario usuario = usuarioRepository.findByEmail(credencialesDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            // 3. Generar token
            String token = jwtService.getToken(usuario);


            return ResponseEntity.ok(mapearRespuesta(usuario, new Token(token)));

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
    public Optional<Usuario> findById(int id) {
        return usuarioRepository.findById(id);
    }


    public boolean validarExistenciaPorCorreo(String email){
        if(usuarioRepository.findByEmail(email).isPresent()){
            return true;
        }
        return false;
    }

    public Usuario convertirAUsuario(UsuarioDTO usuarioDTO){
        return Usuario.builder()
                .nombre(usuarioDTO.getNombre())
                .apellido(usuarioDTO.getApellido())
                .email(usuarioDTO.getEmail())
                .contraseña(passwordEncoder.encode(usuarioDTO.getContraseña()))
                .telefono(usuarioDTO.getTelefono())
                .rol(Rol.ADMINISTRADOR)
                .build();
    }

    public Usuario guargarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }
    private UsuarioDTO mapearRespuesta(Usuario usuario, Token token){
        return UsuarioDTO.builder()
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .token(token)
                .rol(usuario.getRol().toString())
                .build();
    }
}
