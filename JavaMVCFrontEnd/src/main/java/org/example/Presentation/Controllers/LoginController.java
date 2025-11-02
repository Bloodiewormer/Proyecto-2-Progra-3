package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Auth.UserResponseDto;
import org.example.Presentation.Observable;
import org.example.Presentation.Views.CambioClaveView;
import org.example.Presentation.Views.LoginView;
import org.example.Presentation.Views.MenuPrincipalView;
import org.example.Services.AuthService;
import org.example.Services.MedicamentoService;
import org.example.Services.PacienteService;
import org.example.Services.UsuarioService;
import org.example.Utilities.ChangeType;

import javax.swing.*;

public class LoginController extends Observable {

    private final LoginView loginView;
    private final AuthService authService;
    private UserResponseDto currentUser;

    // Server configuration
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 7000;

    public LoginController(LoginView loginView, AuthService authService) {
        this.loginView = loginView;
        this.authService = authService;

        addObserver(loginView);
        wireEvents();
    }

    private void wireEvents() {
        loginView.addLoginListener(e -> handleLogin());
        loginView.setPasswordChangeCallback(this::showPasswordChangeView);
    }

    private void handleLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        // Validate input
        if (!validateInput(username, password)) {
            return;
        }

        loginView.showLoading(true);

        SwingWorker<UserResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected UserResponseDto doInBackground() throws Exception {
                return authService.login(username, password).get();
            }

            @Override
            protected void done() {
                loginView.showLoading(false);
                try {
                    UserResponseDto user = get();
                    if (user != null) {
                        currentUser = user;
                        onLoginSuccess(user);
                    } else {
                        showError("Credenciales inválidas. Por favor, verifique su usuario y contraseña.");
                    }
                } catch (Exception ex) {
                    handleError("Error al intentar iniciar sesión", ex);
                }
            }
        };
        worker.execute();
    }

    private void onLoginSuccess(UserResponseDto user) {
        // Notify observers
        notifyObservers(ChangeType.UPDATED, user);

        // Hide login view
        loginView.setVisible(false);

        // Determine user type and open main view
        UserType userType = determineUserType(user.getRole());
        openMainView(userType, user);
    }

    private UserType determineUserType(String role) {
        if (role == null) return UserType.NULL;

        return switch (role.toUpperCase()) {
            case "ADMIN", "ADMINISTRADOR" -> UserType.ADMINISTRADOR;
            case "FARMACEUTA" -> UserType.FARMACEUTA;
            case "MEDICO", "DOCTOR" -> UserType.MEDICO;
            default -> UserType.NULL;
        };
    }

    private void openMainView(UserType userType, UserResponseDto user) {
        // Initialize services
        UsuarioService usuarioService = new UsuarioService(SERVER_HOST, SERVER_PORT);
        PacienteService pacienteService = new PacienteService(SERVER_HOST, SERVER_PORT);
        MedicamentoService medicamentoService = new MedicamentoService(SERVER_HOST, SERVER_PORT);

        // Create and show main view
        MenuPrincipalView mainView = new MenuPrincipalView(
                userType,
                this,
                usuarioService,
                pacienteService,
                medicamentoService,
                user.getId().intValue()
        );
        mainView.setVisible(true);
    }

    public void showPasswordChangeView() {
        AuthService passwordAuthService = new AuthService(SERVER_HOST, SERVER_PORT);

        // ✅ Crear CambioClaveView directamente (ya es un JFrame)
        new CambioClaveView(passwordAuthService);
        // No necesitas setVisible(true) porque lo hace en initializeFrame()
    }

    public void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
                loginView,
                "¿Está seguro que desea salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public void logout() {
        currentUser = null;
        loginView.clearFields();
        loginView.setVisible(true);
    }

    public UserResponseDto getCurrentUser() {
        return currentUser;
    }

    // Validation
    private boolean validateInput(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            showWarning("Por favor, ingrese su usuario o email");
            return false;
        }

        if (password == null || password.isEmpty()) {
            showWarning("Por favor, ingrese su contraseña");
            return false;
        }

        if (password.length() < 4) {
            showWarning("La contraseña debe tener al menos 4 caracteres");
            return false;
        }

        return true;
    }

    // UI Helper Methods
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                loginView,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
                loginView,
                message,
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void handleError(String message, Exception ex) {
        ex.printStackTrace();
        String errorDetails = ex.getMessage() != null ? ex.getMessage() : "Error desconocido";
        showError(message + "\nDetalles: " + errorDetails);
    }

    // Enum for user types
    public enum UserType {
        ADMINISTRADOR,
        FARMACEUTA,
        MEDICO,
        NULL
    }
}