package com.proyecto.RegistroEgresados_Web.config.data;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.proyecto.RegistroEgresados_Web.persistence.model.Usuario;
import com.proyecto.RegistroEgresados_Web.persistence.model.enums.Rol;
import com.proyecto.RegistroEgresados_Web.persistence.repository.UsuarioRepository;



@Configuration
public class DatosIniciales {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner administrador(UsuarioRepository usuarioRepository) {
        Usuario usuario = Usuario.builder()
                .email("root@root.root")
                .contraseña(passwordEncoder.encode("root12345678"))
                .rol(Rol.ADMINISTRADOR)
                .build();

        return args -> {
            // Verifica si ya hay datos para evitar duplicados
            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(usuario);

                System.out.println("Datos iniciales cargados! " +
                        "\n usuario = root@root.root" +
                        "\n contraseña = root12345678");
            } else if(usuarioRepository.count() != 0){
                System.out.println("ya hay un usuario en la BD");
            }
        };
    }
}
