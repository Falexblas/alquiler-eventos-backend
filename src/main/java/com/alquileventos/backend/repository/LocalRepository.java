package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LocalRepository extends JpaRepository<Local, Integer> {
    
    List<Local> findByDistrito_IdDistrito(Integer idDistrito);
    
    List<Local> findByEstado(Local.EstadoLocal estado);
    
    @Query("SELECT l FROM Local l WHERE l.aforoMaximo >= :aforo")
    List<Local> findByAforoMinimoRequerido(@Param("aforo") Integer aforo);
    
    @Query("SELECT l FROM Local l WHERE l.precioHora BETWEEN :precioMin AND :precioMax")
    List<Local> findByRangoPrecio(@Param("precioMin") BigDecimal precioMin, @Param("precioMax") BigDecimal precioMax);
    
    @Query("SELECT l FROM Local l JOIN l.tiposEvento te WHERE te.idTipoEvento = :idTipoEvento")
    List<Local> findByTipoEvento(@Param("idTipoEvento") Integer idTipoEvento);
    
    @Query("SELECT l FROM Local l WHERE l.nombreLocal LIKE %:nombre% OR l.descripcion LIKE %:descripcion%")
    List<Local> findByNombreOrDescripcionContaining(@Param("nombre") String nombre, @Param("descripcion") String descripcion);
}
