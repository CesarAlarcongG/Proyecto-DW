package com.proyecto.RegistroEgresados_Web.config.security;


import com.proyecto.RegistroEgresados_Web.persistence.model.enums.Permiso;
import com.proyecto.RegistroEgresados_Web.persistence.repository.EgresadoRepository;
import com.proyecto.RegistroEgresados_Web.persistence.repository.UsuarioRepository;
import com.proyecto.RegistroEgresados_Web.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import java.util.Optional;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Security {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EgresadoRepository egresadoRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, UserDetailsService userDetailsService)throws Exception{
        JwtFiltro jwtFiltro = new JwtFiltro(jwtService, userDetailsService);
        return httpSecurity
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Agregar configuración de CORS

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/usuario/login", "/egresado/crear").permitAll()
                        .requestMatchers("/egresado/login").permitAll()
                        .requestMatchers("/usuario/registro").hasAuthority(Permiso.USUARIO_CREATE.toString())
                        .requestMatchers("/egresado/actualizar").hasAnyAuthority(
                                Permiso.USUARIO_EDIT.toString(),
                                Permiso.ALUMNO_EDIT.toString())
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFiltro, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<? extends UserDetails> usuario = usuarioRepository.findByEmail(username);
            if (usuario.isPresent()) return usuario.get();

            Optional<? extends UserDetails> egresado = egresadoRepository.findByEmail(username);
            if (egresado.isPresent()) return egresado.get();

            throw new UsernameNotFoundException("Usuario o egresado no encontrado con email: " + username);
        };
    }


    //Este es la configuración de cors
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://localhost:4321", "http://localhost:8080")); // Especifica los orígenes permitidos
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        corsConfiguration.setAllowCredentials(true); // Permitir credenciales
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
