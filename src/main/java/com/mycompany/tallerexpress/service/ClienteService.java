
package com.mycompany.tallerexpress.service;

import com.mycompany.tallerexpress.exceptions.ClienteActivoException;
import com.mycompany.tallerexpress.exceptions.PlacaUnicaException;
import com.mycompany.tallerexpress.model.Cliente;
import com.mycompany.tallerexpress.model.ClienteVehiculo;
import java.util.List;


public interface ClienteService {
    Cliente registrarCliente(Cliente cliente);
    ClienteVehiculo asociarVehiculo(int clienteId, String placa) throws PlacaUnicaException;
    List<ClienteVehiculo> consultarHistorialVehiculos(int clienteId);
    
    boolean desactivarCliente(int clienteId) throws ClienteActivoException;
}