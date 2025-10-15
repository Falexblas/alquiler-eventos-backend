package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {
    
    Optional<MetodoPago> findByNombreMetodo(String nombreMetodo);
    
    boolean existsByNombreMetodo(String nombreMetodo);
}
