package com.alquileventos.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPagoDTO {
    private Integer idMetodoPago;
    private String nombreMetodo;
    private String descripcion;
}
