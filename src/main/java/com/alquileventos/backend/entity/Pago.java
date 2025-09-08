package com.alquileventos.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    private MetodoPago metodoPago;
    
    @Column(name = "monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPago estado = EstadoPago.PENDIENTE;
    
    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
    
    @Column(name = "comprobante_url", length = 255)
    private String comprobanteUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;
    
    @PrePersist
    protected void onCreate() {
        fechaPago = LocalDateTime.now();
    }
    
    public enum MetodoPago {
        TARJETA("Tarjeta"),
        YAPE("Yape"),
        PLIN("Plin");
        
        private final String valor;
        
        MetodoPago(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
    }
    
    public enum EstadoPago {
        PENDIENTE("Pendiente"),
        PAGADO("Pagado"),
        FALLIDO("Fallido");
        
        private final String valor;
        
        EstadoPago(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
    }
}
