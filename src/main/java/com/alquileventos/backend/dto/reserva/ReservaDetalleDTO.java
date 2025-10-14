package com.alquileventos.backend.dto.reserva;

import com.alquileventos.backend.dto.mobiliario.MobiliarioItemDTO;
import com.alquileventos.backend.dto.pago.PagoDetalleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// Confirmacion y detalle de reserva

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDetalleDTO {
    private Integer idReserva;

    private Integer idLocal;
    private String nombreLocal;
    private String distrito;
    private String direccion;
    private String fotoPrincipal;

    private String tipoEvento;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer totalHoras;
    private Integer cantidadPersonas;

    private String nombreCliente;
    private String apellidoCliente;
    private String emailCliente;

    private BigDecimal costoLocal;
    private BigDecimal costoMobiliario;
    private BigDecimal costoTotal;

    private List<MobiliarioItemDTO> mobiliarios;

    private PagoDetalleDTO pago;

    private String estado;
    private LocalDateTime fechaReserva;
}
