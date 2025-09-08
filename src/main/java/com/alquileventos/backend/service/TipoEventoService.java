package com.alquileventos.backend.service;

import com.alquileventos.backend.entity.TipoEvento;
import com.alquileventos.backend.repository.TipoEventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoEventoService {
    
    private final TipoEventoRepository tipoEventoRepository;
    
    public List<TipoEvento> findAll() {
        return tipoEventoRepository.findAll();
    }
    
    public Optional<TipoEvento> findById(Integer id) {
        return tipoEventoRepository.findById(id);
    }
    
    public TipoEvento save(TipoEvento tipoEvento) {
        if (existsByNombre(tipoEvento.getNombreTipo())) {
            throw new RuntimeException("Ya existe un tipo de evento con el nombre: " + tipoEvento.getNombreTipo());
        }
        return tipoEventoRepository.save(tipoEvento);
    }
    
    public TipoEvento update(Integer id, TipoEvento tipoEventoActualizado) {
        return tipoEventoRepository.findById(id)
            .map(tipoEvento -> {
                tipoEvento.setNombreTipo(tipoEventoActualizado.getNombreTipo());
                tipoEvento.setDescripcion(tipoEventoActualizado.getDescripcion());
                return tipoEventoRepository.save(tipoEvento);
            })
            .orElseThrow(() -> new RuntimeException("Tipo de evento no encontrado con ID: " + id));
    }
    
    public void deleteById(Integer id) {
        if (!tipoEventoRepository.existsById(id)) {
            throw new RuntimeException("Tipo de evento no encontrado con ID: " + id);
        }
        tipoEventoRepository.deleteById(id);
    }
    
    public boolean existsByNombre(String nombreTipo) {
        return tipoEventoRepository.existsByNombreTipo(nombreTipo);
    }
    
    public Optional<TipoEvento> findByNombre(String nombreTipo) {
        return tipoEventoRepository.findByNombreTipo(nombreTipo);
    }
}
