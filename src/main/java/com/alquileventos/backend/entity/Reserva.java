package com.alquileventos.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;
    
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;
    
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;
    
    @Column(name = "cantidad_personas", nullable = false)
    private Integer cantidadPersonas;
    
    @Column(name = "costo_local", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoLocal;
    
    @Column(name = "costo_mobiliario", precision = 10, scale = 2)
    private BigDecimal costoMobiliario = BigDecimal.ZERO;
    
    @Column(name = "costo_total", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal costoTotal;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoReserva estado = EstadoReserva.PENDIENTE;
    
    @Column(name = "fecha_reserva")
    private LocalDateTime fechaReserva;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local", nullable = false)
    private Local local;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_evento", nullable = false)
    private TipoEvento tipoEvento;
    
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservaMobiliario> mobiliario;
    
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pago> pagos;
    
    @PrePersist
    protected void onCreate() {
        fechaReserva = LocalDateTime.now();
    }
    
    @Getter
    public enum EstadoReserva {
        PENDIENTE("Pendiente"),
        CONFIRMADA("Confirmada"),
        CANCELADA("Cancelada");
        
        private final String valor;
        
        EstadoReserva(String valor) {
            this.valor = valor;
        }

    }
}
