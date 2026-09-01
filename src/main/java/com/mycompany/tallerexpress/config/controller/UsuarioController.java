
package com.mycompany.tallerexpress.config.controller;

import com.mycompany.tallerexpress.model.Usuario;
import com.mycompany.tallerexpress.service.UsuarioService;

public class UsuarioController {

    private final UsuarioService usuarioService;

    // Recibe el servicio (idealmente ya envuelto por los Decoradores)
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario y la contraseña son obligatorios.");
        }
        return usuarioService.login(username.trim(), password);
    }

    public Usuario registrarUsuario(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username.trim());
        nuevoUsuario.setPassword(password);

        // El servicio decorado le asignará automáticamente ROLE: RECEPCIONISTA, ACTIVO y fecha actual
        return usuarioService.create(nuevoUsuario);
    }
}
