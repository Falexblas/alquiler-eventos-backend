package com.alquileventos.backend.dto.reserva;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

// Paso 1 para reserva de local

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearReservaDTO {
    @NotNull(message = "El ID del local es obligatorio")
    private Integer idLocal;

    @NotNull(message = "El tipo de evento es obligatorio")
    private Integer idTipoEvento;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha debe ser hoy o futura")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotNull(message = "La cantidad de personas es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 persona")
    private Integer cantidadPersonas;
}
