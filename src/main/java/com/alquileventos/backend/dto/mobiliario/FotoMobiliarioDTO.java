package com.alquileventos.backend.dto.mobiliario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FotoMobiliarioDTO {
        private Integer idFoto;
        private String urlFoto;
        private String descripcion;
}
