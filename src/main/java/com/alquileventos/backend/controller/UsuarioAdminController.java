package com.alquileventos.backend.controller;

import com.alquileventos.backend.dto.common.ApiResponseDTO;
import com.alquileventos.backend.dto.usuario.UsuarioPerfilDTO;
import com.alquileventos.backend.entity.Usuario;
import com.alquileventos.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioAdminController {
    
    private final UsuarioService usuarioService;
    
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<Usuario>>> listarTodos(){
        List <Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(ApiResponseDTO.success("Usuarios encontrados", usuarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UsuarioPerfilDTO>> obtenerPorId(
            @PathVariable Integer id) {
        UsuarioPerfilDTO usuario = usuarioService.obtenerPerfil(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Usuario encontrado", usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Usuario>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(ApiResponseDTO.success("Usuario actualizado correctamente", usuarioActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Usuario eliminado correctamente", null));
    }

    @GetMapping("/rol/{idRol}")
    public ResponseEntity<ApiResponseDTO<List<Usuario>>> listarPorRol(@PathVariable Integer idRol) {
        List<Usuario> usuarios = usuarioService.listarPorRol(idRol);
        return ResponseEntity.ok(ApiResponseDTO.success("Usuarios filtrados por rol", usuarios));
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<Usuario>>> buscar(@RequestParam String keyword) {
        List<Usuario> usuarios = usuarioService.buscarPorNombreOApellido(keyword);
        return ResponseEntity.ok(ApiResponseDTO.success("Búsqueda completada", usuarios));
    }
}

