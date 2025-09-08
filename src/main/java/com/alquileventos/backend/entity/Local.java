package com.alquileventos.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "locales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Local {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_local")
    private Integer idLocal;
    
    @Column(name = "nombre_local", nullable = false, length = 150)
    private String nombreLocal;
    
    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;
    
    @Column(name = "aforo_maximo", nullable = false)
    private Integer aforoMaximo;
    
    @Column(name = "precio_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioHora;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoLocal estado = EstadoLocal.DISPONIBLE;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distrito", nullable = false)
    private Distrito distrito;
    
    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<FotoLocal> fotos;
    
    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reserva> reservas;
    
    @ManyToMany
    @JoinTable(
        name = "local_tipo_evento",
        joinColumns = @JoinColumn(name = "id_local"),
        inverseJoinColumns = @JoinColumn(name = "id_tipo_evento")
    )
    @JsonIgnore
    private List<TipoEvento> tiposEvento;
    
    public enum EstadoLocal {
        DISPONIBLE("Disponible"),
        NO_DISPONIBLE("No Disponible");
        
        private final String valor;
        
        EstadoLocal(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
    }
}
