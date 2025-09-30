package com.alquileventos.backend.controller;

import com.alquileventos.backend.entity.Usuario;
import com.alquileventos.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    /**
     * Cliente
     */

    @PutMapping("/profile/{id}")
    public ResponseEntity<Usuario> updateProfile(
            @PathVariable Integer id,
            @Valid @RequestBody Usuario usuario) {
        try{
            Usuario updatedUser = usuarioService.updateProfile(id, usuario);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUserProfile(
            @PathVariable Integer id){
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
