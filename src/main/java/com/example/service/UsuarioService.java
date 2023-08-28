package com.example.service;

import com.example.model.Usuario;

import java.util.Optional;

public interface UsuarioService {
    Optional<Usuario> findById(Integer id);
    Usuario save(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
}