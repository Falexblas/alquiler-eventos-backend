package com.alquileventos.backend.service;

import com.alquileventos.backend.entity.Reserva;
import com.alquileventos.backend.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {
    
    private final ReservaRepository reservaRepository;
    
    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }
    
    public Optional<Reserva> findById(Integer id) {
        return reservaRepository.findById(id);
    }
    
    public Reserva save(Reserva reserva) {

        if (!isLocalDisponible(reserva.getLocal().getIdLocal(), reserva.getFecha(), 
                              reserva.getHoraInicio(), reserva.getHoraFin())) {
            throw new RuntimeException("El local no está disponible en el horario solicitado");
        }

        reserva.setCostoTotal(calcularCostoTotal(reserva));
        
        return reservaRepository.save(reserva);
    }
    
    public Reserva update(Integer id, Reserva reservaActualizada) {
        return reservaRepository.findById(id)
            .map(reserva -> {
                // Validar disponibilidad si se cambia fecha/hora/local
                if (!reserva.getLocal().getIdLocal().equals(reservaActualizada.getLocal().getIdLocal()) ||
                    !reserva.getFecha().equals(reservaActualizada.getFecha()) ||
                    !reserva.getHoraInicio().equals(reservaActualizada.getHoraInicio()) ||
                    !reserva.getHoraFin().equals(reservaActualizada.getHoraFin())) {
                    
                    if (!isLocalDisponible(reservaActualizada.getLocal().getIdLocal(), 
                                         reservaActualizada.getFecha(),
                                         reservaActualizada.getHoraInicio(), 
                                         reservaActualizada.getHoraFin(), id)) {
                        throw new RuntimeException("El local no está disponible en el nuevo horario solicitado");
                    }
                }
                
                reserva.setFecha(reservaActualizada.getFecha());
                reserva.setHoraInicio(reservaActualizada.getHoraInicio());
                reserva.setHoraFin(reservaActualizada.getHoraFin());
                reserva.setCantidadPersonas(reservaActualizada.getCantidadPersonas());
                reserva.setEstado(reservaActualizada.getEstado());
                
                if (reservaActualizada.getLocal() != null) {
                    reserva.setLocal(reservaActualizada.getLocal());
                }
                
                if (reservaActualizada.getTipoEvento() != null) {
                    reserva.setTipoEvento(reservaActualizada.getTipoEvento());
                }
                
                // Recalcular costo total
                reserva.setCostoTotal(calcularCostoTotal(reserva));
                
                return reservaRepository.save(reserva);
            })
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }
    
    public void deleteById(Integer id) {
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva no encontrada con ID: " + id);
        }
        reservaRepository.deleteById(id);
    }
    
    public List<Reserva> findByUsuario(Integer idUsuario) {
        return reservaRepository.findByUsuario_IdUsuario(idUsuario);
    }
    
    public List<Reserva> findByLocal(Integer idLocal) {
        return reservaRepository.findByLocal_IdLocal(idLocal);
    }
    
    public List<Reserva> findByEstado(Reserva.EstadoReserva estado) {
        return reservaRepository.findByEstado(estado);
    }
    
    public List<Reserva> findByFecha(LocalDate fecha) {
        return reservaRepository.findByFecha(fecha);
    }
    
    public List<Reserva> findByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return reservaRepository.findByRangoFechas(fechaInicio, fechaFin);
    }
    
    public boolean isLocalDisponible(Integer idLocal, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        return isLocalDisponible(idLocal, fecha, horaInicio, horaFin, null);
    }
    
    public boolean isLocalDisponible(Integer idLocal, LocalDate fecha, LocalTime horaInicio, 
                                   LocalTime horaFin, Integer excludeReservaId) {
        List<Reserva> conflictos = reservaRepository.findConflictingReservations(idLocal, fecha, horaInicio, horaFin);
        
        // Excluir la reserva actual si se está actualizando
        if (excludeReservaId != null) {
            conflictos = conflictos.stream()
                .filter(r -> !r.getIdReserva().equals(excludeReservaId))
                .toList();
        }
        
        return conflictos.isEmpty();
    }
    
    public Reserva confirmarReserva(Integer id) {
        return reservaRepository.findById(id)
            .map(reserva -> {
                reserva.setEstado(Reserva.EstadoReserva.CONFIRMADA);
                return reservaRepository.save(reserva);
            })
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }
    
    public Reserva cancelarReserva(Integer id) {
        return reservaRepository.findById(id)
            .map(reserva -> {
                reserva.setEstado(Reserva.EstadoReserva.CANCELADA);
                return reservaRepository.save(reserva);
            })
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }
    
    private BigDecimal calcularCostoTotal(Reserva reserva) {
        // Calcular horas de duración
        Duration duracion = Duration.between(reserva.getHoraInicio(), reserva.getHoraFin());
        long horas = duracion.toHours();
        
        // Costo base del local
        BigDecimal costoLocal = reserva.getLocal().getPrecioHora().multiply(BigDecimal.valueOf(horas));
        
        // Agregar costo del mobiliario si existe
        BigDecimal costoMobiliario = BigDecimal.ZERO;
        if (reserva.getMobiliario() != null) {
            costoMobiliario = reserva.getMobiliario().stream()
                .map(rm -> rm.getPrecioUnitario().multiply(BigDecimal.valueOf(rm.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        
        return costoLocal.add(costoMobiliario);
    }
}
