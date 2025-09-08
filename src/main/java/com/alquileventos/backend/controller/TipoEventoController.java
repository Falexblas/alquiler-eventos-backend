package com.alquileventos.backend.controller;

import com.alquileventos.backend.entity.TipoEvento;
import com.alquileventos.backend.service.TipoEventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tipos-evento")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TipoEventoController {
    
    private final TipoEventoService tipoEventoService;
    
    @GetMapping
    public ResponseEntity<List<TipoEvento>> getAllTiposEvento() {
        List<TipoEvento> tiposEvento = tipoEventoService.findAll();
        return ResponseEntity.ok(tiposEvento);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TipoEvento> getTipoEventoById(@PathVariable Integer id) {
        return tipoEventoService.findById(id)
            .map(tipoEvento -> ResponseEntity.ok(tipoEvento))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<TipoEvento> createTipoEvento(@Valid @RequestBody TipoEvento tipoEvento) {
        try {
            TipoEvento nuevoTipoEvento = tipoEventoService.save(tipoEvento);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipoEvento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TipoEvento> updateTipoEvento(@PathVariable Integer id, @Valid @RequestBody TipoEvento tipoEvento) {
        try {
            TipoEvento tipoEventoActualizado = tipoEventoService.update(id, tipoEvento);
            return ResponseEntity.ok(tipoEventoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoEvento(@PathVariable Integer id) {
        try {
            tipoEventoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoEvento> getTipoEventoByNombre(@PathVariable String nombre) {
        return tipoEventoService.findByNombre(nombre)
            .map(tipoEvento -> ResponseEntity.ok(tipoEvento))
            .orElse(ResponseEntity.notFound().build());
    }
}
