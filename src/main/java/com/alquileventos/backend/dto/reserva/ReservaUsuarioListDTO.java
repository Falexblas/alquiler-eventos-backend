package com.alquileventos.backend.dto.reserva;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

//Mis reservas

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaUsuarioListDTO {
    private Integer idReserva;
    private String nombreLocal;
    private String distrito;
    private LocalDate fecha;
    private String tipoEvento;
    private BigDecimal costoTotal;
    private String estado;
    private LocalDateTime fechaReserva;
}

