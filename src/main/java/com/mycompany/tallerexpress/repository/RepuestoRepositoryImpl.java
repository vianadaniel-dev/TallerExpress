
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.config.DataBaseConnection;
import com.mycompany.tallerexpress.exceptions.CodigoRepuestoUnicoException;
import com.mycompany.tallerexpress.model.Repuesto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RepuestoRepositoryImpl implements RepuestoRepository{

    @Override
public Repuesto guardar(Repuesto repuesto) throws CodigoRepuestoUnicoException {
    String sql = """
                 INSERT INTO repuestos (
                     codigo_referencia, nombre, categoria, proveedor, 
                     stock_total, stock_disponible, precio_unitario, activo, created
                 ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);""";

    try (Connection connection = DataBaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, repuesto.getCodigoReferencia());
statement.setString(2, repuesto.getNombre());
statement.setString(3, repuesto.getCategoria());
statement.setString(4, repuesto.getProveedor());
statement.setInt(5, repuesto.getStockTotal());       // Nuevo parámetro
statement.setInt(6, repuesto.getStockDisponible());  // Parámetro existente
statement.setDouble(7, repuesto.getPrecioUnitario());
statement.setBoolean(8, repuesto.isActivo());
            java.util.Date created = repuesto.getCreatedAt();
            java.sql.Timestamp ts = created != null ? new java.sql.Timestamp(created.getTime()) : new java.sql.Timestamp(System.currentTimeMillis());
            statement.setTimestamp(9, ts);

        statement.executeUpdate();
        return repuesto;

    } catch (SQLException e) {
        // Código SQLState 23505 o código de error 1062 en MySQL para violación de restricción UNIQUE
        if ("23505".equals(e.getSQLState()) || e.getErrorCode() == 1062) {
            throw new CodigoRepuestoUnicoException("El código de referencia " + repuesto.getCodigoReferencia() + " ya está registrado.", e);
        }
        throw new RuntimeException("Error al guardar el repuesto", e);
    }
}

    @Override
    public List<Repuesto> listar() {
        List<Repuesto> repuestos = new ArrayList<>();
        String sql = "SELECT * FROM repuestos ORDER BY nombre ASC";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                repuestos.add(mapearRepuesto(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los repuestos", e);
        }

        return repuestos;
    }
    
    @Override
    public List<Repuesto> listarPorCategoria(String categoria) {
        List<Repuesto> repuestos = new ArrayList<>();
        String sql = "SELECT * FROM repuestos WHERE LOWER(categoria) = LOWER(?) ORDER BY nombre ASC";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoria);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    repuestos.add(mapearRepuesto(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al filtrar repuestos por categoría", e);
        }

        return repuestos;
    }

    @Override
public List<Repuesto> listarPorProveedor(String proveedor) {
    List<Repuesto> repuestos = new ArrayList<>();
    String sql = "SELECT * FROM repuestos WHERE LOWER(proveedor) = LOWER(?) ORDER BY nombre ASC";

    try (Connection connection = DataBaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, proveedor);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                // Reutilizamos el método auxiliar privado de mapeo
                repuestos.add(mapearRepuesto(resultSet));
            }
        }

    } catch (SQLException e) {
        throw new RuntimeException("Error al filtrar repuestos por proveedor: " + proveedor, e);
    }

    return repuestos;
}

    
    // =========================================================================
    // MÉTODO AUXILIAR DE MAPEO (Privado y exclusivo de esta implementación DAO)
    // =========================================================================
    private Repuesto mapearRepuesto(ResultSet rs) throws SQLException {
        Repuesto repuesto = new Repuesto();

        repuesto.setId(rs.getInt("id"));
        repuesto.setCodigoReferencia(rs.getInt("codigo_referencia"));
        repuesto.setNombre(rs.getString("nombre"));
        repuesto.setCategoria(rs.getString("categoria"));
        repuesto.setProveedor(rs.getString("proveedor"));
        repuesto.setStockTotal(rs.getInt("stock_total"));
        repuesto.setStockDisponible(rs.getInt("stock_disponible"));
        repuesto.setPrecioUnitario(rs.getDouble("precio_unitario"));
        repuesto.setActivo(rs.getBoolean("activo"));
        repuesto.setCreatedAt(rs.getTimestamp("created"));

        return repuesto;
    }
    
    @Override
public boolean actualizar(Repuesto repuesto) throws CodigoRepuestoUnicoException {
    String sql = "UPDATE repuestos SET "
               + "codigo_referencia = ?, "
               + "nombre = ?, "
               + "categoria = ?, "
               + "proveedor = ?, "
               + "stock_total = ?, "
               + "stock_disponible = ?, "
               + "precio_unitario = ?, "
               + "activo = ? "
               + "WHERE id = ?";

    try (Connection connection = DataBaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, repuesto.getCodigoReferencia());
        statement.setString(2, repuesto.getNombre());
        statement.setString(3, repuesto.getCategoria());
        statement.setString(4, repuesto.getProveedor());
        statement.setInt(5, repuesto.getStockTotal());
        statement.setInt(6, repuesto.getStockDisponible());
        statement.setDouble(7, repuesto.getPrecioUnitario());
        statement.setBoolean(8, repuesto.isActivo());
        
        // Identificador para encontrar la fila a actualizar
        statement.setInt(9, repuesto.getId());

        int filasAfectadas = statement.executeUpdate();
        return filasAfectadas > 0;

    } catch (SQLException e) {
        // En caso de que se intente cambiar el codigo_referencia a uno que ya pertenece a otro producto
        if ("23505".equals(e.getSQLState())) {
            throw new CodigoRepuestoUnicoException(
                "El código de referencia " + repuesto.getCodigoReferencia() + " ya pertenece a otro repuesto.", e
            );
        }
        throw new RuntimeException("Error al actualizar el repuesto con ID " + repuesto.getId(), e);
    }
}
        
       

    
    
}
