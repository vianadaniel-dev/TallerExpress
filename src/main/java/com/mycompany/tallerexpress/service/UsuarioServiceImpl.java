
package com.mycompany.tallerexpress.service;

import com.mycompany.tallerexpress.model.Usuario;
import com.mycompany.tallerexpress.repository.UsuarioRepository;


    public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario create(Usuario usuario) {
        return usuarioRepository.guardar(usuario);
    }

    @Override
    public Usuario login(String username, String password) {
        return usuarioRepository.autenticar(username, password);
    }
}

