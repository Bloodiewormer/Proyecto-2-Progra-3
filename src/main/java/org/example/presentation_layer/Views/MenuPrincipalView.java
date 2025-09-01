package org.example.presentation_layer.Views;

import org.example.presentation_layer.Components.CustomButton;
import org.example.presentation_layer.Models.UserType;
import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

public class MenuPrincipalView extends JFrame {
    private JPanel mainPanel;
    private JPanel MainPanel;
    private JPanel EncogiblePanel;
    private JPanel OpcionesPanel;
    private JPanel ContentPanel;
    private JLabel Button;
    private JButton salirButton;
    private JButton medicosButton;
    private JButton farmaceutasButton;
    private JButton pacientesButton;
    private JButton medicamentosButton;
    private JButton dashboardButton;
    private JButton acercadeButton;
    private JButton prescribirButton;
    //private JTabbedPane tabbedPane;

    public MenuPrincipalView(UserType userType)  {
        setContentPane(MainPanel);
        setTitle("Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        setVisible(true);
        setLocationRelativeTo(null);

    switch (userType) {

        case ADMINISTRADOR:



            break;
        case FARMACEUTA:

            break;
        case MEDICO:

            break;
        default:
            throw new IllegalArgumentException("Tipo de usuario no soportado: " + userType);

        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        salirButton = new CustomButton("Salir");
    }
}
