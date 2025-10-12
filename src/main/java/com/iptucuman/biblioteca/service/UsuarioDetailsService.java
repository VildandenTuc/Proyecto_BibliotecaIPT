package com.iptucuman.biblioteca.service;

import com.iptucuman.biblioteca.modelo.Usuario;
import com.iptucuman.biblioteca.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()))
        );
    }

    // 📌 Cargar usuario por ID (para JWT con userId como subject)
    public UserDetails loadUserById(Integer userId) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + userId));

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()))
        );
    }
}
