
package com.mycompany.tallerexpress.model;


public class ClienteVehiculo extends Cliente {
    
    private String placa;

    public ClienteVehiculo() {
    }

    public ClienteVehiculo(String placa, int id, String nombre, String email) {
        super(id, nombre, email);
        setPlaca(placa);
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    
}
