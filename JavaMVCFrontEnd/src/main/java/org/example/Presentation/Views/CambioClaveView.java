package org.example.Presentation.Views;

import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.CustomPasswordField;
import org.example.Presentation.Components.CustomTextField;
import org.example.Presentation.Controllers.CambioClaveController;
import org.example.Services.AuthService;

import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

public class CambioClaveView extends JFrame {
    private JPanel mainPanel;
    private JTextField idTextField;
    private JPasswordField passwordField;
    private JButton acceptButton;
    @SuppressWarnings("unused")
    private JPanel textPasswordChangePanel;
    @SuppressWarnings("unused")
    private JLabel changePasswordLabel;
    private JPasswordField confirmPasswordField;

    private final CambioClaveController cambioClaveController;

    // Constructor adaptado para Proyecto 2 (recibe AuthService)
    public CambioClaveView(AuthService authService) {
        this.cambioClaveController = new CambioClaveController(this, authService);
        initializeFrame();
        initializeEvents();
    }

    // Constructor con userId predefinido
    public CambioClaveView(AuthService authService, Integer userId) {
        this.cambioClaveController = new CambioClaveController(this, authService, userId);
        initializeFrame();
        initializeEvents();
    }

    private void initializeFrame() {
        setUndecorated(true); // ✅ Sin bordes (como Proyecto 1)
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(300, 150); // ✅ Tamaño original
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30)); // ✅ Bordes redondeados
        setVisible(true);
    }

    private void initializeEvents() {
        acceptButton.addActionListener(e -> {
            // El controller ya maneja todo
        });
    }

    // Getters para el controller
    public JTextField getIdTextField() {
        return idTextField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JPasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public JButton getAcceptButton() {
        return acceptButton;
    }

    public void showLoading(boolean visible) {
        // Implementar si necesitas overlay
        acceptButton.setEnabled(!visible);
        if (visible) {
            acceptButton.setText("Procesando...");
        } else {
            acceptButton.setText("Aceptar");
        }
    }

    public void clearFields() {
        idTextField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    private void createUIComponents() {
        idTextField = new CustomTextField();
        acceptButton = new BlueRoundedButton("Aceptar");
        passwordField = new CustomPasswordField();
        confirmPasswordField = new CustomPasswordField();
    }
}