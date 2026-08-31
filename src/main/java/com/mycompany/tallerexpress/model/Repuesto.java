package com.mycompany.tallerexpress.model;

import java.util.Date;

public class Repuesto {

    private String codigoReferencia;
    private String nombre;
    private String categoria;
    private String proveedor;
    private int stockDisponible;
    private double precioUnitario;
    private boolean Activo;
    private Date created;

    public Repuesto() {
    }

    public Repuesto(String codigoReferencia, String nombre, String categoria, String proveedor, int stockDisponible, double precioUnitario, boolean Activo, Date created) {
        setCodigoReferencia(codigoReferencia);
        setNombre(nombre);
        setCategoria(categoria);
        setProveedor(proveedor);
        setStockDisponible(stockDisponible);
        setPrecioUnitario(precioUnitario);
        setActivo(Activo);
        setCreated(created);
    }

    public String getCodigoReferencia() {
        return codigoReferencia;
    }

    public void setCodigoReferencia(String codigoReferencia) {
        this.codigoReferencia = codigoReferencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {

        this.stockDisponible = stockDisponible;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public boolean isActivo() {
        return Activo;
    }

    public void setActivo(boolean Activo) {
        this.Activo = Activo;
    }

    public Date createdAt() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int stockTotal() {
        return stockDisponible;
    }

    public void mostrarInformacion() {
        System.out.println("Hola soy to repuesto.");
        System.out.println("Codigo: " + getCodigoReferencia());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Precio Base: " + getPrecioUnitario());
        System.out.println("Cantidad Disponible " + getStockDisponible());
        System.out.println("Activo: " + isActivo());
    }
    
    

}
