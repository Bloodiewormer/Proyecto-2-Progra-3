package org.example.Presentation.Views;

import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.CustomPasswordField;
import org.example.Presentation.Components.CustomTextField;
import org.example.Presentation.Controllers.LoginController;
import org.example.Presentation.Models.UserType;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

public class LoginView  extends  JFrame {
    private JPanel mainPanel;
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel forgotLabel;
    private JLabel closeProgramLabel;
    @SuppressWarnings("unused")
    private JPanel loginPanel;
    @SuppressWarnings("unused")
    private JPanel IconPanel;
    @SuppressWarnings("unused")
    private JLabel icon;
    @SuppressWarnings("unused")
    private JLabel LoginLabel;
    private JLabel infoLabel;

    private final LoginController controller;

    public LoginView(LoginController controller) {
        this.controller = controller;
        initializeUI();
        registerEventHandlers();
    }

    private void initializeUI() {
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LogoAPP.png")));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.printf("Icon could not be loaded: %s%n", e.getMessage());
        }
        setContentPane(mainPanel);
        setTitle("Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        setLocationRelativeTo(null);
        setVisible(true);
        getRootPane().setDefaultButton(loginButton);
    }

    private void registerEventHandlers() {
        loginButton.addActionListener(e -> onLoginClicked());


        forgotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.showPasswordChangeView();
            }
        });


        closeProgramLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.exitApplication();
            }
        });

        infoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showGroupInfo();
            }
        });

        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onLoginClicked();
                }
            }
        };

        userIdField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

    }



    private void onLoginClicked() {
        String userInput = userIdField.getText().trim();
        String password = new String(passwordField.getPassword());
        int id;
        try {
            id = Integer.parseInt(userInput);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.");
            return;
        }
        boolean success = controller.login(id, password);

        if (success) {
            UserType userType = controller.getUserType(id);
            // Delegate navigation to controller
            controller.onLoginSuccess(userType, this, id);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.");
        }

    }

    private void showGroupInfo() {
        JOptionPane.showMessageDialog(this,
                """
                Group Members:
                - David Gonzalez Cordoba
                - Dany Montero Romero
                - Emmanuel Nunez Jimenez
                """,
                "Group Members",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void createUIComponents() {
        loginButton = new BlueRoundedButton("LOGIN");
        userIdField = new CustomTextField();
        passwordField = new CustomPasswordField();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}