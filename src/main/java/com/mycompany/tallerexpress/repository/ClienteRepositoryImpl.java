
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.config.DataBaseConnection;
import com.mycompany.tallerexpress.exceptions.PlacaUnicaException;
import com.mycompany.tallerexpress.model.Cliente;
import com.mycompany.tallerexpress.model.ClienteVehiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ClienteRepositoryImpl implements ClienteRepository {
    
    @Override
    public Cliente guardarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, email) VALUES (?, ?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getEmail());
            statement.executeUpdate();

            // Recuperamos el ID autogenerado asignado por PostgreSQL
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setId(generatedKeys.getInt(1));
                }
            }

            return cliente;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el cliente", e);
        }
    }

    @Override
    public ClienteVehiculo registrarVehiculo(int clienteId, String placa) throws PlacaUnicaException {
        String sql = "INSERT INTO vehiculos (placa, cliente_id) VALUES (?, ?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, placa.toUpperCase().trim());
            statement.setInt(2, clienteId);
            statement.executeUpdate();

            ClienteVehiculo vehiculo = new ClienteVehiculo();
            vehiculo.setPlaca(placa.toUpperCase().trim());
            vehiculo.setClienteId(clienteId);

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehiculo.setId(generatedKeys.getInt(1));
                }
            }

            return vehiculo;

        } catch (SQLException e) {
            // Captura de violacion de la restricción UNIQUE en la columna placa (PostgreSQL 23505)
            if ("23505".equals(e.getSQLState())) {
                throw new PlacaUnicaException("La placa " + placa + " ya se encuentra registrada en el sistema.", e);
            }
            throw new RuntimeException("Error al registrar el vehículo", e);
        }
    }

    @Override
    public List<ClienteVehiculo> verHistorial(int clienteId) {
        List<ClienteVehiculo> historial = new ArrayList<>();
        String sql = "SELECT id, placa, cliente_id FROM vehiculos WHERE cliente_id = ? ORDER BY created DESC";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, clienteId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ClienteVehiculo vehiculo = new ClienteVehiculo();
                    vehiculo.setId(resultSet.getInt("id"));
                    vehiculo.setPlaca(resultSet.getString("placa"));
                    vehiculo.setClienteId(resultSet.getInt("cliente_id"));
                    historial.add(vehiculo);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar el historial de vehículos del cliente", e);
        }

        return historial;
    }
}
