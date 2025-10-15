package com.alquileventos.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "mobiliario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mobiliario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mobiliario")
    private Integer idMobiliario;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "stock_total", nullable = false)
    private Integer stockTotal;
    
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @OneToMany(mappedBy = "mobiliario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FotoMobiliario> fotos;
    
    @OneToMany(mappedBy = "mobiliario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservaMobiliario> reservasMobiliario;
}
