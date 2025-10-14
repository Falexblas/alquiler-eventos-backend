package com.alquileventos.backend.dto.local;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ActualizarLocalAdminDTO {

    @NotBlank(message = "El nombre del local es obligatorio")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String nombreLocal;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "Máximo 255 caracteres")
    private String direccion;

    @NotNull(message = "El distrito es obligatorio")
    private Integer idDistrito;

    @NotNull(message = "El aforo máximo es obligatorio")
    @Min(value = 1, message = "El aforo debe ser al menos 1")
    private Integer aforoMaximo;

    @NotNull(message = "El precio por hora es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precioHora;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;

    @NotEmpty(message = "Debe seleccionar al menos un tipo de evento")
    private List<Integer> idsTiposEvento;

    private List<String> urlsFotos;

    private String estado;
}