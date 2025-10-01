package com.alquileventos.backend.service;

import com.alquileventos.backend.entity.MetodoPago;
import com.alquileventos.backend.entity.Pago;
import com.alquileventos.backend.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoService {
    
    private final PagoRepository pagoRepository;
    
    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }
    
    public Optional<Pago> findById(Integer id) {
        return pagoRepository.findById(id);
    }
    
    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }
    
    public Pago update(Integer id, Pago pagoActualizado) {
        return pagoRepository.findById(id)
            .map(pago -> {
                pago.setMetodoPago(pagoActualizado.getMetodoPago());
                pago.setMonto(pagoActualizado.getMonto());
                pago.setEstado(pagoActualizado.getEstado());
                pago.setComprobanteUrl(pagoActualizado.getComprobanteUrl());
                
                return pagoRepository.save(pago);
            })
            .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }
    
    public void deleteById(Integer id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
        pagoRepository.deleteById(id);
    }
    
    public List<Pago> findByReserva(Integer idReserva) {
        return pagoRepository.findByReserva_IdReserva(idReserva);
    }
    
    public List<Pago> findByEstado(Pago.EstadoPago estado) {
        return pagoRepository.findByEstado(estado);
    }

    public List<Pago> findByMetodoPago(MetodoPago metodoPago) {
        return pagoRepository.findByMetodoPago(metodoPago);
    }
    
    public List<Pago> findByRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pagoRepository.findByRangoFechas(fechaInicio, fechaFin);
    }
    
    public Pago procesarPago(Integer id) {
        return pagoRepository.findById(id)
            .map(pago -> {
                pago.setEstado(Pago.EstadoPago.PAGADO);
                return pagoRepository.save(pago);
            })
            .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }
    
    public Pago marcarComoFallido(Integer id) {
        return pagoRepository.findById(id)
            .map(pago -> {
                pago.setEstado(Pago.EstadoPago.FALLIDO);
                return pagoRepository.save(pago);
            })
            .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }
}
