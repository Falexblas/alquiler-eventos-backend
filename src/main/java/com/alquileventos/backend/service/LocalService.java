package com.alquileventos.backend.service;

import com.alquileventos.backend.dto.common.TipoEventoDTO;
import com.alquileventos.backend.dto.local.*;
import com.alquileventos.backend.entity.*;
import com.alquileventos.backend.exception.ResourceNotFoundException;
import com.alquileventos.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalService {
    
    private final LocalRepository localRepository;
    private final DistritoRepository distritoRepository;
    private final TipoEventoRepository tipoEventoRepository;
    private final FotoLocalRepository fotoLocalRepository;

    /**
     * Cliente
     */

    @Transactional(readOnly = true)
    public List<LocalCardDTO> listarLocalesDisponibles(){
        List<Local> locales = localRepository.findByEstado(Local.EstadoLocal.DISPONIBLE);
        return locales.stream()
                .map(this::convertirALocalCardDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LocalCardDTO> buscarConFiltros(LocalFiltroDTO filtros){
        List<Local> locales = localRepository.buscarConFiltros(
                filtros.getIdDistrito(),
                filtros.getIdTipoEvento(),
                filtros.getAforoMin(),
                filtros.getPrecioMin(),
                filtros.getPrecioMax()
        );
        return locales.stream()
                .map(this::convertirALocalCardDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LocalDetalleDTO obtenerDetalle(Integer idLocal){
        Local local = localRepository.findByIdWithFotos(idLocal)
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado con ID: " + idLocal));
        return convertirALocalDetalleDTO(local);
    }

    /**
     * Administrador
     */

    @Transactional(readOnly = true)
    public List<LocalAdminListDTO> listarTodos(){
        List<Local> locales = localRepository.findAll();
        return locales.stream()
                .map(this::convertirALocalAdminListDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LocalAdminListDTO> buscarPorNombre(String nombre){
        List<Local> locales = localRepository.findByNombreLocalContainingIgnoreCase(nombre);
        return locales.stream()
                .map(this::convertirALocalAdminListDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LocalDetalleDTO crear(CrearLocalAdminDTO datos){

        Distrito distrito = distritoRepository.findById(datos.getIdDistrito())
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado"));

        List<TipoEvento> tiposEvento = tipoEventoRepository.findAllById(datos.getIdsTiposEvento());
        if (tiposEvento.size() != datos.getIdsTiposEvento().size()) {
            throw new ResourceNotFoundException("Algunos tipos de evento no existen");
        }

        Local local = new Local();
        local.setNombreLocal(datos.getNombreLocal());
        local.setDireccion(datos.getDireccion());
        local.setDistrito(distrito);
        local.setAforoMaximo(datos.getAforoMaximo());
        local.setPrecioHora(datos.getPrecioHora());
        local.setDescripcion(datos.getDescripcion());
        local.setEstado(Local.EstadoLocal.DISPONIBLE);
        local.setTiposEvento(tiposEvento);

        Local localGuardado = localRepository.save(local);

        if (datos.getUrlsFotos() != null && !datos.getUrlsFotos().isEmpty()) {
            for (String url : datos.getUrlsFotos()) {
                FotoLocal foto = new FotoLocal();
                foto.setLocal(localGuardado);
                foto.setUrlFoto(url);
                fotoLocalRepository.save(foto);
            }
        }

        return obtenerDetalle(localGuardado.getIdLocal());
    }

    @Transactional
    public LocalDetalleDTO actualizar(Integer id, ActualizarLocalAdminDTO datos){
        Local local = localRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado con ID: " + id));

        Distrito distrito = distritoRepository.findById(datos.getIdDistrito())
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado"));

        List<TipoEvento> tiposEvento = tipoEventoRepository.findAllById(datos.getIdsTiposEvento());
        if (tiposEvento.size() != datos.getIdsTiposEvento().size()) {
            throw new ResourceNotFoundException("Algunos tipos de evento no existen");
        }

        local.setNombreLocal(datos.getNombreLocal());
        local.setDireccion(datos.getDireccion());
        local.setDistrito(distrito);
        local.setAforoMaximo(datos.getAforoMaximo());
        local.setPrecioHora(datos.getPrecioHora());
        local.setDescripcion(datos.getDescripcion());
        local.setTiposEvento(tiposEvento);

        if (datos.getUrlsFotos() != null) {
            fotoLocalRepository.deleteByLocal_IdLocal(id);

            for (String url : datos.getUrlsFotos()) {
                FotoLocal foto = new FotoLocal();
                foto.setLocal(local);
                foto.setUrlFoto(url);
                fotoLocalRepository.save(foto);
            }
        }

        localRepository.save(local);
        return obtenerDetalle(id);
    }

    @Transactional
    public LocalDetalleDTO cambiarEstado(Integer id, Local.EstadoLocal nuevoEstado){
        Local local = localRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado con ID: " + id));

        local.setEstado(nuevoEstado);
        localRepository.save(local);

        return obtenerDetalle(id);
    }

    @Transactional
    public void eliminar(Integer id){
        if (!localRepository.existsById(id)) {
            throw new RuntimeException("Local no encontrado con ID: " + id);
        }
        localRepository.deleteById(id);
    }

    /**
     * Métodos de conversion
     */

    private LocalCardDTO convertirALocalCardDTO(Local local) {
        return LocalCardDTO.builder()
                .idLocal(local.getIdLocal())
                .nombreLocal(local.getNombreLocal())
                .distrito(local.getDistrito().getNombreDistrito())
                .aforoMaximo(local.getAforoMaximo())
                .precioHora(local.getPrecioHora())
               .fotoPrincipal(obtenerFotoPrincipal(local))
                .tiposEvento(local.getTiposEvento().stream()
                        .map(TipoEvento::getNombreTipo)
                        .collect(Collectors.toList()))
                .build();
    }

    private LocalDetalleDTO convertirALocalDetalleDTO(Local local) {
        return LocalDetalleDTO.builder()
                .idLocal(local.getIdLocal())
                .nombreLocal(local.getNombreLocal())
                .direccion(local.getDireccion())
                .distrito(local.getDistrito().getNombreDistrito())
                .idDistrito(local.getDistrito().getIdDistrito())
                .aforoMaximo(local.getAforoMaximo())
                .precioHora(local.getPrecioHora())
                .descripcion(local.getDescripcion())
                .estado(local.getEstado())
                .fotos(local.getFotos().stream()
                        .map(foto -> FotoLocalDTO.builder()
                                .idFoto(foto.getIdFoto())
                                .urlFoto(foto.getUrlFoto())
                                .descripcion(foto.getDescripcion())
                                .build())
                        .collect(Collectors.toList()))
                .tiposEvento(local.getTiposEvento().stream()
                        .map(te -> TipoEventoDTO.builder()
                                .idTipoEvento(te.getIdTipoEvento())
                                .nombreTipo(te.getNombreTipo())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private LocalAdminListDTO convertirALocalAdminListDTO(Local local) {
        return LocalAdminListDTO.builder()
                .idLocal(local.getIdLocal())
                .nombreLocal(local.getNombreLocal())
                .distrito(local.getDistrito().getNombreDistrito())
                .aforoMaximo(local.getAforoMaximo())
                .precioHora(local.getPrecioHora())
                .estado(local.getEstado())
                .build();
    }

    private String obtenerFotoPrincipal(Local local) {
        return local.getFotos().isEmpty()
                ? null
                : local.getFotos().getFirst().getUrlFoto();
    }
}
