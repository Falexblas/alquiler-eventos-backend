package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByDni(String dni);

    List<Usuario> findByRol_IdRol(Integer idRol);
    @Query("SELECT u FROM Usuario u " +
            "WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Usuario> findByNombreOApellido(@Param("keyword") String keyword);
}
