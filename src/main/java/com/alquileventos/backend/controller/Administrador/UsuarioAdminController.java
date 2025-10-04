package com.alquileventos.backend.controller.Administrador;

import com.alquileventos.backend.dto.common.ApiResponseDTO;
import com.alquileventos.backend.dto.usuario.ActualizarClienteAdminDTO;
import com.alquileventos.backend.dto.usuario.ClienteListDTO;
import com.alquileventos.backend.dto.usuario.UsuarioPerfilDTO;
import com.alquileventos.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/clientes")
@RequiredArgsConstructor
public class UsuarioAdminController {
    
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ClienteListDTO>>> listarClientes() {
        List<ClienteListDTO> clientes = usuarioService.listarClientes();
        return ResponseEntity.ok(ApiResponseDTO.success("Clientes encontrados", clientes));
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<ClienteListDTO>>> buscar(@RequestParam String keyword) {
        List<ClienteListDTO> clientes = usuarioService.buscarClientes(keyword);
        return ResponseEntity.ok(ApiResponseDTO.success("Búsqueda completada", clientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UsuarioPerfilDTO>> obtenerPorId(@PathVariable Integer id) {
        UsuarioPerfilDTO cliente = usuarioService.obtenerClientePorId(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Cliente encontrado", cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UsuarioPerfilDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarClienteAdminDTO datos) {
        UsuarioPerfilDTO clienteActualizado = usuarioService.actualizarCliente(id, datos);
        return ResponseEntity.ok(ApiResponseDTO.success("Cliente actualizado correctamente", clienteActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(@PathVariable Integer id) {
        usuarioService.eliminarCliente(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Cliente eliminado correctamente", null));
    }
}

