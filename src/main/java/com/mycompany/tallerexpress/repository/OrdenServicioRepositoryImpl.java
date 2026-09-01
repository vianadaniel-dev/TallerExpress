
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.config.DataBaseConnection;
import com.mycompany.tallerexpress.model.DetalleRepuestoOrden;
import com.mycompany.tallerexpress.model.OrdenServicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrdenServicioRepositoryImpl implements OrdenServicioRepository {

    @Override
    public OrdenServicio guardar(OrdenServicio orden) {
        String sqlOrden = "INSERT INTO ordenes_servicio (cliente_id, vehiculo_id, mecanico, fecha_ingreso, descripcion_problema, estado) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlRepuesto = "INSERT INTO orden_repuestos (orden_id, repuesto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Transacción atómica

            try (PreparedStatement stmt = conn.prepareStatement(sqlOrden, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, orden.getClienteId());
                stmt.setInt(2, orden.getVehiculoId());
                stmt.setString(3, orden.getMecanico());
                stmt.setTimestamp(4, new Timestamp(orden.getFechaIngreso().getTime()));
                stmt.setString(5, orden.getDescripcionProblema());
                stmt.setString(6, orden.getEstado());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) orden.setId(rs.getInt(1));
                }
            }

            try (PreparedStatement stmtRep = conn.prepareStatement(sqlRepuesto)) {
                for (DetalleRepuestoOrden det : orden.getRepuestosUtilizados()) {
                    stmtRep.setInt(1, orden.getId());
                    stmtRep.setInt(2, det.getRepuesto().getId());
                    stmtRep.setInt(3, det.getCantidad());
                    stmtRep.setDouble(4, det.getPrecioUnitario());
                    stmtRep.addBatch();
                }
                stmtRep.executeBatch();
            }

            conn.commit();
            return orden;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la orden de servicio", e);
        }
    }

    @Override
    public boolean actualizarEstado(int ordenId, String nuevoEstado, String diagnostico) {
        String sql = "UPDATE ordenes_servicio SET estado = ?, diagnostico = ? WHERE id = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setString(2, diagnostico);
            stmt.setInt(3, ordenId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado de la orden", e);
        }
    }

    @Override
    public List<OrdenServicio> consultarHistorialPorVehiculo(int vehiculoId) {
        List<OrdenServicio> historial = new ArrayList<>();
        String sql = "SELECT * FROM ordenes_servicio WHERE vehiculo_id = ? ORDER BY fecha_ingreso DESC";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehiculoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrdenServicio orden = new OrdenServicio();
                    orden.setId(rs.getInt("id"));
                    orden.setClienteId(rs.getInt("cliente_id"));
                    orden.setVehiculoId(rs.getInt("vehiculo_id"));
                    orden.setMecanico(rs.getString("mecanico"));
                    orden.setFechaIngreso(rs.getTimestamp("fecha_ingreso"));
                    orden.setDescripcionProblema(rs.getString("descripcion_problema"));
                    orden.setDiagnostico(rs.getString("diagnostico"));
                    orden.setEstado(rs.getString("estado"));
                    historial.add(orden);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar historial del vehículo", e);
        }
        return historial;
    }

    @Override
    public OrdenServicio buscarPorId(int ordenId) {
        // Implementación directa o búsqueda JDBC por ID
        return null; 
    }
}