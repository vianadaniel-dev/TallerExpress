package com.mycompany.tallerexpress.model;

import com.mycompany.tallerexpress.exceptions.StockMayorIgualCeroException;
import java.util.Date;

public class Repuesto {
    private int id;
    private int codigoReferencia;
    private String nombre;
    private String categoria;
    private String proveedor;
    private int stockTotal;
    private int stockDisponible;
    private double precioUnitario;
    private boolean activo;
    private Date created;

    public Repuesto() {
    }

    public Repuesto(int codigoReferencia, String nombre, String categoria, String proveedor, int stockTotal, int stockDisponible, double precioUnitario, boolean Activo, Date created) {
        setCodigoReferencia(codigoReferencia);
        setNombre(nombre);
        setCategoria(categoria);
        setProveedor(proveedor);
        setStockTotal(stockTotal);
        setStockDisponible(stockDisponible);
        setPrecioUnitario(precioUnitario);
        setActivo(Activo);
        setCreatedAt(created);
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getCodigoReferencia() {
        return codigoReferencia;
    }

    public void setCodigoReferencia(int codigoReferencia) {
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
    
    
    
    public int getStockTotal() {
        return stockTotal;
    }
    
    public void setStockTotal(int stockTotal) {
        if (stockTotal < 0) {
            throw new IllegalArgumentException("El stock total no puede ser negativo.");
        }
        if (stockTotal < this.stockDisponible) {
            throw new IllegalArgumentException("El stock total no puede ser menor al stock disponible actual.");
        }
        this.stockTotal = stockTotal;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    // Registrar o actualizar el stock disponible
    public void setStockDisponible(int stockDisponible) {
        if (stockDisponible < 0) {
            throw new StockMayorIgualCeroException("El stock disponible no puede ser negativo.");
        }
        if (this.stockTotal > 0 && stockDisponible > this.stockTotal) {
            throw new IllegalArgumentException("El stock disponible no puede ser mayor al stock total.");
        }
        this.stockDisponible = stockDisponible;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean Activo) {
        this.activo = Activo;
    }

    public Date getCreatedAt() {
        return created;
    }

    public void setCreatedAt(Date created) {
        this.created = created;
    }

   

    public void mostrarInformacion() {
        System.out.println("Repuesto{codigo=" + getCodigoReferencia() + ", nombre=" + getNombre() + ", precio=" + getPrecioUnitario() + ", disponible=" + getStockDisponible() + ", activo=" + isActivo() + "}");
    }

}
