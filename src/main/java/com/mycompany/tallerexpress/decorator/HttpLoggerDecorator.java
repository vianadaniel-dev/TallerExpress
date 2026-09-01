
package com.mycompany.tallerexpress.decorator;

import com.mycompany.tallerexpress.model.Usuario;
import com.mycompany.tallerexpress.service.UsuarioService;

public class HttpLoggerDecorator implements UsuarioService {

    private final UsuarioService usuarioService;

    public HttpLoggerDecorator(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Usuario create(Usuario usuario) {
        System.out.println("[HTTP LOG] POST /api/usuarios - Status: 201 CREATED - Body: username=" + usuario.getUsername());
        return usuarioService.create(usuario);
    }

    @Override
    public Usuario login(String username, String password) {
        System.out.println("[HTTP LOG] POST /api/login - Intentando autenticar usuario: " + username);
        Usuario user = usuarioService.login(username, password);
        
        if (user != null) {
            System.out.println("[HTTP LOG] POST /api/login - Status: 200 OK - Role: " + user.getRole());
        } else {
            System.out.println("[HTTP LOG] POST /api/login - Status: 401 UNAUTHORIZED");
        }
        
        return user;
    }
}

