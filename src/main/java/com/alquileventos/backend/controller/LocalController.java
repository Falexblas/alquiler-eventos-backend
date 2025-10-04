package com.alquileventos.backend.controller;

import com.alquileventos.backend.dto.common.ApiResponseDTO;
import com.alquileventos.backend.dto.local.LocalCardDTO;
import com.alquileventos.backend.dto.local.LocalDetalleDTO;
import com.alquileventos.backend.dto.local.LocalFiltroDTO;
import com.alquileventos.backend.service.LocalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/locales")
@RequiredArgsConstructor
public class LocalController {
    
    private final LocalService localService;

    //Catálogo
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LocalCardDTO>>>listarDisponibles(){
        List<LocalCardDTO> locales = localService.listarLocalesDisponibles();
        return ResponseEntity.ok(ApiResponseDTO.success("Locales encontrados", locales));
    }

    // filtro flexible
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<LocalCardDTO>>> buscarFiltroInicio(
            @RequestParam(required = false) Integer distrito,
            @RequestParam(required = false) Integer tipoEvento,
            @RequestParam(required = false) Integer aforoMin,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax
    ) {
        LocalFiltroDTO filtros = new LocalFiltroDTO(distrito, tipoEvento, aforoMin, precioMin, precioMax);
        List<LocalCardDTO> locales = localService.buscarConFiltros(filtros);
        return ResponseEntity.ok(ApiResponseDTO.success("Búsqueda completada", locales));
    }

    // Detalle de local
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LocalDetalleDTO>> obtenerDetalle(
            @PathVariable Integer id) {
        LocalDetalleDTO localDetalle = localService.obtenerDetalle(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Detalle encontrado", localDetalle));
    }
}
