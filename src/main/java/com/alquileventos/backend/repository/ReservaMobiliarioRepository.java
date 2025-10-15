package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.ReservaMobiliario;
import com.alquileventos.backend.entity.ReservaMobiliario.ReservaMobiliarioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaMobiliarioRepository extends JpaRepository<ReservaMobiliario, ReservaMobiliarioId> {
    
    List<ReservaMobiliario> findByReserva_IdReserva(Integer idReserva);
    
    List<ReservaMobiliario> findByMobiliario_IdMobiliario(Integer idMobiliario);
    
    @Query("SELECT SUM(rm.cantidad) FROM ReservaMobiliario rm " +
           "WHERE rm.mobiliario.idMobiliario = :idMobiliario " +
           "AND rm.reserva.fecha = :fecha " +
           "AND rm.reserva.estado != 'CANCELADA'")
    Integer findCantidadReservadaPorFecha(@Param("idMobiliario") Integer idMobiliario, 
                                          @Param("fecha") LocalDate fecha);
}
