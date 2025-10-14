package com.alquileventos.backend.controller.Administrador;

import com.alquileventos.backend.dto.common.ApiResponseDTO;
import com.alquileventos.backend.dto.usuario.CrearAdminDTO;
import com.alquileventos.backend.dto.usuario.UsuarioPerfilDTO;
import com.alquileventos.backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/administradores")
@RequiredArgsConstructor
public class UsuarioAdminController {

    private final UsuarioService usuarioService;

    //Crear perfil ADMIN
    @PostMapping
    public ResponseEntity<ApiResponseDTO<UsuarioPerfilDTO>> crearAdmin(
            @Valid @RequestBody CrearAdminDTO datos) {
        UsuarioPerfilDTO nuevoAdmin = usuarioService.crearAdministrador(datos);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Administrador creado exitosamente", nuevoAdmin));
    }
}
