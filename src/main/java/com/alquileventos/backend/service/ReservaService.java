package com.alquileventos.backend.service;

import com.alquileventos.backend.dto.reserva.*;
import com.alquileventos.backend.entity.Local;
import com.alquileventos.backend.entity.Reserva;
import com.alquileventos.backend.entity.TipoEvento;
import com.alquileventos.backend.entity.Usuario;
import com.alquileventos.backend.exception.ResourceNotFoundException;
import com.alquileventos.backend.repository.LocalRepository;
import com.alquileventos.backend.repository.ReservaRepository;
import com.alquileventos.backend.repository.TipoEventoRepository;
import com.alquileventos.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {
    
    private final ReservaRepository reservaRepository;
    private final LocalRepository localRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoEventoRepository tipoEventoRepository;

    @Transactional(readOnly = true)
    public DisponibilidadDTO verificarDisponibilidad(
            Integer idLocal,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin) {

        boolean disponible = reservaRepository.estaDisponible(idLocal, fecha, horaInicio, horaFin);

        String mensaje = disponible
                ? "Local disponible para las fechas seleccionadas"
                : "Local no disponible. Hay otra reserva en ese horario (incluye 1h de limpieza antes/después)";

        return DisponibilidadDTO.builder()
                .disponible(disponible)
                .mensaje(mensaje)
                .build();
    }

    @Transactional(readOnly = true)
    public PresupuestoDTO calcularPresupuesto(CrearReservaDTO datos) {
        Local local = localRepository.findById(datos.getIdLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado"));

        int totalHoras = calcularTotalHoras(datos.getHoraInicio(), datos.getHoraFin());
        BigDecimal costoLocal = calcularCostoLocal(local.getPrecioHora(), totalHoras);

        return PresupuestoDTO.builder()
                .precioHora(local.getPrecioHora())
                .totalHoras(totalHoras)
                .costoLocal(costoLocal)
                .costoMobiliario(BigDecimal.ZERO)
                .costoTotal(costoLocal)
                .build();
    }

    @Transactional
    public ReservaDetalleDTO crearReserva(CrearReservaDTO datos, Integer idUsuario) {

        Local local = localRepository.findById(datos.getIdLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado"));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        TipoEvento tipoEvento = tipoEventoRepository.findById(datos.getIdTipoEvento())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de evento no encontrado"));

        validarReserva(datos, local);

        int totalHoras = calcularTotalHoras(datos.getHoraInicio(), datos.getHoraFin());
        BigDecimal costoLocal = calcularCostoLocal(local.getPrecioHora(), totalHoras);

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setLocal(local);
        reserva.setTipoEvento(tipoEvento);
        reserva.setFecha(datos.getFecha());
        reserva.setHoraInicio(datos.getHoraInicio());
        reserva.setHoraFin(datos.getHoraFin());
        reserva.setCantidadPersonas(datos.getCantidadPersonas());
        reserva.setCostoLocal(costoLocal);
        reserva.setCostoMobiliario(BigDecimal.ZERO);
        reserva.setCostoTotal(costoLocal);
        reserva.setEstado(Reserva.EstadoReserva.PENDIENTE);
        reserva.setFechaReserva(LocalDateTime.now());

        Reserva reservaGuardada = reservaRepository.save(reserva);

        return convertirAReservaDetalleDTO(reservaGuardada);
    }

    @Transactional(readOnly = true)
    public List<ReservaResumenDTO> obtenerMisReservas(Integer idUsuario) {
        List<Reserva> reservas = reservaRepository.findByUsuario_IdUsuarioOrderByFechaReservaDesc(idUsuario);
        return reservas.stream()
                .map(this::convertirAReservaResumenDTO)
                .collect(Collectors.toList());
    }

    // Metodos privados

    private int calcularTotalHoras(LocalTime inicio, LocalTime fin) {
        long minutos = Duration.between(inicio, fin).toMinutes();
        return (int) Math.ceil(minutos / 60.0);
    }

    private BigDecimal calcularCostoLocal(BigDecimal precioHora, int totalHoras) {
        return precioHora.multiply(BigDecimal.valueOf(totalHoras));
    }

    private void validarReserva(CrearReservaDTO datos, Local local) {
        if (datos.getCantidadPersonas() > local.getAforoMaximo()) {
            throw new IllegalArgumentException("La cantidad de personas excede el aforo máximo (" + local.getAforoMaximo() + ")");
        }

        boolean disponible = reservaRepository.estaDisponible(
                datos.getIdLocal(), datos.getFecha(),
                datos.getHoraInicio(), datos.getHoraFin());

        if (!disponible) {
            throw new IllegalStateException("El local no está disponible en ese horario");
        }
    }

    private ReservaDetalleDTO convertirAReservaDetalleDTO(Reserva reserva) {
        long minutos = Duration.between(reserva.getHoraInicio(), reserva.getHoraFin()).toMinutes();
        int totalHoras = (int) Math.ceil(minutos / 60.0);

        return ReservaDetalleDTO.builder()
                .idReserva(reserva.getIdReserva())
                .idLocal(reserva.getLocal().getIdLocal())
                .nombreLocal(reserva.getLocal().getNombreLocal())
                .distrito(reserva.getLocal().getDistrito().getNombreDistrito())
                .direccion(reserva.getLocal().getDireccion())
                .fotoPrincipal(reserva.getLocal().getFotos().isEmpty()
                        ? null : reserva.getLocal().getFotos().get(0).getUrlFoto())
                .tipoEvento(reserva.getTipoEvento().getNombreTipo())
                .fecha(reserva.getFecha())
                .horaInicio(reserva.getHoraInicio())
                .horaFin(reserva.getHoraFin())
                .totalHoras(totalHoras)
                .cantidadPersonas(reserva.getCantidadPersonas())
                .nombreCliente(reserva.getUsuario().getNombre())
                .apellidoCliente(reserva.getUsuario().getApellido())
                .emailCliente(reserva.getUsuario().getEmail())
                .costoLocal(reserva.getCostoLocal())
                .costoMobiliario(reserva.getCostoMobiliario())
                .costoTotal(reserva.getCostoTotal())
                .estado(reserva.getEstado().name())
                .fechaReserva(reserva.getFechaReserva())
                .build();
    }

    private ReservaResumenDTO convertirAReservaResumenDTO(Reserva reserva) {
        return ReservaResumenDTO.builder()
                .idReserva(reserva.getIdReserva())
                .nombreLocal(reserva.getLocal().getNombreLocal())
                .distrito(reserva.getLocal().getDistrito().getNombreDistrito())
                .fecha(reserva.getFecha())
                .tipoEvento(reserva.getTipoEvento().getNombreTipo())
                .costoTotal(reserva.getCostoTotal())
                .estado(reserva.getEstado().name())
                .fechaReserva(reserva.getFechaReserva())
                .build();
    }




    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }
    
    public Optional<Reserva> findById(Integer id) {
        return reservaRepository.findById(id);
    }
    
    public Reserva save(Reserva reserva) {

        if (!isLocalDisponible(reserva.getLocal().getIdLocal(), reserva.getFecha(), 
                              reserva.getHoraInicio(), reserva.getHoraFin())) {
            throw new RuntimeException("El local no está disponible en el horario solicitado");
        }

        reserva.setCostoTotal(calcularCostoTotal(reserva));
        
        return reservaRepository.save(reserva);
    }
    
    public Reserva update(Integer id, Reserva reservaActualizada) {
        return reservaRepository.findById(id)
            .map(reserva -> {
                // Validar disponibilidad si se cambia fecha/hora/local
                if (!reserva.getLocal().getIdLocal().equals(reservaActualizada.getLocal().getIdLocal()) ||
                    !reserva.getFecha().equals(reservaActualizada.getFecha()) ||
                    !reserva.getHoraInicio().equals(reservaActualizada.getHoraInicio()) ||
                    !reserva.getHoraFin().equals(reservaActualizada.getHoraFin())) {
                    
                    if (!isLocalDisponible(reservaActualizada.getLocal().getIdLocal(), 
                                         reservaActualizada.getFecha(),
                                         reservaActualizada.getHoraInicio(), 
                                         reservaActualizada.getHoraFin(), id)) {
                        throw new RuntimeException("El local no está disponible en el nuevo horario solicitado");
                    }
                }
                
                reserva.setFecha(reservaActualizada.getFecha());
                reserva.setHoraInicio(reservaActualizada.getHoraInicio());
                reserva.setHoraFin(reservaActualizada.getHoraFin());
                reserva.setCantidadPersonas(reservaActualizada.getCantidadPersonas());
                reserva.setEstado(reservaActualizada.getEstado());
                
                if (reservaActualizada.getLocal() != null) {
                    reserva.setLocal(reservaActualizada.getLocal());
                }
                
                if (reservaActualizada.getTipoEvento() != null) {
                    reserva.setTipoEvento(reservaActualizada.getTipoEvento());
                }
                
                // Recalcular costo total
                reserva.setCostoTotal(calcularCostoTotal(reserva));
                
                return reservaRepository.save(reserva);
            })
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }
    
    public void deleteById(Integer id) {
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva no encontrada con ID: " + id);
        }
        reservaRepository.deleteById(id);
    }
    
    public List<Reserva> findByUsuario(Integer idUsuario) {
        return reservaRepository.findByUsuario_IdUsuario(idUsuario);
    }
    
    public List<Reserva> findByLocal(Integer idLocal) {
        return reservaRepository.findByLocal_IdLocal(idLocal);
    }
    
    public List<Reserva> findByEstado(Reserva.EstadoReserva estado) {
        return reservaRepository.findByEstado(estado);
    }
    
    public List<Reserva> findByFecha(LocalDate fecha) {
        return reservaRepository.findByFecha(fecha);
    }
    
    public List<Reserva> findByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return reservaRepository.findByRangoFechas(fechaInicio, fechaFin);
    }
    
    public boolean isLocalDisponible(Integer idLocal, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        return isLocalDisponible(idLocal, fecha, horaInicio, horaFin, null);
    }
    
    public boolean isLocalDisponible(Integer idLocal, LocalDate fecha, LocalTime horaInicio, 
                                   LocalTime horaFin, Integer excludeReservaId) {
        List<Reserva> conflictos = reservaRepository.findConflictingReservations(idLocal, fecha, horaInicio, horaFin);
        
        // Excluir la reserva actual si se está actualizando
        if (excludeReservaId != null) {
            conflictos = conflictos.stream()
                .filter(r -> !r.getIdReserva().equals(excludeReservaId))
                .toList();
        }
        
        return conflictos.isEmpty();
    }
    
    public Reserva confirmarReserva(Integer id) {
        return reservaRepository.findById(id)
            .map(reserva -> {
                reserva.setEstado(Reserva.EstadoReserva.CONFIRMADA);
                return reservaRepository.save(reserva);
            })
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }
    
    public Reserva cancelarReserva(Integer id) {
        return reservaRepository.findById(id)
            .map(reserva -> {
                reserva.setEstado(Reserva.EstadoReserva.CANCELADA);
                return reservaRepository.save(reserva);
            })
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }
    
    private BigDecimal calcularCostoTotal(Reserva reserva) {
        // Calcular horas de duración
        Duration duracion = Duration.between(reserva.getHoraInicio(), reserva.getHoraFin());
        long horas = duracion.toHours();
        
        // Costo base del local
        BigDecimal costoLocal = reserva.getLocal().getPrecioHora().multiply(BigDecimal.valueOf(horas));
        
        // Agregar costo del mobiliario si existe
        BigDecimal costoMobiliario = BigDecimal.ZERO;
        if (reserva.getMobiliario() != null) {
            costoMobiliario = reserva.getMobiliario().stream()
                .map(rm -> rm.getPrecioUnitario().multiply(BigDecimal.valueOf(rm.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        
        return costoLocal.add(costoMobiliario);
    }
}
