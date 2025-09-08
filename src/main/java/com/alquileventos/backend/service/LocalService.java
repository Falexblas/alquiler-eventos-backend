package com.alquileventos.backend.service;

import com.alquileventos.backend.entity.Local;
import com.alquileventos.backend.repository.LocalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LocalService {
    
    private final LocalRepository localRepository;
    
    public List<Local> findAll() {
        return localRepository.findAll();
    }
    
    public Optional<Local> findById(Integer id) {
        return localRepository.findById(id);
    }
    
    public Local save(Local local) {
        return localRepository.save(local);
    }
    
    public Local update(Integer id, Local localActualizado) {
        return localRepository.findById(id)
            .map(local -> {
                local.setNombreLocal(localActualizado.getNombreLocal());
                local.setDireccion(localActualizado.getDireccion());
                local.setAforoMaximo(localActualizado.getAforoMaximo());
                local.setPrecioHora(localActualizado.getPrecioHora());
                local.setDescripcion(localActualizado.getDescripcion());
                local.setEstado(localActualizado.getEstado());
                
                if (localActualizado.getDistrito() != null) {
                    local.setDistrito(localActualizado.getDistrito());
                }
                
                if (localActualizado.getTiposEvento() != null) {
                    local.setTiposEvento(localActualizado.getTiposEvento());
                }
                
                return localRepository.save(local);
            })
            .orElseThrow(() -> new RuntimeException("Local no encontrado con ID: " + id));
    }
    
    public void deleteById(Integer id) {
        if (!localRepository.existsById(id)) {
            throw new RuntimeException("Local no encontrado con ID: " + id);
        }
        localRepository.deleteById(id);
    }
    
    public List<Local> findByDistrito(Integer idDistrito) {
        return localRepository.findByDistrito_IdDistrito(idDistrito);
    }
    
    public List<Local> findDisponibles() {
        return localRepository.findByEstado(Local.EstadoLocal.DISPONIBLE);
    }
    
    public List<Local> findByAforoMinimo(Integer aforo) {
        return localRepository.findByAforoMinimoRequerido(aforo);
    }
    
    public List<Local> findByRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return localRepository.findByRangoPrecio(precioMin, precioMax);
    }
    
    public List<Local> findByTipoEvento(Integer idTipoEvento) {
        return localRepository.findByTipoEvento(idTipoEvento);
    }
    
    public List<Local> searchByNombreODescripcion(String termino) {
        return localRepository.findByNombreOrDescripcionContaining(termino, termino);
    }
    
    public List<Local> findLocalesDisponiblesParaEvento(Integer aforo, Integer idTipoEvento, 
                                                       BigDecimal presupuestoMaximo) {
        return localRepository.findAll().stream()
            .filter(local -> local.getEstado() == Local.EstadoLocal.DISPONIBLE)
            .filter(local -> local.getAforoMaximo() >= aforo)
            .filter(local -> presupuestoMaximo == null || local.getPrecioHora().compareTo(presupuestoMaximo) <= 0)
            .filter(local -> local.getTiposEvento().stream()
                .anyMatch(tipo -> tipo.getIdTipoEvento().equals(idTipoEvento)))
            .toList();
    }
}
