package com.alquileventos.backend.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ActualizarClienteAdminDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String email;

    @NotBlank(message = "El celular es obligatorio")
    @Pattern(regexp = "^9\\d{8}$", message = "Formato: 9 dígitos empezando con 9")
    private String celular;

}