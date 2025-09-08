package com.alquileventos.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tipos_evento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoEvento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_evento")
    private Integer idTipoEvento;
    
    @Column(name = "nombre_tipo", nullable = false, unique = true, length = 100)
    private String nombreTipo;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "tiposEvento")
    private List<Local> locales;
    
    @JsonIgnore
    @OneToMany(mappedBy = "tipoEvento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;
}
