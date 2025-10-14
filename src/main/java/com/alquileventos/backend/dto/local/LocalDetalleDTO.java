package com.alquileventos.backend.dto.local;

import com.alquileventos.backend.dto.common.TipoEventoSimpleDTO;
import com.alquileventos.backend.entity.Local;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalDetalleDTO {
    private Integer idLocal;
    private String nombreLocal;
    private String direccion;
    private String distrito;
    private Integer idDistrito;
    private Integer aforoMaximo;
    private BigDecimal precioHora;
    private String descripcion;
    private Local.EstadoLocal estado;
    private List<FotoLocalDTO> fotos;
    private List<TipoEventoSimpleDTO> tiposEvento;
}

