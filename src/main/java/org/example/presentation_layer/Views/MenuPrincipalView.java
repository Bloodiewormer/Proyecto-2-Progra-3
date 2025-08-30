package org.example.presentation_layer.Views;

import org.example.presentation_layer.Models.UserType;
import javax.swing.*;

public class MenuPrincipalView {
    private JPanel mainPanel;
    //private JTabbedPane tabbedPane;

    public MenuPrincipalView(UserType userType) {


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
}
