package com.alquileventos.backend.controller;

import com.alquileventos.backend.dto.common.ApiResponseDTO;
import com.alquileventos.backend.dto.usuario.ActualizarDatosDTO;
import com.alquileventos.backend.dto.usuario.UsuarioPerfilDTO;
import com.alquileventos.backend.security.CustomUserPrincipal;
import com.alquileventos.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    @PutMapping("/me")
    public ResponseEntity<ApiResponseDTO<UsuarioPerfilDTO>> actualizarMiPerfil(
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
            @Valid @RequestBody ActualizarDatosDTO datos) {
        UsuarioPerfilDTO perfilActualizado = usuarioService.actualizarDatosCliente(
                userPrincipal.getId(),
                datos
        );
        return ResponseEntity.ok(ApiResponseDTO.success("Datos actualizados correctamente", perfilActualizado));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<UsuarioPerfilDTO>> obtenerMiPerfil(
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        UsuarioPerfilDTO perfil = usuarioService.obtenerPerfil(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponseDTO.success("Perfil obtenido correctamente", perfil));
    }
}
