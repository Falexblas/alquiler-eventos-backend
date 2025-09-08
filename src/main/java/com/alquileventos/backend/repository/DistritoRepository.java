package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Integer> {
    
    Optional<Distrito> findByNombreDistrito(String nombreDistrito);
    
    boolean existsByNombreDistrito(String nombreDistrito);
}
