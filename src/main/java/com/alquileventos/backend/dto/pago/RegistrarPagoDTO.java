package com.alquileventos.backend.dto.pago;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

// Registro de pago Paso 3

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarPagoDTO {
    @NotNull(message = "El ID de la reserva es obligatorio")
    private Integer idReserva;

    @NotNull(message = "El método de pago es obligatorio")
    private Integer idMetodoPago;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    private String codigoTransaccion;
}
