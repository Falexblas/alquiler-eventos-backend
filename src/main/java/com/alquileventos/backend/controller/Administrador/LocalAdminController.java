package com.alquileventos.backend.controller.Administrador;

import com.alquileventos.backend.dto.common.ApiResponseDTO;
import com.alquileventos.backend.dto.local.*;
import com.alquileventos.backend.entity.Local;
import com.alquileventos.backend.service.LocalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/locales")
@RequiredArgsConstructor
public class LocalAdminController {

    private final LocalService localService;

    //Listar
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LocalAdminListDTO>>> listarTodos() {
        List<LocalAdminListDTO> locales = localService.listarTodos();
        return ResponseEntity.ok(ApiResponseDTO.success("Locales encontrados", locales));
    }

    //buscar
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<LocalAdminListDTO>>> buscar(
            @RequestParam String nombreLocal) {
        List<LocalAdminListDTO> locales = localService.buscarPorNombre(nombreLocal);
        return ResponseEntity.ok(ApiResponseDTO.success(
                "Búsqueda completada", locales));
    }

    //Crear
    @PostMapping
    public ResponseEntity<ApiResponseDTO<LocalDetalleDTO>> crear(
            @Valid @RequestBody CrearLocalDTO datos) {
        LocalDetalleDTO nuevoLocal = localService.crear(datos);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Local creado exitosamente.", nuevoLocal));
    }

    //Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LocalDetalleDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarLocalDTO datos) {
        LocalDetalleDTO localActualizado = localService.actualizar(id, datos);
        return ResponseEntity.ok(ApiResponseDTO.success("Local actualizado correctamente", localActualizado));
    }

    //cambiar estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponseDTO<LocalDetalleDTO>> cambiarEstado(
            @PathVariable Integer id,
            @RequestBody Local.EstadoLocal estado){
    LocalDetalleDTO localActualizado = localService.cambiarEstado(id, estado);
        return ResponseEntity.ok(ApiResponseDTO.success(
                "Estado actualizado correctamente", localActualizado));
    }

    //Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(
            @PathVariable Integer id) {
        localService.eliminar(id);
        return ResponseEntity.ok(ApiResponseDTO.success(
                "Local eliminado correctamente", null));
    }
}

