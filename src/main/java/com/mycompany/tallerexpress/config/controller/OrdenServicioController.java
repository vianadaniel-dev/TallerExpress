
package com.mycompany.tallerexpress.config.controller;

import com.mycompany.tallerexpress.model.OrdenServicio;
import com.mycompany.tallerexpress.service.OrdenServicioService;
import java.util.List;


public class OrdenServicioController {

    private final OrdenServicioService service;

    public OrdenServicioController(OrdenServicioService service) {
        this.service = service;
    }

    public OrdenServicio crearOrden(OrdenServicio orden) {
        return service.crearOrden(orden);
    }

    public boolean actualizarEstado(int ordenId, String nuevoEstado, String diagnostico) {
        return service.actualizarEstado(ordenId, nuevoEstado, diagnostico);
    }

    public List<OrdenServicio> consultarHistorial(int vehiculoId) {
        return service.consultarHistorialVehiculo(vehiculoId);
    }

    public double obtenerCostoTotal(int ordenId) {
        return service.calcularCostoTotal(ordenId);
    }
}
