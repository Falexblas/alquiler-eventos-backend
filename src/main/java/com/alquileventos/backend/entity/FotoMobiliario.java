package com.alquileventos.backend.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fotos_mobiliario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoMobiliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Integer idFoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mobiliario", nullable = false)
    private Mobiliario mobiliario;

    @Column(name = "url_foto", nullable = false)
    private String urlFoto;

    @Column(name = "descripcion")
    private String descripcion;
}
