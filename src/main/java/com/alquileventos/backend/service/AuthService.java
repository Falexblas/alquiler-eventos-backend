package com.alquileventos.backend.service;

import com.alquileventos.backend.dto.LoginRequest;
import com.alquileventos.backend.dto.LoginResponse;
import com.alquileventos.backend.dto.RegisterRequest;
import com.alquileventos.backend.entity.Rol;
import com.alquileventos.backend.entity.Usuario;
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
    public void register(RegisterRequest req) {

        if (usuarioRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        if (usuarioRepository.findByDni(req.getDni()).isPresent()) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        Rol rolCliente = rolRepository.findByNombreRol("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(req.getNombre());
        nuevoUsuario.setApellido(req.getApellido());
        nuevoUsuario.setEmail(req.getEmail());
        nuevoUsuario.setDni(req.getDni());
        nuevoUsuario.setCelular(req.getCelular());
        nuevoUsuario.setContrasena(passwordEncoder.encode(req.getContrasena()));
        nuevoUsuario.setRol(rolCliente);

        usuarioRepository.save(nuevoUsuario);
    }

    /**
     * Autenticacion de usuario
     */

    public LoginResponse authenticate(LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getContrasena()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        Usuario usuario = usuarioRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new LoginResponse(
                token,
                "Bearer",
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getRol().getNombreRol()
        );
    }
}