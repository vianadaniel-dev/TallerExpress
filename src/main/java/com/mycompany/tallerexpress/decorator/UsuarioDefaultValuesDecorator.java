
package com.mycompany.tallerexpress.decorator;

import com.mycompany.tallerexpress.model.Role;
import com.mycompany.tallerexpress.model.Usuario;
import com.mycompany.tallerexpress.service.UsuarioService;
import java.util.Date;


public class UsuarioDefaultValuesDecorator implements UsuarioService {

    private final UsuarioService usuarioService;

    public UsuarioDefaultValuesDecorator(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Usuario create(Usuario usuario) {
        // Asignación de propiedades por defecto según requerimiento
        if (usuario.getRole() == null) {
            usuario.setRole(Role.RECEPCIONISTA);
        }
        if (usuario.getEstado() == null) {
            usuario.setEstado("ACTIVO");
        }
        if (usuario.getCreatedAt() == null) {
            usuario.setCreatedAt(new Date());
        }

        // Ejecuta la lógica base del servicio wrapped
        return usuarioService.create(usuario);
    }

    @Override
    public Usuario login(String username, String password) {
        return usuarioService.login(username, password);
    }
}

