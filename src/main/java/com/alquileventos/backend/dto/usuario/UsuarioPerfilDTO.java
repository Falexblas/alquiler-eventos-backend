package com.alquileventos.backend.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfilDTO {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String celular;
    private LocalDateTime fechaRegistro;
}