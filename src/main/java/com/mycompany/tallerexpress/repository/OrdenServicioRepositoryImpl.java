
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.config.DataBaseConnection;
import com.mycompany.tallerexpress.model.DetalleRepuestoOrden;
import com.mycompany.tallerexpress.model.Repuesto;
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

            try {
                try (PreparedStatement stmt = conn.prepareStatement(sqlOrden, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, orden.getClienteId());
                    stmt.setInt(2, orden.getVehiculoId());
                    stmt.setString(3, orden.getMecanico());
                    java.util.Date fecha = orden.getFechaIngreso();
                    Timestamp ts = (fecha != null) ? new Timestamp(fecha.getTime()) : new Timestamp(System.currentTimeMillis());
                    stmt.setTimestamp(4, ts);
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
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // ignore rollback failure
                }
                throw new RuntimeException("Error al guardar la orden de servicio", e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    // ignore
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conexión para guardar la orden de servicio", e);
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
        String sqlOrden = "SELECT * FROM ordenes_servicio WHERE id = ?";
        String sqlRepuestos = "SELECT orr.repuesto_id, orr.cantidad, orr.precio_unitario AS precio_ord, "
                            + "r.id AS r_id, r.codigo_referencia, r.nombre, r.categoria, r.proveedor, r.stock_total, r.stock_disponible, r.precio_unitario AS rep_precio, r.activo, r.created "
                            + "FROM orden_repuestos orr LEFT JOIN repuestos r ON orr.repuesto_id = r.id WHERE orr.orden_id = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sqlOrden)) {
                stmt.setInt(1, ordenId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) return null;
                    OrdenServicio orden = new OrdenServicio();
                    orden.setId(rs.getInt("id"));
                    orden.setClienteId(rs.getInt("cliente_id"));
                    orden.setVehiculoId(rs.getInt("vehiculo_id"));
                    orden.setMecanico(rs.getString("mecanico"));
                    orden.setFechaIngreso(rs.getTimestamp("fecha_ingreso"));
                    orden.setDescripcionProblema(rs.getString("descripcion_problema"));
                    orden.setDiagnostico(rs.getString("diagnostico"));
                    orden.setEstado(rs.getString("estado"));

                    // Cargar repuestos asociados
                    try (PreparedStatement stmtRep = conn.prepareStatement(sqlRepuestos)) {
                        stmtRep.setInt(1, ordenId);
                        try (ResultSet rrs = stmtRep.executeQuery()) {
                            while (rrs.next()) {
                                Repuesto rep = new Repuesto();
                                int rId = rrs.getInt("r_id");
                                if (rId > 0) {
                                    rep.setId(rId);
                                    rep.setCodigoReferencia(rrs.getInt("codigo_referencia"));
                                    rep.setNombre(rrs.getString("nombre"));
                                    rep.setCategoria(rrs.getString("categoria"));
                                    rep.setProveedor(rrs.getString("proveedor"));
                                    rep.setStockTotal(rrs.getInt("stock_total"));
                                    rep.setStockDisponible(rrs.getInt("stock_disponible"));
                                    rep.setPrecioUnitario(rrs.getDouble("rep_precio"));
                                    rep.setActivo(rrs.getBoolean("activo"));
                                    rep.setCreatedAt(rrs.getTimestamp("created"));
                                }
                                DetalleRepuestoOrden det = new DetalleRepuestoOrden(rep, rrs.getInt("cantidad"), rrs.getDouble("precio_ord"));
                                orden.getRepuestosUtilizados().add(det);
                            }
                        }
                    }

                    return orden;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la orden por id", e);
        }
    }
}