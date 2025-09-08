package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    
    Optional<Rol> findByNombreRol(String nombreRol);
    
    boolean existsByNombreRol(String nombreRol);
}
