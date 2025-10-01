package com.alquileventos.backend.controller;

import com.alquileventos.backend.entity.MetodoPago;
import com.alquileventos.backend.entity.Pago;
import com.alquileventos.backend.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PagoController {
    
    private final PagoService pagoService;
    
    @GetMapping
    public ResponseEntity<List<Pago>> getAllPagos() {
        List<Pago> pagos = pagoService.findAll();
        return ResponseEntity.ok(pagos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pago> getPagoById(@PathVariable Integer id) {
        return pagoService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Pago> createPago(@Valid @RequestBody Pago pago) {
        Pago nuevoPago = pagoService.save(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Pago> updatePago(@PathVariable Integer id, @Valid @RequestBody Pago pago) {
        try {
            Pago pagoActualizado = pagoService.update(id, pago);
            return ResponseEntity.ok(pagoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable Integer id) {
        try {
            pagoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<Pago>> getPagosByReserva(@PathVariable Integer idReserva) {
        List<Pago> pagos = pagoService.findByReserva(idReserva);
        return ResponseEntity.ok(pagos);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> getPagosByEstado(@PathVariable Pago.EstadoPago estado) {
        List<Pago> pagos = pagoService.findByEstado(estado);
        return ResponseEntity.ok(pagos);
    }
    
    @GetMapping("/metodo/{metodoPago}")
    public ResponseEntity<List<Pago>> getPagosByMetodo(@PathVariable MetodoPago metodoPago) {
        List<Pago> pagos = pagoService.findByMetodoPago(metodoPago);
        return ResponseEntity.ok(pagos);
    }
    
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<Pago>> getPagosByRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Pago> pagos = pagoService.findByRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(pagos);
    }
    
    @PutMapping("/{id}/procesar")
    public ResponseEntity<Pago> procesarPago(@PathVariable Integer id) {
        try {
            Pago pagoProcesado = pagoService.procesarPago(id);
            return ResponseEntity.ok(pagoProcesado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/fallar")
    public ResponseEntity<Pago> marcarComoFallido(@PathVariable Integer id) {
        try {
            Pago pagoFallido = pagoService.marcarComoFallido(id);
            return ResponseEntity.ok(pagoFallido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
