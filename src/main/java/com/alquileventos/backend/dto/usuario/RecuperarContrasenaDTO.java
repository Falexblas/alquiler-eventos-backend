package com.alquileventos.backend.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RecuperarContrasenaDTO {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ingresar un email válido")
    private String email;
}
