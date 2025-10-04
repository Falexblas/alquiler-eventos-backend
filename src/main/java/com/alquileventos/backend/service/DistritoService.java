package com.alquileventos.backend.service;

import com.alquileventos.backend.entity.Distrito;
import com.alquileventos.backend.repository.DistritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DistritoService {
    
    private final DistritoRepository distritoRepository;
    
    public List<Distrito> listarTodos() {
        return distritoRepository.findAll();
    }
    
    public Optional<Distrito> findById(Integer id) {
        return distritoRepository.findById(id);
    }
    
    public Distrito save(Distrito distrito) {
        if (existsByNombre(distrito.getNombreDistrito())) {
            throw new RuntimeException("Ya existe un distrito con el nombre: " + distrito.getNombreDistrito());
        }
        return distritoRepository.save(distrito);
    }
    
    public Distrito update(Integer id, Distrito distritoActualizado) {
        return distritoRepository.findById(id)
            .map(distrito -> {
                distrito.setNombreDistrito(distritoActualizado.getNombreDistrito());
                return distritoRepository.save(distrito);
            })
            .orElseThrow(() -> new RuntimeException("Distrito no encontrado con ID: " + id));
    }
    
    public void deleteById(Integer id) {
        if (!distritoRepository.existsById(id)) {
            throw new RuntimeException("Distrito no encontrado con ID: " + id);
        }
        distritoRepository.deleteById(id);
    }
    
    public boolean existsByNombre(String nombreDistrito) {
        return distritoRepository.existsByNombreDistrito(nombreDistrito);
    }
    
    public Optional<Distrito> findByNombre(String nombreDistrito) {
        return distritoRepository.findByNombreDistrito(nombreDistrito);
    }
}
