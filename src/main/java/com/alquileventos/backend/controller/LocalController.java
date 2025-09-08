package com.alquileventos.backend.controller;

import com.alquileventos.backend.entity.Local;
import com.alquileventos.backend.service.LocalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/locales")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LocalController {
    
    private final LocalService localService;
    
    @GetMapping
    public ResponseEntity<List<Local>> getAllLocales() {
        List<Local> locales = localService.findAll();
        return ResponseEntity.ok(locales);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Local> getLocalById(@PathVariable Integer id) {
        return localService.findById(id)
            .map(local -> ResponseEntity.ok(local))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Local> createLocal(@Valid @RequestBody Local local) {
        Local nuevoLocal = localService.save(local);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLocal);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Local> updateLocal(@PathVariable Integer id, @Valid @RequestBody Local local) {
        try {
            Local localActualizado = localService.update(id, local);
            return ResponseEntity.ok(localActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocal(@PathVariable Integer id) {
        try {
            localService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/disponibles")
    public ResponseEntity<List<Local>> getLocalesDisponibles() {
        List<Local> locales = localService.findDisponibles();
        return ResponseEntity.ok(locales);
    }
    
    @GetMapping("/distrito/{idDistrito}")
    public ResponseEntity<List<Local>> getLocalesByDistrito(@PathVariable Integer idDistrito) {
        List<Local> locales = localService.findByDistrito(idDistrito);
        return ResponseEntity.ok(locales);
    }
    
    @GetMapping("/aforo/{aforo}")
    public ResponseEntity<List<Local>> getLocalesByAforoMinimo(@PathVariable Integer aforo) {
        List<Local> locales = localService.findByAforoMinimo(aforo);
        return ResponseEntity.ok(locales);
    }
    
    @GetMapping("/precio")
    public ResponseEntity<List<Local>> getLocalesByRangoPrecio(
            @RequestParam BigDecimal min, 
            @RequestParam BigDecimal max) {
        List<Local> locales = localService.findByRangoPrecio(min, max);
        return ResponseEntity.ok(locales);
    }
    
    @GetMapping("/tipo-evento/{idTipoEvento}")
    public ResponseEntity<List<Local>> getLocalesByTipoEvento(@PathVariable Integer idTipoEvento) {
        List<Local> locales = localService.findByTipoEvento(idTipoEvento);
        return ResponseEntity.ok(locales);
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Local>> buscarLocales(@RequestParam String termino) {
        List<Local> locales = localService.searchByNombreODescripcion(termino);
        return ResponseEntity.ok(locales);
    }
    
    @GetMapping("/filtrar")
    public ResponseEntity<List<Local>> filtrarLocalesParaEvento(
            @RequestParam Integer aforo,
            @RequestParam Integer idTipoEvento,
            @RequestParam(required = false) BigDecimal presupuestoMaximo) {
        List<Local> locales = localService.findLocalesDisponiblesParaEvento(aforo, idTipoEvento, presupuestoMaximo);
        return ResponseEntity.ok(locales);
    }
}
