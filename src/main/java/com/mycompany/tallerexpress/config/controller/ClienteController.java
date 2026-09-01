
package com.mycompany.tallerexpress.config.controller;

import com.mycompany.tallerexpress.exceptions.ClienteActivoException;
import com.mycompany.tallerexpress.exceptions.PlacaUnicaException;
import com.mycompany.tallerexpress.model.Cliente;
import com.mycompany.tallerexpress.model.ClienteVehiculo;
import com.mycompany.tallerexpress.service.ClienteService;
import java.util.List;

public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public Cliente registrarCliente(String nombre, String email) {
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setEmail(email);
        return clienteService.registrarCliente(cliente);
    }

    public ClienteVehiculo asociarVehiculo(int clienteId, String placa) throws PlacaUnicaException {
        return clienteService.asociarVehiculo(clienteId, placa);
    }

    public List<ClienteVehiculo> verHistorial(int clienteId) {
        return clienteService.consultarHistorialVehiculos(clienteId);
    }
    
    public boolean desactivarCliente(int clienteId) throws ClienteActivoException {
    return clienteService.desactivarCliente(clienteId);
}
}