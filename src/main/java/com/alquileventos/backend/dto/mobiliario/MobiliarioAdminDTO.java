package com.alquileventos.backend.dto.mobiliario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

// Lista de mobiliarios

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobiliarioAdminDTO {
    private Integer idMobiliario;
    private String nombre;
    private String descripcion;
    private Integer stockTotal;
    private BigDecimal precioUnitario;
}
