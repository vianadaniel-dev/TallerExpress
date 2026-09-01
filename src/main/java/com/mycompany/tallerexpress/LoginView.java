/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress;

import com.mycompany.tallerexpress.config.controller.UsuarioController;
import com.mycompany.tallerexpress.model.Usuario;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView {

    private final UsuarioController usuarioController;

    public LoginView(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }

    /**
     * Muestra el cuadro de diálogo de inicio de sesión.
     * @return true si el usuario se autenticó con éxito, false si canceló o falló los intentos.
     */
    public boolean mostrarPantallaLogin() {
        int intentos = 0;
        final int MAX_INTENTOS = 3;

        while (intentos < MAX_INTENTOS) {
            JTextField txtUsername = new JTextField();
            JPasswordField txtPassword = new JPasswordField();

            Object[] formulario = {
                "Nombre de Usuario:", txtUsername,
                "Contraseña:", txtPassword
            };

            int opcion = JOptionPane.showConfirmDialog(
                null,
                formulario,
                "Iniciar Sesión - Taller Automotriz",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            // Si el usuario presiona Cancelar o cierra la ventana
            if (opcion != JOptionPane.OK_OPTION) {
                return false;
            }

            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());

            try {
                // Invoca al controlador que llama al servicio decorado y al repositorio
                Usuario usuarioAutenticado = usuarioController.login(username, password);

                if (usuarioAutenticado != null) {
                    JOptionPane.showMessageDialog(
                        null,
                        "¡Bienvenido, " + usuarioAutenticado.getUsername() + "!\n" +
                        "Rol: " + usuarioAutenticado.getRole(),
                        "Inicio de Sesión Exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    return true; // Autenticación correcta
                } else {
                    intentos++;
                    JOptionPane.showMessageDialog(
                        null,
                        "Usuario o contraseña incorrectos. Intentos restantes: " + (MAX_INTENTOS - intentos),
                        "Error de Autenticación",
                        JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Datos Incompletos",
                    JOptionPane.WARNING_MESSAGE
                );
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Error inesperado al conectar con el sistema: " + e.getMessage(),
                    "Error del Sistema",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }

        JOptionPane.showMessageDialog(
            null,
            "Ha superado el número máximo de intentos permitidos.",
            "Acceso Bloqueado",
            JOptionPane.WARNING_MESSAGE
        );
        return false;
    }
}