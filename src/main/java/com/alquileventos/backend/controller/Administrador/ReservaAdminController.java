package com.alquileventos.backend.controller.Administrador;

import com.alquileventos.backend.entity.Reserva;
import com.alquileventos.backend.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reservas")
@RequiredArgsConstructor
public class ReservaAdminController {

    private final ReservaService reservaService;

    //Listar
    @GetMapping
    public ResponseEntity<List<Reserva>> listarTodos() {
        List<Reserva> reservas = reservaService.findAll();
        return ResponseEntity.ok(reservas);
    }

}
