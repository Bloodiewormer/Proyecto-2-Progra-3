// java
package org.example.Presentation.Views;

import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.CustomTextField;
import org.example.Presentation.Components.CustomPasswordField;

import org.example.Presentation.Components.LoadingOverlay;
import org.example.Presentation.Controllers.CambioClaveController;
import org.example.Services.AuthService;

import javax.swing.*;
import java.awt.*;

public class CambioClaveView extends JPanel {
    @SuppressWarnings("unused")
    private JPanel mainPanel;
    @SuppressWarnings("unused")
    private JPanel textPasswordChangePanel;
    @SuppressWarnings("unused")
    private JLabel changePasswordLabel;
    private JTextField idTextField = new CustomTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JPasswordField confirmPasswordField = new JPasswordField();
    private JButton acceptButton = new BlueRoundedButton("Aceptar");
    private final LoadingOverlay overlay;

    private CambioClaveController controller;

    public CambioClaveView(JFrame parent, AuthService authService) {
        this.overlay = new LoadingOverlay(parent);
        buildUI();
        this.controller = new CambioClaveController(this, authService);
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("ID usuario:"), gbc);
        gbc.gridy++;
        add(new JLabel("Nueva contraseña:"), gbc);
        gbc.gridy++;
        add(new JLabel("Confirmar contraseña:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        add(idTextField, gbc);
        gbc.gridy++;
        add(passwordField, gbc);
        gbc.gridy++;
        add(confirmPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        add(acceptButton, gbc);
    }

    // FIX: getters required by controller and for previous code
    public JButton getAcceptButton() { return acceptButton; }
    public JTextField getIdTextField() { return idTextField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JPasswordField getConfirmPasswordField() { return confirmPasswordField; }

    public void showLoading(boolean visible) { overlay.show(visible); }

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
