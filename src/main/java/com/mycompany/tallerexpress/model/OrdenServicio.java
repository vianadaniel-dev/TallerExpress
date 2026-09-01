
package com.mycompany.tallerexpress.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdenServicio {
    private int id;
    private int clienteId;
    private int vehiculoId;
    private String mecanico;
    private Date fechaIngreso;
    private String descripcionProblema;
    private String diagnostico;
    private String estado; // PENDIENTE, EN_PROCESO, COMPLETADO, CANCELADO
    private List<DetalleRepuestoOrden> repuestosUtilizados = new ArrayList<>();

    // Método para calcular el costo total de la reparación
    public double calcularCostoTotal() {
        double total = 0;
        for (DetalleRepuestoOrden detalle : repuestosUtilizados) {
            total += detalle.getSubtotal();
        }
        return total;
    }

    // Getters y Setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public int getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(int vehiculoId) { this.vehiculoId = vehiculoId; }
    public String getMecanico() { return mecanico; }
    public void setMecanico(String mecanico) { this.mecanico = mecanico; }
    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public String getDescripcionProblema() { return descripcionProblema; }
    public void setDescripcionProblema(String descripcionProblema) { this.descripcionProblema = descripcionProblema; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<DetalleRepuestoOrden> getRepuestosUtilizados() { return repuestosUtilizados; }
    public void setRepuestosUtilizados(List<DetalleRepuestoOrden> repuestosUtilizados) { this.repuestosUtilizados = repuestosUtilizados; }
}