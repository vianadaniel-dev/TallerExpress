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
    private boolean Activo;
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
        setCreated(created);
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
        return stockDisponible;
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
        if (stockDisponible > this.stockTotal) {
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

   

    public void mostrarInformacion() {
        System.out.println("Hola soy to repuesto.");
        System.out.println("Codigo: " + getCodigoReferencia());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Precio Base: " + getPrecioUnitario());
        System.out.println("Cantidad Disponible " + getStockDisponible());
        System.out.println("Activo: " + isActivo());
    }

}
