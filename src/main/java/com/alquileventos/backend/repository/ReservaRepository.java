package com.alquileventos.backend.repository;

import com.alquileventos.backend.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    
    List<Reserva> findByUsuario_IdUsuario(Integer idUsuario);
    
    List<Reserva> findByLocal_IdLocal(Integer idLocal);
    
    List<Reserva> findByEstado(Reserva.EstadoReserva estado);
    
    List<Reserva> findByFecha(LocalDate fecha);
    
    @Query("SELECT r FROM Reserva r WHERE r.local.idLocal = :idLocal AND r.fecha = :fecha " +
           "AND ((r.horaInicio <= :horaInicio AND r.horaFin > :horaInicio) " +
           "OR (r.horaInicio < :horaFin AND r.horaFin >= :horaFin) " +
           "OR (r.horaInicio >= :horaInicio AND r.horaFin <= :horaFin)) " +
           "AND r.estado != 'CANCELADA'")
    List<Reserva> findConflictingReservations(@Param("idLocal") Integer idLocal, 
                                            @Param("fecha") LocalDate fecha,
                                            @Param("horaInicio") LocalTime horaInicio, 
                                            @Param("horaFin") LocalTime horaFin);
    
    @Query("SELECT r FROM Reserva r WHERE r.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Reserva> findByRangoFechas(@Param("fechaInicio") LocalDate fechaInicio, 
                                   @Param("fechaFin") LocalDate fechaFin);
}
