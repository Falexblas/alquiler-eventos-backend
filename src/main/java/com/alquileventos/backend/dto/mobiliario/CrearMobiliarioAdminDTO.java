package com.alquileventos.backend.dto.mobiliario;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public class CrearMobiliarioAdminDTO {
    @NotBlank
    @Size(max = 100)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    @NotNull
    @Min(1)
    private Integer stockTotal;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal precioUnitario;

    private List<String> urlsFotos;
}
