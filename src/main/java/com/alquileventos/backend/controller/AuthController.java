package com.alquileventos.backend.controller;

import com.alquileventos.backend.dto.LoginRequest;
import com.alquileventos.backend.dto.LoginResponse;
import com.alquileventos.backend.dto.RegisterRequest;
import com.alquileventos.backend.entity.Rol;
import com.alquileventos.backend.entity.Usuario;
import com.alquileventos.backend.repository.RolRepository;
import com.alquileventos.backend.security.JwtTokenProvider;
import com.alquileventos.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;
    private final JwtTokenProvider tokenProvider;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getContrasena()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        return ResponseEntity.ok(new LoginResponse(jwt, "Bearer"));
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (usuarioService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: El email ya está en uso!");
        }
        
        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(registerRequest.getNombre());
        usuario.setApellido(registerRequest.getApellido());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setTelefono(registerRequest.getTelefono());
        usuario.setContrasena(registerRequest.getContrasena());
        
        // Asignar rol por defecto (CLIENTE)
        Rol rolCliente = rolRepository.findByNombreRol("CLIENTE")
            .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
        usuario.setRol(rolCliente);
        
        usuarioService.save(usuario);
        
        return ResponseEntity.ok("Usuario registrado exitosamente!");
    }
}
