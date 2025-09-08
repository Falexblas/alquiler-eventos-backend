package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.TipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoEventoRepository extends JpaRepository<TipoEvento, Integer> {
    
    Optional<TipoEvento> findByNombreTipo(String nombreTipo);
    
    boolean existsByNombreTipo(String nombreTipo);
}
