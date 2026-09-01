
package com.mycompany.tallerexpress.repository;



import com.mycompany.tallerexpress.config.DataBaseConnection;
import com.mycompany.tallerexpress.model.Role;
import com.mycompany.tallerexpress.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class UsuarioRepositoryImpl implements UsuarioRepository {

    @Override
    public Usuario guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, role, estado, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, usuario.getUsername());
            statement.setString(2, usuario.getPassword());
            statement.setString(3, usuario.getRole().name()); // Guarda el valor String del Enum (ADMIN / RECEPCIONISTA)
            statement.setString(4, usuario.getEstado());
            java.util.Date created = usuario.getCreatedAt();
            java.sql.Timestamp ts = created != null ? new Timestamp(created.getTime()) : new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(5, ts);

            statement.executeUpdate();

            // Asignar el ID autogenerado por PostgreSQL al objeto
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                }
            }

            return usuario;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el usuario en la base de datos", e);
        }
    }

    @Override
    public Usuario autenticar(String username, String password) {
        String sql = "SELECT id, username, password, role, estado, created_at FROM usuarios "
                   + "WHERE username = ? AND password = ? AND estado = 'ACTIVO'";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearUsuario(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al autenticar el usuario", e);
        }

        return null; // Credenciales inválidas o usuario inactivo
    }

    // Método privado para evitar duplicar mapeo de ResultSet
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPassword(rs.getString("password"));
        usuario.setRole(Role.valueOf(rs.getString("role")));
        usuario.setEstado(rs.getString("estado"));
        usuario.setCreatedAt(rs.getTimestamp("created_at"));
        return usuario;
    }
}
