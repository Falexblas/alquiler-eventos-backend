package com.alquileventos.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "reserva_mobiliario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaMobiliario {
    
    @EmbeddedId
    private ReservaMobiliarioId id;
    
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idReserva")
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idMobiliario")
    @JoinColumn(name = "id_mobiliario")
    private Mobiliario mobiliario;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservaMobiliarioId {
        @Column(name = "id_reserva")
        private Integer idReserva;
        
        @Column(name = "id_mobiliario")
        private Integer idMobiliario;
    }
}
