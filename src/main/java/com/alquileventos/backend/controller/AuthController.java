package com.alquileventos.backend.controller;

import com.alquileventos.backend.dto.LoginRequest;
import com.alquileventos.backend.dto.LoginResponse;
import com.alquileventos.backend.dto.RegisterRequest;
import com.alquileventos.backend.entity.Rol;
import com.alquileventos.backend.entity.Usuario;
import com.alquileventos.backend.repository.RolRepository;
import com.alquileventos.backend.security.JwtTokenProvider;
import com.alquileventos.backend.service.AuthService;
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

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok("Usuario registrado exitosamente!");
    }
}
