package com.alquileventos.backend.controller;

import com.alquileventos.backend.dto.auth.AuthResponseDTO;
import com.alquileventos.backend.dto.auth.LoginRequest;
import com.alquileventos.backend.dto.auth.RegisterRequest;
import com.alquileventos.backend.dto.common.ApiResponseDTO;
import com.alquileventos.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // inciar sesion
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        AuthResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponseDTO.success("Inicio de sesión exitoso", response));
    }

    // registrar
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> register(
            @Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponseDTO response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Registro exitoso.", response));
    }
}
