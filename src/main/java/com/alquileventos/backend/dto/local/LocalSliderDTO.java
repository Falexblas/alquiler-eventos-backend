package com.alquileventos.backend.dto.local;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalSliderDTO {
    private Integer idLocal;
    private String nombreLocal;
    private String fotoPrincipal;
}