package com.alquileventos.backend.controller;

import com.alquileventos.backend.entity.Reserva;
import com.alquileventos.backend.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {
    
    private final ReservaService reservaService;


    
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Integer id) {
        return reservaService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Reserva> createReserva(@Valid @RequestBody Reserva reserva) {
        try {
            Reserva nuevaReserva = reservaService.save(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Integer id, @Valid @RequestBody Reserva reserva) {
        try {
            Reserva reservaActualizada = reservaService.update(id, reserva);
            return ResponseEntity.ok(reservaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Integer id) {
        try {
            reservaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Reserva>> getReservasByUsuario(@PathVariable Integer idUsuario) {
        List<Reserva> reservas = reservaService.findByUsuario(idUsuario);
        return ResponseEntity.ok(reservas);
    }
    
    @GetMapping("/local/{idLocal}")
    public ResponseEntity<List<Reserva>> getReservasByLocal(@PathVariable Integer idLocal) {
        List<Reserva> reservas = reservaService.findByLocal(idLocal);
        return ResponseEntity.ok(reservas);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reserva>> getReservasByEstado(@PathVariable Reserva.EstadoReserva estado) {
        List<Reserva> reservas = reservaService.findByEstado(estado);
        return ResponseEntity.ok(reservas);
    }
    
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Reserva>> getReservasByFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Reserva> reservas = reservaService.findByFecha(fecha);
        return ResponseEntity.ok(reservas);
    }
    
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<Reserva>> getReservasByRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Reserva> reservas = reservaService.findByRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(reservas);
    }
    
    @GetMapping("/disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(
            @RequestParam Integer idLocal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFin) {
        boolean disponible = reservaService.isLocalDisponible(idLocal, fecha, horaInicio, horaFin);
        return ResponseEntity.ok(disponible);
    }
    
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Reserva> confirmarReserva(@PathVariable Integer id) {
        try {
            Reserva reservaConfirmada = reservaService.confirmarReserva(id);
            return ResponseEntity.ok(reservaConfirmada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelarReserva(@PathVariable Integer id) {
        try {
            Reserva reservaCancelada = reservaService.cancelarReserva(id);
            return ResponseEntity.ok(reservaCancelada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
