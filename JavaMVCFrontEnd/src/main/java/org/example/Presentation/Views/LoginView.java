package org.example.Presentation.Views;

import org.example.Domain.Dtos.Auth.UserResponseDto;
import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.CustomPasswordField;
import org.example.Presentation.Components.CustomTextField;
import org.example.Presentation.Components.LoadingOverlay;
import org.example.Presentation.IObserver;
import org.example.Utilities.ChangeType;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

public class LoginView extends JFrame implements IObserver {
    private JPanel mainPanel;
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel forgotLabel;
    private JLabel closeProgramLabel;
    private JPanel loginPanel;
    private JPanel IconPanel;
    private JLabel icon;
    private JLabel LoginLabel;
    private JLabel infoLabel;

    private final LoadingOverlay loadingOverlay;

    private Runnable passwordChangeCallback;

    public LoginView() {
        initializeUI();
        registerEventHandlers();
        loadingOverlay = new LoadingOverlay(this);
    }

    private void initializeUI() {
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LogoAPP.png")));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.printf("Icon could not be loaded: %s%n", e.getMessage());
        }

        setContentPane(mainPanel);
        setTitle("Sistema de Gestión - Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        setLocationRelativeTo(null);
        setVisible(true);
        getRootPane().setDefaultButton(loginButton);
    }

    private void registerEventHandlers() {
        // Enter key support on text fields
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        };

        userIdField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        // Close button
        closeProgramLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // This will be handled by the controller
                System.exit(0);
            }
        });

        // Info button
        infoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showGroupInfo();
            }
        });

        // Forgot password label
        forgotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (passwordChangeCallback != null) {
                    passwordChangeCallback.run();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                forgotLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                forgotLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void showGroupInfo() {
        JOptionPane.showMessageDialog(this,
                """
                Miembros del Grupo:
                - David González Córdoba
                - Dany Montero Romero
                - Emmanuel Núñez Jiménez
                
                Sistema de Gestión v1.0
                Universidad Nacional de Costa Rica
                """,
                "Información del Grupo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Public methods for controller
    public void addLoginListener(java.awt.event.ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addForgotPasswordListener(MouseAdapter listener) {
        forgotLabel.addMouseListener(listener);
    }

    public String getUsername() {
        return userIdField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void clearFields() {
        userIdField.setText("");
        passwordField.setText("");
        userIdField.requestFocus();
    }

    public void showLoading(boolean visible) {
        loadingOverlay.show(visible);
    }

    @Override
    public void update(ChangeType changeType, Object data) {
        if (changeType == ChangeType.UPDATED && data instanceof UserResponseDto user) {
            // Login successful - could show a welcome message
            System.out.println("Login successful for user: " + user.getUsername());
        } else if (changeType == ChangeType.DELETED) {
            // Logout
            clearFields();
            setVisible(true);
        }
    }

    public void setPasswordChangeCallback(Runnable callback) {
        this.passwordChangeCallback = callback;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        loginButton = new BlueRoundedButton("INICIAR SESIÓN");
        userIdField = new CustomTextField("Ingrese su ID");
        passwordField = new CustomPasswordField("Ingrese su contraseña");
    }
}