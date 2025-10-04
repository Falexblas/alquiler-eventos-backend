package com.alquileventos.backend.dto.local;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FotoLocalDTO {
    private Integer idFoto;
    private String urlFoto;
    private String descripcion;
}
