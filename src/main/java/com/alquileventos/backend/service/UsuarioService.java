package com.alquileventos.backend.service;

import com.alquileventos.backend.dto.usuario.ActualizarDatosDTO;
import com.alquileventos.backend.dto.usuario.ActualizarClienteAdminDTO;
import com.alquileventos.backend.dto.usuario.ClienteListDTO;
import com.alquileventos.backend.dto.usuario.UsuarioPerfilDTO;
import com.alquileventos.backend.entity.Usuario;
import com.alquileventos.backend.exception.DuplicateResourceException;
import com.alquileventos.backend.exception.ResourceNotFoundException;
import com.alquileventos.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Cliente
     */

    @Transactional(readOnly = true)
    public UsuarioPerfilDTO obtenerPerfil(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return convertirAUsuarioPerfilDTO(usuario);
    }

    @Transactional
    public UsuarioPerfilDTO actualizarDatosCliente(Integer idUsuario, ActualizarDatosDTO datos) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        String email = datos.getEmail().toLowerCase();

        if (!usuario.getEmail().equals(email)) {
            if (usuarioRepository.existsByEmail(email)) {
                throw new DuplicateResourceException("El email ya está en uso");
            }
            usuario.setEmail(email);
        }

        usuario.setNombre(datos.getNombre());
        usuario.setApellido(datos.getApellido());
        usuario.setCelular(datos.getCelular());

        usuarioRepository.save(usuario);

        return convertirAUsuarioPerfilDTO(usuario);
    }

    /**
     * Administrador
     */

    @Transactional(readOnly = true)
    public List<ClienteListDTO> listarClientes() {

        List<Usuario> clientes = usuarioRepository.findByRol_IdRol(1);

        return clientes.stream()
                .map(this::convertirAClienteListDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClienteListDTO> buscarClientes(String keyword) {
        List<Usuario> clientes = usuarioRepository.findByNombreOApellido(keyword);

        return clientes.stream()
                .filter(u -> u.getRol().getNombreRol().equals("ROLE_CLIENTE"))
                .map(this::convertirAClienteListDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioPerfilDTO obtenerClientePorId(Integer idCliente) {
        Usuario cliente = usuarioRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        if (!"ROLE_CLIENTE".equals(cliente.getRol().getNombreRol())) {
            throw new ResourceNotFoundException("El usuario no es un cliente");
        }

        return convertirAUsuarioPerfilDTO(cliente);
    }

    @Transactional
    public UsuarioPerfilDTO actualizarCliente(Integer idCliente, ActualizarClienteAdminDTO datos) {
        Usuario cliente = usuarioRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        if (!"ROLE_CLIENTE".equals(cliente.getRol().getNombreRol())) {
            throw new ResourceNotFoundException("El usuario no es un cliente");
        }

        String email = datos.getEmail().toLowerCase();

        if (!cliente.getEmail().equals(email)) {
            if (usuarioRepository.existsByEmail(email)) {
                throw new DuplicateResourceException("El email ya está en uso");
            }
            cliente.setEmail(email);
        }

        cliente.setNombre(datos.getNombre());
        cliente.setApellido(datos.getApellido());
        cliente.setCelular(datos.getCelular());

        usuarioRepository.save(cliente);

        return convertirAUsuarioPerfilDTO(cliente);
    }

    @Transactional
    public void eliminarCliente(Integer idCliente) {
        Usuario cliente = usuarioRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        if (!"ROLE_CLIENTE".equals(cliente.getRol().getNombreRol())) {
            throw new ResourceNotFoundException("El usuario no es un cliente");
        }

        usuarioRepository.deleteById(idCliente);
    }

    /**
     * Métodos de conversion
     */

    private UsuarioPerfilDTO convertirAUsuarioPerfilDTO(Usuario usuario) {
        return UsuarioPerfilDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .dni(usuario.getDni())
                .email(usuario.getEmail())
                .celular(usuario.getCelular())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }

    private ClienteListDTO convertirAClienteListDTO(Usuario usuario) {
        return ClienteListDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombreCompleto(usuario.getNombre() + " " + usuario.getApellido())
                .dni(usuario.getDni())
                .email(usuario.getEmail())
                .celular(usuario.getCelular())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }
}