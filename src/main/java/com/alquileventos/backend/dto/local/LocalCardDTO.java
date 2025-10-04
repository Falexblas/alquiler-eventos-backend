package com.alquileventos.backend.dto.local;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalCardDTO {
    private Integer idLocal;
    private String nombreLocal;
    private String distrito;
    private Integer aforoMaximo;
    private BigDecimal precioHora;
    private String fotoPrincipal;
    private List<String> tiposEvento;
}