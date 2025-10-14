package com.alquileventos.backend.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CambiarContrasenaDTO {

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String contrasenaActual;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String nuevaContrasena;

    @NotBlank(message = "Confirmar contraseña es obligatorio")
    private String confirmarContrasena;
}
