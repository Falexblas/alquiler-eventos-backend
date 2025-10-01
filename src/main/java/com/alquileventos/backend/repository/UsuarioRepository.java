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
    @Query("SELECT u FROM Usuario u WHERE u.nombre LIKE %:nombre% OR u.apellido LIKE %:apellido%")
    List<Usuario> findByNombreOrApellidoContaining(@Param("nombre") String nombre, @Param("apellido") String apellido);
}
