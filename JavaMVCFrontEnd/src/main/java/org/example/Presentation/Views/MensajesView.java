package org.example.Presentation.Views;

import org.example.Presentation.Components.CustomButton;
import org.example.Presentation.Components.LoadingOverlay;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MensajesView extends JPanel {

    // Panels
    private JPanel PrincipalPanel;
    private JPanel LeftPanel;
    private JPanel RightPanel;
    private JPanel ComunicationPanel;
    private JPanel MessageComposerPanel;
    private JPanel MessagesPanel;
    private JPanel StatusPanel;

    // Labels
    private JLabel UserLabel;

    // TextFields
    private JTextField WriteMessageField;

    // Buttons
    private JButton StatusButton;
    private JButton SendButton;

    // Scroll and Lists
    private JScrollPane MessagesScroll;
    private JScrollPane UsersScroll;
    private JList<String> UsersList;

    // Models
    private DefaultListModel<String> usersModel;

    public MensajesView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(PrincipalPanel, BorderLayout.CENTER);

        // Inicializar modelo de la lista
        usersModel = new DefaultListModel<>();
        UsersList.setModel(usersModel);

        // Configurar MessagesPanel con BoxLayout para apilar mensajes
        MessagesPanel.setLayout(new BoxLayout(MessagesPanel, BoxLayout.Y_AXIS));
    }

    // ---------------------------
    // Getters públicos
    // ---------------------------

    public JPanel getPrincipalPanel() {
        return PrincipalPanel;
    }

    public JTextField getWriteMessageField() {
        return WriteMessageField;
    }

    public JButton getSendButton() {
        return SendButton;
    }

    public JList<String> getUsersList() {
        return UsersList;
    }

    public DefaultListModel<String> getUsersModel() {
        return usersModel;
    }

    public JPanel getMessagesPanel() {
        return MessagesPanel;
    }

    public JScrollPane getMessagesScroll() {
        return MessagesScroll;
    }

    public JLabel getUserLabel() {
        return UserLabel;
    }

    public JButton getStatusButton() {
        return StatusButton;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        SendButton = new CustomButton("Enviar", Color.decode("#4CAF50"), Color.WHITE);
        StatusButton = new CustomButton("", Color.decode("#2196F3"), Color.WHITE);
    }
}
