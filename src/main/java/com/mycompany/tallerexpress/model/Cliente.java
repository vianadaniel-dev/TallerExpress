
package com.mycompany.tallerexpress.model;

import java.util.ArrayList;
import java.util.List;


// Entidad Cliente
public class Cliente {
    private int id;
    private String nombre;
    private String email;
    private List<ClienteVehiculo> vehiculos = new ArrayList<>();

    public Cliente() {
    }

    public Cliente(int id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public Cliente(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

   
    
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<ClienteVehiculo> getVehiculos() { return vehiculos; }
    public void setVehiculos(List<ClienteVehiculo> vehiculos) { this.vehiculos = vehiculos; }
}
    

