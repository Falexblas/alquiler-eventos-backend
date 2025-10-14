package com.alquileventos.backend.dto.reserva;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaResumenDTO {
    private Integer idReserva;
    private Integer idLocal;
    private Integer idTipoEvento;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer cantidadPersonas;
}

