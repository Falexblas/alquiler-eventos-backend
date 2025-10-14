package com.alquileventos.backend.dto.reserva;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisponibilidadDTO {
    private boolean disponible;
    private String mensaje;
}