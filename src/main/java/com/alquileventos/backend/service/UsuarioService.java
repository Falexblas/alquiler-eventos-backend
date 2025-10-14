package com.alquileventos.backend.service;

import com.alquileventos.backend.dto.usuario.*;
import com.alquileventos.backend.entity.Rol;
import com.alquileventos.backend.entity.Usuario;
import com.alquileventos.backend.exception.DuplicateResourceException;
import com.alquileventos.backend.exception.ResourceNotFoundException;
import com.alquileventos.backend.exception.UnauthorizedException;
import com.alquileventos.backend.repository.RolRepository;
import com.alquileventos.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

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

        actualizarDatosBasicos(usuario, datos.getNombre(), datos.getApellido(), datos.getEmail(),  datos.getCelular());
        usuarioRepository.save(usuario);
        return convertirAUsuarioPerfilDTO(usuario);
    }

    @Transactional
    public void cambiarContrasena(Integer idUsuario, CambiarContrasenaDTO datos) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(datos.getContrasenaActual(), usuario.getContrasena())) {
            throw new UnauthorizedException("La contraseña actual es incorrecta");
        }

        if (!datos.getNuevaContrasena().equals(datos.getConfirmarContrasena())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        if (passwordEncoder.matches(datos.getNuevaContrasena(), usuario.getContrasena())) {
            throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual");
        }

        usuario.setContrasena(passwordEncoder.encode(datos.getNuevaContrasena()));
        usuarioRepository.save(usuario);
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

        actualizarDatosBasicos(cliente, datos.getNombre(), datos.getApellido(), datos.getEmail(),  datos.getCelular());
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

    @Transactional
    public UsuarioPerfilDTO crearAdministrador(CrearAdminDTO datos) {

        if (usuarioRepository.existsByEmail(datos.getEmail().toLowerCase())) {
            throw new DuplicateResourceException("El email ya está registrado");
        }

        if (usuarioRepository.existsByDni(datos.getDni())) {
            throw new DuplicateResourceException("El DNI ya está registrado");
        }

        Rol rolAdmin = rolRepository.findByNombreRol("ROLE_ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("Rol ADMIN no encontrado"));

        Usuario nuevoAdmin = new Usuario();
        nuevoAdmin.setNombre(datos.getNombre());
        nuevoAdmin.setApellido(datos.getApellido());
        nuevoAdmin.setDni(datos.getDni());
        nuevoAdmin.setCelular(datos.getCelular());
        nuevoAdmin.setEmail(datos.getEmail().toLowerCase());
        nuevoAdmin.setContrasena(passwordEncoder.encode(datos.getContrasena()));
        nuevoAdmin.setRol(rolAdmin);
        nuevoAdmin.setFechaRegistro(LocalDateTime.now());

        usuarioRepository.save(nuevoAdmin);

        return convertirAUsuarioPerfilDTO(nuevoAdmin);
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

    private void actualizarDatosBasicos(Usuario usuario, String nombre, String apellido,
                                        String email, String celular) {
        String emailLower = email.toLowerCase();

        if (!usuario.getEmail().equals(emailLower)) {
            if (usuarioRepository.existsByEmail(emailLower)) {
                throw new DuplicateResourceException("El email ya está en uso");
            }
            usuario.setEmail(emailLower);
        }

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCelular(celular);
    }

}