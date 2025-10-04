package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.Local;
import com.alquileventos.backend.entity.Local.EstadoLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocalRepository extends JpaRepository<Local, Integer> {

    List<Local> findByNombreLocalContainingIgnoreCase(String nombre);
    
    List<Local> findByEstado(EstadoLocal estado);

    @Query("SELECT l FROM Local l " +
            "LEFT JOIN FETCH l.fotos " +
            "LEFT JOIN FETCH l.tiposEvento " +
            "WHERE l.idLocal = :id")
    Optional<Local> findByIdWithFotos(@Param("id") Integer id);

    @Query("SELECT DISTINCT l FROM Local l " +
            "LEFT JOIN l.tiposEvento te " +
            "WHERE l.estado = 'DISPONIBLE' " +
            "AND (:idDistrito IS NULL OR l.distrito.idDistrito = :idDistrito) " +
            "AND (:idTipoEvento IS NULL OR te.idTipoEvento = :idTipoEvento) " +
            "AND (:aforoMin IS NULL OR l.aforoMaximo >= :aforoMin) " +
            "AND (:precioMin IS NULL OR l.precioHora >= :precioMin) " +
            "AND (:precioMax IS NULL OR l.precioHora <= :precioMax)")
    List<Local> buscarConFiltros(
            @Param("idDistrito") Integer idDistrito,
            @Param("idTipoEvento") Integer idTipoEvento,
            @Param("aforoMin") Integer aforoMin,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );
}
