/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.service;

import com.mycompany.tallerexpress.exceptions.ClienteActivoException;
import com.mycompany.tallerexpress.exceptions.PlacaUnicaException;
import com.mycompany.tallerexpress.exceptions.VehiculoRegistradoException;
import com.mycompany.tallerexpress.model.Cliente;
import com.mycompany.tallerexpress.model.ClienteVehiculo;
import com.mycompany.tallerexpress.repository.ClienteRepository;
import java.util.List;

public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente registrarCliente(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
        if (cliente.getEmail() == null || !cliente.getEmail().contains("@")) {
            throw new IllegalArgumentException("El correo electrónico no es válido.");
        }
        return clienteRepository.guardarCliente(cliente);
    }

@Override
public ClienteVehiculo asociarVehiculo(int clienteId, String placa) throws PlacaUnicaException, VehiculoRegistradoException {
    // Validar si la placa ya existe en el historial del sistema antes de llamar a la DB
    List<ClienteVehiculo> existentes = clienteRepository.verHistorial(clienteId);
    for (ClienteVehiculo v : existentes) {
        if (v.getPlaca().equalsIgnoreCase(placa)) {
            throw new VehiculoRegistradoException("El vehículo con placa " + placa + " ya está registrado para este cliente.");
        }
    }
    return clienteRepository.registrarVehiculo(clienteId, placa);
}

    @Override
    public boolean desactivarCliente(int clienteId) throws ClienteActivoException {
    // Si el cliente tiene vehículos asociados, no se puede inactivar
    List<ClienteVehiculo> vehiculos = clienteRepository.verHistorial(clienteId);
    if (!vehiculos.isEmpty()) {
        throw new ClienteActivoException("No se puede desactivar el cliente porque tiene vehículos activos registrados.");
    }
    // Lógica para actualizar estado a inactivo...
    return true;
}

    @Override
    public List<ClienteVehiculo> consultarHistorialVehiculos(int clienteId) {
        if (clienteId <= 0) {
            throw new IllegalArgumentException("ID de cliente inválido.");
        }
        return clienteRepository.verHistorial(clienteId);
    }
}
