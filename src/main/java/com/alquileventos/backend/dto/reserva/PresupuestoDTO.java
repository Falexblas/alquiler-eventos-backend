package com.alquileventos.backend.dto.reserva;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

// Presupuesto en detalle de local

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresupuestoDTO {
    private BigDecimal precioHora;
    private Integer totalHoras;
    private BigDecimal costoLocal;
    private BigDecimal costoMobiliario;
    private BigDecimal costoTotal;
}
