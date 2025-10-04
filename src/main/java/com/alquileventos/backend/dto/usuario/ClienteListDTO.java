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
public class ClienteListDTO {
    private Integer idUsuario;
    private String nombreCompleto;
    private String dni;
    private String email;
    private String celular;
    private LocalDateTime fechaRegistro;
}