package com.alquileventos.backend.controller;

import com.alquileventos.backend.entity.Distrito;
import com.alquileventos.backend.service.DistritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/distritos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DistritoController {
    
    private final DistritoService distritoService;
    
    @GetMapping
    public ResponseEntity<List<Distrito>> getAllDistritos() {
        List<Distrito> distritos = distritoService.findAll();
        return ResponseEntity.ok(distritos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Distrito> getDistritoById(@PathVariable Integer id) {
        return distritoService.findById(id)
            .map(distrito -> ResponseEntity.ok(distrito))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Distrito> createDistrito(@Valid @RequestBody Distrito distrito) {
        try {
            Distrito nuevoDistrito = distritoService.save(distrito);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDistrito);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Distrito> updateDistrito(@PathVariable Integer id, @Valid @RequestBody Distrito distrito) {
        try {
            Distrito distritoActualizado = distritoService.update(id, distrito);
            return ResponseEntity.ok(distritoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistrito(@PathVariable Integer id) {
        try {
            distritoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Distrito> getDistritoByNombre(@PathVariable String nombre) {
        return distritoService.findByNombre(nombre)
            .map(distrito -> ResponseEntity.ok(distrito))
            .orElse(ResponseEntity.notFound().build());
    }
}
