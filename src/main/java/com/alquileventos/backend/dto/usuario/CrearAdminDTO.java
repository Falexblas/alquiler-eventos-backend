package com.alquileventos.backend.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CrearAdminDTO {
    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotBlank
    @Size(max = 100)
    private String apellido;

    @NotBlank
    @Size(min = 8, max = 8)
    private String dni;

    @NotBlank
    @Pattern(regexp = "^9\\d{8}$")
    private String celular;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String contrasena;
}
