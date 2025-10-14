package com.alquileventos.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoEventoDTO {
    private Integer idTipoEvento;
    private String nombreTipo;
    private String descripcionTipo;
}
