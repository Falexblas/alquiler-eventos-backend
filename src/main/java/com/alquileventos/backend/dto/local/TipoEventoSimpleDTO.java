package com.alquileventos.backend.dto.local;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoEventoSimpleDTO {
    private Integer idTipoEvento;
    private String nombreTipo;
}
