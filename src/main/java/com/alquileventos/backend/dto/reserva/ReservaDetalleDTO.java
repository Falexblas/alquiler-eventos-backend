package com.alquileventos.backend.dto.reserva;

import com.alquileventos.backend.dto.mobiliario.MobiliarioResumenDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservaDetalleDTO {
    private String nombreLocal;
    private String tipoEvento;
    private String distrito;
    private String direccion;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer horasTotales;
    private BigDecimal costoPorHora;
    private BigDecimal subtotalLocal;
    private List<MobiliarioResumenDTO> mobiliarios;
    private BigDecimal totalEstimado;
}
