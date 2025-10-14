package com.alquileventos.backend.dto.local;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistritoCardDTO {
    private Integer idDistrito;
    private String nombreDistrito;
    private String fotoLocal;
    private Integer cantidadLocales;
}

