package com.proyecto.RegistroEgresados_Web.persistence.repository;

import com.proyecto.RegistroEgresados_Web.persistence.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
}
