
package com.mycompany.tallerexpress.service;

import com.mycompany.tallerexpress.model.OrdenServicio;
import java.util.List;

public interface OrdenServicioService {
    OrdenServicio crearOrden(OrdenServicio orden);
    boolean actualizarEstado(int ordenId, String nuevoEstado, String diagnostico);
    List<OrdenServicio> consultarHistorialVehiculo(int vehiculoId);
    double calcularCostoTotal(int ordenId);
}
