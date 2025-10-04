package com.alquileventos.backend.dto.local;

import com.alquileventos.backend.entity.Local;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalAdminListDTO {
    private Integer idLocal;
    private String nombreLocal;
    private String distrito;
    private Integer aforoMaximo;
    private BigDecimal precioHora;
    private Local.EstadoLocal estado;
}