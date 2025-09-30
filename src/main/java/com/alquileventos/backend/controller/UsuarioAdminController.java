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
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioAdminController {
    
    private final UsuarioService usuarioService;
    
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsers(){
        List <Usuario> users = usuarioService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUserById(
            @PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> createUser(
            @Valid @RequestBody Usuario user) {
        try {
            Usuario newUser = usuarioService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody Usuario user) {
        try {
            Usuario updatedUser = usuarioService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Integer id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> getUsersByEmail(@PathVariable String email) {
        return usuarioService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rol/{idRol}")
    public ResponseEntity<List<Usuario>> getUsersByRol(@PathVariable Integer idRol) {
        List<Usuario> users = usuarioService.findByRol(idRol);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> searchUser(@RequestParam String keyword) {
        List<Usuario> users = usuarioService.findByNameOrLastname(keyword);
        return ResponseEntity.ok(users);
    }
}

