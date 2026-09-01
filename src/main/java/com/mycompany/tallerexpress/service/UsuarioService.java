
package com.mycompany.tallerexpress.service;

import com.mycompany.tallerexpress.model.Usuario;

    public interface UsuarioService {
    Usuario create(Usuario usuario);
    Usuario login(String username, String password);
}

