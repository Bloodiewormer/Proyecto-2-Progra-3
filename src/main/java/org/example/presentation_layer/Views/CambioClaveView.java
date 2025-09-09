package org.example.presentation_layer.Views;

import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Components.CustomPaswordField;
import org.example.presentation_layer.Components.CustomTextField;
import org.example.presentation_layer.Controllers.CambioClaveController;
import org.example.presentation_layer.Controllers.LoginController;

import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

public class CambioClaveView extends JFrame{
    private JPanel MainPanel;
    private JTextField IDtextField;
    private JPasswordField passwordField;
    private JButton Aceptbutton;
    private JPanel textPasschangeLable;
    private JLabel ChagepassLable;
    private JPasswordField ConfirmpasswordField;

    CambioClaveController cambioClaveController;


    public CambioClaveView( LoginController controller) {





        JFrame frame = new JFrame("CambioClaveView");
        frame.setUndecorated(true);


        frame.setContentPane(MainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setSize(300, 200);
        frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 30, 30));
        frame.setVisible(true);

        Aceptbutton.addActionListener(e -> {
            String idText = IDtextField.getText();
            String newPassword = new String(passwordField.getPassword());
            String confirmpassword = new String(ConfirmpasswordField.getPassword());

            if (idText.isEmpty() || newPassword.isEmpty() || confirmpassword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "El ID de usuario debe ser un número válido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //comprobar que los espacios no esten vacios
            if (IDtextField.getText().isEmpty() || newPassword.isEmpty() || confirmpassword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //comprobar que las contraseñas coincidan
            if (!newPassword.equals(confirmpassword)) {
                JOptionPane.showMessageDialog(frame, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            cambioClaveController = new CambioClaveController(controller);
            //comprobar que el id sea valido y el usuario exista
            if (!cambioClaveController.IsValidUserID(IDtextField.getText() ) || !cambioClaveController.usuarioExiste(id)) {
                JOptionPane.showMessageDialog(frame, "El ID de usuario no es válido o no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //comprobar que la nueva contraseña sea valida
            if (!cambioClaveController.IsValidPassword(newPassword)) {
                JOptionPane.showMessageDialog(frame, "La nueva contraseña no cumple con los requisitos de seguridad.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //cambiar la contraseña
            if (cambioClaveController.cambiarClave(id, newPassword)) {
                JOptionPane.showMessageDialog(frame, "Contraseña cambiada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Error al cambiar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }




    public JPanel getMainPanel() {
        return MainPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        IDtextField = new CustomTextField();
        Aceptbutton = new BlueRoundedButton("REGISTER");
        passwordField = new CustomPaswordField();
        ConfirmpasswordField = new CustomPaswordField();
    }
}
