package org.example.app;

import org.example.Presentation.Controllers.LoginController;
import org.example.Presentation.Views.LoginView;
import org.example.Services.AuthService;

import javax.swing.*;

/**
 * Clase principal de la aplicación
 * Sistema de Gestión Médica
 *
 * @version 1.0
 * @author David González, Dany Montero, Emmanuel Núñez
 */
public class Main {

    // Configuración del servidor
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 7000;

    public static void main(String[] args) {
        // Configure Look and Feel
        configureLookAndFeel();

        // Run application on EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(Main::initializeApplication);
    }

    private static void initializeApplication() {
        try {
            // Show splash screen or loading indicator (optional)
            System.out.println("Iniciando Sistema de Gestión Médica...");
            System.out.println("Conectando a servidor: " + SERVER_HOST + ":" + SERVER_PORT);

            // Initialize services
            AuthService authService = new AuthService(SERVER_HOST, SERVER_PORT);

            // Create and show login view
            LoginView loginView = new LoginView();

            // Create controller and wire up
            LoginController loginController = new LoginController(loginView, authService);

            System.out.println("Sistema iniciado correctamente");

        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error al iniciar la aplicación.\n" +
                            "Asegúrese de que el servidor esté ejecutándose en " + SERVER_HOST + ":" + SERVER_PORT,
                    "Error de Inicialización",
                    JOptionPane.ERROR_MESSAGE
            );

            System.exit(1);
        }
    }

    private static void configureLookAndFeel() {
        try {
            // Try to use system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            /*
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            */

        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
            // Continue with default LAF
        }
    }
}