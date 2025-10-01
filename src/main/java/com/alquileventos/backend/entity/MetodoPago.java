package com.alquileventos.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "metodos_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Integer idMetodoPago;

    @Column(name = "nombre_metodo", nullable = false, unique = true)
    private String nombreMetodo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @JsonIgnore
    @OneToMany(mappedBy = "metodoPago", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pago> pagos;
}
