package com.alquileventos.backend.dto.reserva;

import com.alquileventos.backend.dto.mobiliario.MobiliarioResumenDTO;

import java.math.BigDecimal;
import java.util.List;

public class PresupuestoDTO {
    private BigDecimal costoLocalPorHora;
    private BigDecimal totalHoras;
    private BigDecimal subtotalLocal;
    private List<MobiliarioResumenDTO> mobiliariosSeleccionados;
    private BigDecimal subtotalMobiliario;
    private BigDecimal totalEstimado;
}
