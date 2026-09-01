
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.exceptions.PlacaUnicaException;
import com.mycompany.tallerexpress.model.Cliente;
import com.mycompany.tallerexpress.model.ClienteVehiculo;
import java.util.List;


public interface ClienteRepository {
    
    // Guarda el cliente
    Cliente guardarCliente(Cliente cliente);
    
    // Registra un vehículo asociado a un cliente especifico
    ClienteVehiculo registrarVehiculo(int clienteId, String placa);
    
    // Consulta los vehículos de un cliente
    List<ClienteVehiculo> verHistorial(int clienteId);
}