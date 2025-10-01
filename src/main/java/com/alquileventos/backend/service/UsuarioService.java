package com.alquileventos.backend.service;

import com.alquileventos.backend.dto.usuario.ActualizarDatosDTO;
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

    public UsuarioPerfilDTO actualizarDatosCliente(Integer idUsuario, ActualizarDatosDTO datos) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        String emailLower = datos.getEmail().toLowerCase();

        if (!usuario.getEmail().equals(emailLower)) {
            if (usuarioRepository.existsByEmail(emailLower)) {
                throw new DuplicateResourceException("El email ya está en uso");
            }
            usuario.setEmail(emailLower);
        }

        usuario.setNombre(datos.getNombre());
        usuario.setApellido(datos.getApellido());
        usuario.setCelular(datos.getCelular());

        usuarioRepository.save(usuario);

        return obtenerPerfil(idUsuario);
    }

    /**
     * Administrador
     */

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> buscarPorNombreOApellido(String keyword) {
        return usuarioRepository.findByNombreOrApellidoContaining(keyword, keyword);
    }

    public List<Usuario> listarPorRol(Integer idRol) {
        return usuarioRepository.findByRol_IdRol(idRol);
    }

    public Usuario actualizarUsuario(Integer id, Usuario datosActualizados) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setNombre(datosActualizados.getNombre());
        usuario.setApellido(datosActualizados.getApellido());
        usuario.setCelular(datosActualizados.getCelular());

        String email = datosActualizados.getEmail().toLowerCase();
        if (!usuario.getEmail().equals(email)) {
            if (usuarioRepository.existsByEmail(email)) {
                throw new DuplicateResourceException("El email ya está en uso");
            }
            usuario.setEmail(email);
        }

        if (datosActualizados.getContrasena() != null &&
                !datosActualizados.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(datosActualizados.getContrasena()));
        }

        if (datosActualizados.getRol() != null) {
            usuario.setRol(datosActualizados.getRol());
        }

        return usuarioRepository.save(usuario);
    }

    public void eliminar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}