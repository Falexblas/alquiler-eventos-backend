package com.alquileventos.backend.dto.reserva;

import com.alquileventos.backend.dto.mobiliario.MobiliarioDetalleDTO;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CrearReservaDTO {
    @NotNull
    private Integer idLocal;

    @NotNull
    private Integer idTipoEvento;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime horaInicio;

    @NotNull
    private LocalTime horaFin;

    @NotNull
    @Min(1)
    private Integer cantidadPersonas;

    private List<MobiliarioDetalleDTO> mobiliarioSeleccionado;
}
