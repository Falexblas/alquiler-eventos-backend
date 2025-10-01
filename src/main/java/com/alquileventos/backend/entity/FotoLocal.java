package com.alquileventos.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fotos_locales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoLocal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Integer idFoto;
    
    @Column(name = "url_foto", nullable = false)
    private String urlFoto;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local", nullable = false)
    private Local local;
}
