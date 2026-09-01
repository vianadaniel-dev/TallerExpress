
package com.mycompany.tallerexpress.service;

import com.mycompany.tallerexpress.model.OrdenServicio;
import com.mycompany.tallerexpress.repository.OrdenServicioRepository;
import java.util.Date;
import java.util.List;

public class OrdenServicioServiceImpl implements OrdenServicioService {

    private final OrdenServicioRepository repository;

    public OrdenServicioServiceImpl(OrdenServicioRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrdenServicio crearOrden(OrdenServicio orden) {
        if (orden.getClienteId() <= 0 || orden.getVehiculoId() <= 0) {
            throw new IllegalArgumentException("Cliente y Vehículo son obligatorios.");
        }
        if (orden.getMecanico() == null || orden.getMecanico().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe asignar un mecánico responsable.");
        }
        if (orden.getFechaIngreso() == null) {
            orden.setFechaIngreso(new Date());
        }
        if (orden.getEstado() == null) {
            orden.setEstado("PENDIENTE");
        }
        return repository.guardar(orden);
    }

    @Override
    public boolean actualizarEstado(int ordenId, String nuevoEstado, String diagnostico) {
        return repository.actualizarEstado(ordenId, nuevoEstado, diagnostico);
    }

    @Override
    public List<OrdenServicio> consultarHistorialVehiculo(int vehiculoId) {
        return repository.consultarHistorialPorVehiculo(vehiculoId);
    }

    @Override
    public double calcularCostoTotal(int ordenId) {
        OrdenServicio orden = repository.buscarPorId(ordenId);
        return (orden != null) ? orden.calcularCostoTotal() : 0.0;
    }
}