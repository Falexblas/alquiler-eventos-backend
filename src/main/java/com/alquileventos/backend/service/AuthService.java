package com.alquileventos.backend.service;

import com.alquileventos.backend.dto.auth.*;
import com.alquileventos.backend.entity.Rol;
import com.alquileventos.backend.entity.Usuario;
import com.alquileventos.backend.exception.DuplicateResourceException;
import com.alquileventos.backend.exception.ResourceNotFoundException;
import com.alquileventos.backend.exception.UnauthorizedException;
import com.alquileventos.backend.repository.RolRepository;
import com.alquileventos.backend.repository.UsuarioRepository;
import com.alquileventos.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Registro de nuevo usuario
     */

    @Transactional
    public AuthResponseDTO register(RegisterRequest req) {

        String email = req.getEmail().toLowerCase();

        if (usuarioRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("El email ya está registrado");
        }

        if (usuarioRepository.existsByDni(req.getDni())) {
            throw new DuplicateResourceException("El DNI ya está registrado");
        }

        Rol rolCliente = rolRepository.findByNombreRol("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(req.getNombre());
        nuevoUsuario.setApellido(req.getApellido());
        nuevoUsuario.setEmail(req.getEmail().toLowerCase());
        nuevoUsuario.setDni(req.getDni());
        nuevoUsuario.setCelular(req.getCelular());
        nuevoUsuario.setContrasena(passwordEncoder.encode(req.getContrasena()));
        nuevoUsuario.setRol(rolCliente);
        nuevoUsuario.setFechaRegistro(LocalDateTime.now());

        usuarioRepository.save(nuevoUsuario);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.getContrasena())
        );
        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .idUsuario(nuevoUsuario.getIdUsuario())
                .email(nuevoUsuario.getEmail())
                .nombreCompleto(nuevoUsuario.getNombre() + " " + nuevoUsuario.getApellido())
                .rol(rolCliente.getNombreRol())
                .build();
    }

    /**
     * Autenticacion de usuario
     */

    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequest req) {
        String email = req.getEmail().toLowerCase();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.getContrasena())
        );

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!"CLIENTE".equals(usuario.getRol().getNombreRol())) {
            throw new UnauthorizedException("Acceso denegado. Use el login de administrador");
        }

        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .idUsuario(usuario.getIdUsuario())
                .email(usuario.getEmail())
                .nombreCompleto(usuario.getNombre() + " " + usuario.getApellido())
                .rol(usuario.getRol().getNombreRol())
                .build();
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO adminLogin(AdminLoginRequestDTO req) {
        String email = req.getEmail().toLowerCase();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.getContrasena())
        );

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!"ADMIN".equals(usuario.getRol().getNombreRol())) {
            throw new UnauthorizedException("No tiene permisos de administrador");
        }

        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .idUsuario(usuario.getIdUsuario())
                .email(usuario.getEmail())
                .nombreCompleto(usuario.getNombre() + " " + usuario.getApellido())
                .rol(usuario.getRol().getNombreRol())
                .build();
    }
}