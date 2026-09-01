
package com.mycompany.tallerexpress.model;


public class DetalleRepuestoOrden {
    private Repuesto repuesto;
    private int cantidad;
    private double precioUnitario;

    public DetalleRepuestoOrden(Repuesto repuesto, int cantidad, double precioUnitario) {
        this.repuesto = repuesto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

    // Getters y Setters...
    public Repuesto getRepuesto() { return repuesto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
}
