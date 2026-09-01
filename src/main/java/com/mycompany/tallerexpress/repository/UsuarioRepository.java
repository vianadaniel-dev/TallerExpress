
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.model.Usuario;

 public interface UsuarioRepository {

    // Guarda o crea un nuevo usuario en la DB
    Usuario guardar(Usuario usuario);

    // Autentica credenciales y retorna el usuario con su Rol si es correcto
    Usuario autenticar(String username, String password);
}