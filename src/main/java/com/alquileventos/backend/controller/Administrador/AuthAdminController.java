package com.alquileventos.backend.controller.Administrador;

import com.alquileventos.backend.dto.auth.AdminLoginRequestDTO;
import com.alquileventos.backend.dto.auth.AuthResponseDTO;
import com.alquileventos.backend.dto.common.ApiResponseDTO;
import com.alquileventos.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AuthAdminController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> adminLogin(
            @Valid @RequestBody AdminLoginRequestDTO request) {
        AuthResponseDTO response = authService.adminLogin(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Acceso administrativo permitido", response));
    }
}
