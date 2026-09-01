package com.mycompany.tallerexpress.view;


import com.mycompany.tallerexpress.config.controller.OrdenServicioController;
import com.mycompany.tallerexpress.config.controller.RepuestoController;
import com.mycompany.tallerexpress.model.DetalleRepuestoOrden;
import com.mycompany.tallerexpress.model.OrdenServicio;
import com.mycompany.tallerexpress.model.Repuesto;
import java.awt.HeadlessException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdenServicioView {

    private final OrdenServicioController ordenController;
    private final RepuestoController repuestoController;

    public OrdenServicioView(OrdenServicioController ordenController, RepuestoController repuestoController) {
        this.ordenController = ordenController;
        this.repuestoController = repuestoController;
    }

    public void mostrarMenuOrdenes() {
        String[] opciones = {
            "1. Registrar Nueva Orden",
            "2. Consultar Costo Total de Orden",
            "3. Salir"
        };

        boolean continuar = true;
        while (continuar) {
            String seleccion = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione una operación para Órdenes de Servicio:",
                "Módulo de Órdenes de Servicio",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );

            if (seleccion == null || seleccion.contains("Salir")) {
                continuar = false;
            } else if (seleccion.startsWith("1")) {
                registrarOrdenFlujo();
            } else if (seleccion.startsWith("2")) {
                consultarCostoFlujo();
            }
        }
    }

    // =========================================================================
    // 1. FLUJO PARA REGISTRAR LA ORDEN DE SERVICIO Y SUS REPUESTOS
    // =========================================================================
    private void registrarOrdenFlujo() {
        try {
            // Pasos iniciales: Datos Básicos
            JTextField txtClienteId = new JTextField();
            JTextField txtVehiculoId = new JTextField();
            JTextField txtMecanico = new JTextField();
            JTextArea txtDescripcion = new JTextArea(3, 20);

            Object[] formularioDatos = {
                "ID Cliente:", txtClienteId,
                "ID Vehículo:", txtVehiculoId,
                "Mecánico Responsable:", txtMecanico,
                "Descripción del Problema:", new JScrollPane(txtDescripcion)
            };

            int confirmacion = JOptionPane.showConfirmDialog(
                null, 
                formularioDatos, 
                "Paso 1: Datos Generales de la Orden", 
                JOptionPane.OK_CANCEL_OPTION
            );

            if (confirmacion != JOptionPane.OK_OPTION) return;

            int clienteId = Integer.parseInt(txtClienteId.getText().trim());
            int vehiculoId = Integer.parseInt(txtVehiculoId.getText().trim());
            String mecanico = txtMecanico.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            // Cargar repuestos desde el Controller para el selector
            List<Repuesto> listaRepuestos = repuestoController.listar();
            if (listaRepuestos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay repuestos registrados en el inventario.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Seleccionar Repuestos
            List<DetalleRepuestoOrden> repuestosDetalle = new ArrayList<>();
            boolean agregarMasRepuestos = true;

            while (agregarMasRepuestos) {
                JComboBox<Repuesto> comboRepuestos = new JComboBox<>(listaRepuestos.toArray(new Repuesto[0]));
                JTextField txtCantidad = new JTextField("1");

                Object[] formularioRepuesto = {
                    "Seleccione Repuesto:", comboRepuestos,
                    "Cantidad a utilizar:", txtCantidad
                };

                int respRepuesto = JOptionPane.showConfirmDialog(
                    null, 
                    formularioRepuesto, 
                    "Paso 2: Agregar Repuestos a la Orden", 
                    JOptionPane.OK_CANCEL_OPTION
                );

                if (respRepuesto == JOptionPane.OK_OPTION) {
                    Repuesto repuestoSeleccionado = (Repuesto) comboRepuestos.getSelectedItem();
                    int cantidad = Integer.parseInt(txtCantidad.getText().trim());

                    if (cantidad <= 0) {
                        JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    // Construimos el detalle utilizando el precio del repuesto
                    DetalleRepuestoOrden detalle = new DetalleRepuestoOrden(
                        repuestoSeleccionado, 
                        cantidad, 
                        repuestoSeleccionado.getPrecioUnitario()
                    );
                    
                    repuestosDetalle.add(detalle);

                    int otraVez = JOptionPane.showConfirmDialog(
                        null, 
                        "¿Desea agregar otro repuesto a esta orden?", 
                        "Continuar", 
                        JOptionPane.YES_NO_OPTION
                    );
                    agregarMasRepuestos = (otraVez == JOptionPane.YES_OPTION);
                } else {
                    agregarMasRepuestos = false;
                }
            }

            // Construir el objeto OrdenServicio y enviarlo al Controller
            OrdenServicio nuevaOrden = new OrdenServicio();
            nuevaOrden.setClienteId(clienteId);
            nuevaOrden.setVehiculoId(vehiculoId);
            nuevaOrden.setMecanico(mecanico);
            nuevaOrden.setDescripcionProblema(descripcion);
            nuevaOrden.setFechaIngreso(new Date());
            nuevaOrden.setEstado("PENDIENTE");
            nuevaOrden.setRepuestosUtilizados(repuestosDetalle);

            OrdenServicio ordenCreada = ordenController.crearOrden(nuevaOrden);

            JOptionPane.showMessageDialog(
                null, 
                "¡Orden de Servicio #" + ordenCreada.getId() + " registrada exitosamente!\n" +
                "Total acumulado en repuestos: $" + String.format("%.2f", ordenCreada.calcularCostoTotal()),
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Debe ingresar valores numéricos válidos en los campos de ID y Cantidad.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error al crear la orden: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // 2. FLUJO PARA CONSULTAR EL COSTO TOTAL
    // =========================================================================
    private void consultarCostoFlujo() {
        String inputId = JOptionPane.showInputDialog(null, "Ingrese el ID de la Orden de Servicio:", "Consultar Costo", JOptionPane.QUESTION_MESSAGE);
        
        if (inputId != null && !inputId.trim().isEmpty()) {
            try {
                int ordenId = Integer.parseInt(inputId.trim());
                double costoTotal = ordenController.obtenerCostoTotal(ordenId);

                JOptionPane.showMessageDialog(
                    null, 
                    "El costo total de la reparación para la Orden #" + ordenId + " es: $" + String.format("%.2f", costoTotal),
                    "Costo Total", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El ID de la orden debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(null, "Error al consultar la orden: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}