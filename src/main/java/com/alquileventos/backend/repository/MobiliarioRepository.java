package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.Mobiliario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobiliarioRepository extends JpaRepository<Mobiliario, Integer> {
    
    List<Mobiliario> findByStockTotalGreaterThan(Integer stock);
    
    @Query("SELECT m FROM Mobiliario m WHERE m.nombre LIKE %:nombre%")
    List<Mobiliario> findByNombreContaining(@Param("nombre") String nombre);
    
    @Query("SELECT m FROM Mobiliario m WHERE m.stockTotal >= :cantidadRequerida")
    List<Mobiliario> findDisponiblesPorCantidad(@Param("cantidadRequerida") Integer cantidadRequerida);
}
