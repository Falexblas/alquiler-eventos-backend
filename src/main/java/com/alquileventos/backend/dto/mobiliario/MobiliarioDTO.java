package com.alquileventos.backend.dto.mobiliario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

// Mobiliario disponible paso 2

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobiliarioDTO {
    private Integer idMobiliario;
    private String nombre;
    private String descripcion;
    private BigDecimal precioUnitario;
    private String urlFotoPrincipal;
    private Integer stockDisponible;
}
