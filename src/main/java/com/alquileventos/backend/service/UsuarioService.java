package com.alquileventos.backend.service;

import com.alquileventos.backend.entity.Usuario;
import com.alquileventos.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existsByDni(String dni){
        return usuarioRepository.findByDni(dni).isPresent();
    }

    public Usuario save(Usuario user) {
        if (existsByEmail(user.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        if (existsByDni(user.getDni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }
        if (user.getContrasena() != null && !user.getContrasena().isEmpty()) {
            user.setContrasena(passwordEncoder.encode(user.getContrasena()));
        }
        return usuarioRepository.save(user);
    }

    /**
     * Cliente
     */

    public Usuario updateProfile(Integer id, Usuario updatedUser) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setCelular(updatedUser.getCelular());
                    usuario.setEmail(updatedUser.getEmail().toLowerCase());

                    if (updatedUser.getContrasena() != null && !updatedUser.getContrasena().isEmpty()) {
                        usuario.setContrasena(passwordEncoder.encode(updatedUser.getContrasena()));
                    }
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(
                        () -> new RuntimeException("Usuario no encontrado con ID" + id));
    }

    /**
     * Administrador
     */

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario updateUser(Integer id, Usuario updatedUser) {
        return usuarioRepository.findById(id)
                .map(usuario -> {

                    usuario.setNombre(updatedUser.getNombre());
                    usuario.setApellido(updatedUser.getApellido());
                    usuario.setDni(updatedUser.getDni());
                    usuario.setEmail(updatedUser.getEmail().toLowerCase());
                    usuario.setCelular(updatedUser.getCelular());

                    if (updatedUser.getContrasena() != null && !updatedUser.getContrasena().isEmpty()) {
                        usuario.setContrasena(passwordEncoder.encode(updatedUser.getContrasena()));
                    }

                    if (updatedUser.getRol() != null) {
                        usuario.setRol(updatedUser.getRol());
                    }

                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public void deleteById(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> findByRol(Integer idRol) {
        return usuarioRepository.findByRol_IdRol(idRol);
    }

    public List<Usuario> findByNameOrLastname(String keyword) {
        return usuarioRepository.findByNombreOrApellidoContaining(keyword, keyword);
    }
}