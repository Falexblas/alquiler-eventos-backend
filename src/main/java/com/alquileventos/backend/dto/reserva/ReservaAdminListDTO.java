package com.alquileventos.backend.dto.reserva;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservaAdminListDTO {
    private Integer idReserva;
    private LocalDate fechaEvento;
    private LocalDate fechaReserva;
    private String cliente;
    private String emailCliente;
    private String celularCliente;
    private String local;
    private String estado;
    private BigDecimal costoTotal;
}
