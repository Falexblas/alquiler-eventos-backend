package com.alquileventos.backend.dto.pago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Información de pago realizado

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoDetalleDTO {
    private Integer idPago;
    private BigDecimal monto;
    private String metodoPago;
    private String codigoConfirmacion;
    private String estado;
    private LocalDateTime fechaPago;
    private String comprobanteUrl;
}
