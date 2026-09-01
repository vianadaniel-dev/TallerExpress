
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.model.OrdenServicio;
import java.util.List;


public interface OrdenServicioRepository {
    OrdenServicio guardar(OrdenServicio orden);
    boolean actualizarEstado(int ordenId, String nuevoEstado, String diagnostico);
    List<OrdenServicio> consultarHistorialPorVehiculo(int vehiculoId);
    OrdenServicio buscarPorId(int ordenId);
}