package com.alquileventos.backend.dto.local;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalFiltroDTO {
    private Integer idDistrito;
    private Integer idTipoEvento;
    private Integer aforoMin;
    private BigDecimal precioMin;
    private BigDecimal precioMax;
}