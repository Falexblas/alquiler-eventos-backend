package com.alquileventos.backend.dto.reserva;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

// Paso 4 de confirmacion

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmacionReservaDTO {
    private String nombreLocal;
    private String tipoEvento;
    private String distrito;
    private String direccion;

    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer totalHoras;

    private String nombreCompleto;
    private String email;

    private BigDecimal montoTotal;
    private String metodoPago;
    private String codigoConfirmacion;

    private String urlComprobante;
}
