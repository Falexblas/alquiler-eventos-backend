package com.alquileventos.backend.dto.mobiliario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Mobiliario en reserva

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobiliarioItemDTO {
    private Integer idMobiliario;
    private String nombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
