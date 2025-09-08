package com.alquileventos.backend.service;

import com.alquileventos.backend.entity.Mobiliario;
import com.alquileventos.backend.repository.MobiliarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MobiliarioService {
    
    private final MobiliarioRepository mobiliarioRepository;
    
    public List<Mobiliario> findAll() {
        return mobiliarioRepository.findAll();
    }
    
    public Optional<Mobiliario> findById(Integer id) {
        return mobiliarioRepository.findById(id);
    }
    
    public Mobiliario save(Mobiliario mobiliario) {
        return mobiliarioRepository.save(mobiliario);
    }
    
    public Mobiliario update(Integer id, Mobiliario mobiliarioActualizado) {
        return mobiliarioRepository.findById(id)
            .map(mobiliario -> {
                mobiliario.setNombre(mobiliarioActualizado.getNombre());
                mobiliario.setDescripcion(mobiliarioActualizado.getDescripcion());
                mobiliario.setStockTotal(mobiliarioActualizado.getStockTotal());
                mobiliario.setPrecioUnitario(mobiliarioActualizado.getPrecioUnitario());
                
                return mobiliarioRepository.save(mobiliario);
            })
            .orElseThrow(() -> new RuntimeException("Mobiliario no encontrado con ID: " + id));
    }
    
    public void deleteById(Integer id) {
        if (!mobiliarioRepository.existsById(id)) {
            throw new RuntimeException("Mobiliario no encontrado con ID: " + id);
        }
        mobiliarioRepository.deleteById(id);
    }
    
    public List<Mobiliario> findDisponibles() {
        return mobiliarioRepository.findByStockTotalGreaterThan(0);
    }
    
    public List<Mobiliario> searchByNombre(String nombre) {
        return mobiliarioRepository.findByNombreContaining(nombre);
    }
    
    public List<Mobiliario> findDisponiblesPorCantidad(Integer cantidadRequerida) {
        return mobiliarioRepository.findDisponiblesPorCantidad(cantidadRequerida);
    }
}
