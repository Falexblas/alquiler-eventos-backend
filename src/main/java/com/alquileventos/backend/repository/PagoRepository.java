package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    
    List<Pago> findByReserva_IdReserva(Integer idReserva);
    
    List<Pago> findByEstado(Pago.EstadoPago estado);
    
    List<Pago> findByMetodoPago(Pago.MetodoPago metodoPago);
    
    @Query("SELECT p FROM Pago p WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin")
    List<Pago> findByRangoFechas(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                @Param("fechaFin") LocalDateTime fechaFin);
}
