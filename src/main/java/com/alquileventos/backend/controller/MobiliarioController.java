package com.alquileventos.backend.controller;

import com.alquileventos.backend.entity.Mobiliario;
import com.alquileventos.backend.service.MobiliarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mobiliario")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MobiliarioController {
    
    private final MobiliarioService mobiliarioService;
    
    @GetMapping
    public ResponseEntity<List<Mobiliario>> getAllMobiliario() {
        List<Mobiliario> mobiliario = mobiliarioService.findAll();
        return ResponseEntity.ok(mobiliario);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Mobiliario> getMobiliarioById(@PathVariable Integer id) {
        return mobiliarioService.findById(id)
            .map(mobiliario -> ResponseEntity.ok(mobiliario))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Mobiliario> createMobiliario(@Valid @RequestBody Mobiliario mobiliario) {
        Mobiliario nuevoMobiliario = mobiliarioService.save(mobiliario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMobiliario);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Mobiliario> updateMobiliario(@PathVariable Integer id, @Valid @RequestBody Mobiliario mobiliario) {
        try {
            Mobiliario mobiliarioActualizado = mobiliarioService.update(id, mobiliario);
            return ResponseEntity.ok(mobiliarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMobiliario(@PathVariable Integer id) {
        try {
            mobiliarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/disponibles")
    public ResponseEntity<List<Mobiliario>> getMobiliarioDisponible() {
        List<Mobiliario> mobiliario = mobiliarioService.findDisponibles();
        return ResponseEntity.ok(mobiliario);
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Mobiliario>> buscarMobiliario(@RequestParam String nombre) {
        List<Mobiliario> mobiliario = mobiliarioService.searchByNombre(nombre);
        return ResponseEntity.ok(mobiliario);
    }
    
    @GetMapping("/cantidad/{cantidad}")
    public ResponseEntity<List<Mobiliario>> getMobiliarioDisponiblePorCantidad(@PathVariable Integer cantidad) {
        List<Mobiliario> mobiliario = mobiliarioService.findDisponiblesPorCantidad(cantidad);
        return ResponseEntity.ok(mobiliario);
    }
}
