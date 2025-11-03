package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Mensaje.*;
import org.example.Logic.ClienteMensajeria;
import org.example.Presentation.Views.MensajesView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador de mensajería siguiendo el patrón del FarmaceutaController
 * Maneja toda la lógica de eventos, validaciones y comunicación con servidor
 */
public class MensajeController {
    private final MensajesView mensajesView;
    private final ClienteMensajeria clienteMensajeria;

    private String selectedUser;
    private final String currentUser;
    private final Map<String, Boolean> usersStatus;

    public MensajeController(MensajesView mensajesView, String currentUser, String serverHost, int serverPort) {
        this.mensajesView = mensajesView;
        this.currentUser = currentUser;
        this.usersStatus = new HashMap<>();

        // Inicializar cliente de mensajería
        this.clienteMensajeria = new ClienteMensajeria(currentUser, this);

        // Conectar al servidor de forma asíncrona
        connectToServerAsync(serverHost, serverPort);

        // Configurar listeners
        addListeners();

        // Configurar estado inicial
        setupInitialState();
    }

    // ---------------------------
    // Initialization
    // ---------------------------

    private void connectToServerAsync(String host, int port) {
        showLoading(true);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return clienteMensajeria.conectar(host, port);
            }

            @Override
            protected void done() {
                try {
                    Boolean connected = get();
                    if (!connected) {
                        showError("No se pudo conectar al servidor de mensajería");
                    }
                } catch (Exception e) {
                    handleError("Error al conectar al servidor", e);
                } finally {
                    showLoading(false);
                }
            }
        };
        worker.execute();
    }

    private void setupInitialState() {
        setPlaceholder("Escribir mensaje...");
    }

    private void addListeners() {
        mensajesView.getSendButton().addActionListener(e -> handleSendMessage());
        mensajesView.getWriteMessageField().addActionListener(e -> handleSendMessage());
        mensajesView.getUsersList().getSelectionModel().addListSelectionListener(this::handleUserSelection);

        // Placeholder management
        mensajesView.getWriteMessageField().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                handleFieldFocusGained();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                handleFieldFocusLost();
            }
        });
    }

    // ---------------------------
    // Action Handlers
    // ---------------------------

    private void handleSendMessage() {
        if (!validateMessageFields()) return;

        try {
            String message = mensajesView.getWriteMessageField().getText().trim();

            AddMensajeRequestDto dto = new AddMensajeRequestDto(
                    currentUser,
                    selectedUser,
                    message
            );

            // Agregar mensaje a la vista inmediatamente (optimistic UI)
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));
            addSentMessage(message, timestamp);

            // Limpiar campo
            clearMessageField();

            // Enviar al servidor de forma asíncrona
            executeAsync(
                    () -> {
                        clienteMensajeria.enviarMensaje(dto);
                        return true;
                    },
                    success -> System.out.println("✅ Mensaje enviado exitosamente"),
                    "Error al enviar mensaje"
            );

        } catch (Exception ex) {
            handleError("Error al enviar mensaje", ex);
        }
    }

    private void handleUserSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = mensajesView.getUsersList().getSelectedIndex();
            if (row >= 0) {
                String userWithEmoji = mensajesView.getUsersList().getSelectedValue();
                if (userWithEmoji != null) {
                    loadChatWithUser(userWithEmoji);
                }
            }
        }
    }

    private void loadChatWithUser(String userWithEmoji) {
        String cleanUser = cleanUserName(userWithEmoji);
        this.selectedUser = cleanUser;

        // Actualizar UI
        updateUserLabel(cleanUser);

        // Actualizar indicador de estado
        boolean isActive = usersStatus.getOrDefault(cleanUser, false);
        updateStatusIndicator(isActive);

        // Limpiar mensajes anteriores
        clearMessages();

        // Cargar historial de forma asíncrona
        if (clienteMensajeria.isConectado()) {
            loadHistoryAsync(cleanUser);
        } else {
            showWarning("No hay conexión con el servidor");
        }
    }

    private void loadHistoryAsync(String contactUser) {
        showLoading(true);

        ListMensajeResponsetDto dto = new ListMensajesRequestDto(currentUser, contactUser);

        executeAsync(
                () -> {
                    clienteMensajeria.solicitarHistorial(dto);
                    return true;
                },
                success -> System.out.println("✅ Solicitud de historial enviada"),
                "Error al cargar historial"
        );
    }

    private void handleFieldFocusGained() {
        if (mensajesView.getWriteMessageField().getText().equals("Escribir mensaje...")) {
            clearPlaceholder();
        }
    }

    private void handleFieldFocusLost() {
        if (mensajesView.getWriteMessageField().getText().trim().isEmpty()) {
            setPlaceholder("Escribir mensaje...");
        }
    }

    // ---------------------------
    // Callbacks from ClienteMensajeria
    // ---------------------------

    /**
     * Manejar mensaje entrante del servidor
     */
    public void handleIncomingMessage(MensajeResponseDto mensaje) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = mensaje.getFechaHora()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));

            // Solo mostrar si es del usuario actualmente seleccionado
            if (mensaje.getRemitente().equals(selectedUser)) {
                addReceivedMessage(
                        mensaje.getContenido(),
                        mensaje.getRemitente(),
                        timestamp
                );
            }

            // Notificación sonora
            java.awt.Toolkit.getDefaultToolkit().beep();
        });
    }

    /**
     * Manejar lista de usuarios recibida del servidor
     */
    public void handleUsersList(List<String> users) {
        SwingUtilities.invokeLater(() -> {
            DefaultListModel<String> model = mensajesView.getUsersModel();
            model.clear();

            for (String user : users) {
                if (!user.equals(currentUser)) {
                    boolean active = usersStatus.getOrDefault(user, true);
                    String emoji = active ? "🟢" : "🔴";
                    model.addElement(emoji + " " + user);
                }
            }
        });
    }

    /**
     * Manejar cambio de estado de un usuario
     */
    public void handleUserStatusChange(String username, boolean isActive) {
        usersStatus.put(username, isActive);

        SwingUtilities.invokeLater(() -> {
            // Actualizar en la lista
            DefaultListModel<String> model = mensajesView.getUsersModel();
            for (int i = 0; i < model.getSize(); i++) {
                String element = model.getElementAt(i);
                String cleanName = cleanUserName(element);

                if (cleanName.equals(username)) {
                    String emoji = isActive ? "🟢" : "🔴";
                    model.set(i, emoji + " " + username);
                    break;
                }
            }

            // Si es el usuario seleccionado, actualizar indicador
            if (username.equals(selectedUser)) {
                updateStatusIndicator(isActive);
            }
        });
    }

    /**
     * Manejar historial de mensajes recibido
     */
    public void handleHistory(List<MensajeResponseDto> mensajes) {
        SwingUtilities.invokeLater(() -> {
            clearMessages();

            for (MensajeResponseDto mensaje : mensajes) {
                String timestamp = mensaje.getFechaHora()
                        .format(DateTimeFormatter.ofPattern("HH:mm"));

                if (mensaje.getRemitente().equals(currentUser)) {
                    addSentMessage(mensaje.getContenido(), timestamp);
                } else {
                    addReceivedMessage(
                            mensaje.getContenido(),
                            mensaje.getRemitente(),
                            timestamp
                    );
                }
            }

            showLoading(false);
        });
    }

    // ---------------------------
    // UI Manipulation Methods
    // ---------------------------

    private void setPlaceholder(String text) {
        mensajesView.getWriteMessageField().setText(text);
        mensajesView.getWriteMessageField().setForeground(new Color(128, 128, 128));
    }

    private void clearPlaceholder() {
        mensajesView.getWriteMessageField().setText("");
        mensajesView.getWriteMessageField().setForeground(Color.BLACK);
    }

    private void clearMessageField() {
        mensajesView.getWriteMessageField().setText("");
    }

    private void updateUserLabel(String username) {
        try {
            java.lang.reflect.Field field = mensajesView.getClass().getDeclaredField("UserLabel");
            field.setAccessible(true);
            JLabel userLabel = (JLabel) field.get(mensajesView);
            userLabel.setText("Comunicándose con: " + username);
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar UserLabel: " + e.getMessage());
        }
    }

    private void updateStatusIndicator(boolean isActive) {
        try {
            java.lang.reflect.Field field = mensajesView.getClass().getDeclaredField("StatusButton");
            field.setAccessible(true);
            JButton statusButton = (JButton) field.get(mensajesView);
            statusButton.setBackground(isActive ? new Color(0, 200, 0) : Color.RED);
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar StatusButton: " + e.getMessage());
        }
    }

    private void clearMessages() {
        try {
            java.lang.reflect.Field field = mensajesView.getClass().getDeclaredField("MessagesPanel");
            field.setAccessible(true);
            JPanel messagesPanel = (JPanel) field.get(mensajesView);
            messagesPanel.removeAll();
            messagesPanel.revalidate();
            messagesPanel.repaint();
        } catch (Exception e) {
            System.err.println("❌ Error al limpiar mensajes: " + e.getMessage());
        }
    }

    private void addSentMessage(String content, String timestamp) {
        try {
            java.lang.reflect.Field field = mensajesView.getClass().getDeclaredField("MessagesPanel");
            field.setAccessible(true);
            JPanel messagesPanel = (JPanel) field.get(mensajesView);

            JPanel messagePanel = createMessagePanel(content, timestamp, true);
            messagesPanel.add(messagePanel);
            messagesPanel.revalidate();
            messagesPanel.repaint();
            scrollToBottom();
        } catch (Exception e) {
            System.err.println("❌ Error al agregar mensaje enviado: " + e.getMessage());
        }
    }

    private void addReceivedMessage(String content, String sender, String timestamp) {
        try {
            java.lang.reflect.Field field = mensajesView.getClass().getDeclaredField("MessagesPanel");
            field.setAccessible(true);
            JPanel messagesPanel = (JPanel) field.get(mensajesView);

            JPanel messagePanel = createMessagePanel(content, timestamp, false);
            messagesPanel.add(messagePanel);
            messagesPanel.revalidate();
            messagesPanel.repaint();
            scrollToBottom();
        } catch (Exception e) {
            System.err.println("❌ Error al agregar mensaje recibido: " + e.getMessage());
        }
    }

    private JPanel createMessagePanel(String content, String timestamp, boolean isSent) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel bubble = new JPanel(new BorderLayout(5, 5));
        bubble.setBackground(isSent ? new Color(220, 248, 198) : new Color(230, 230, 230));
        bubble.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel messageLabel = new JLabel("<html><body style='width: 250px'>" + content + "</body></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel timeLabel = new JLabel(timestamp);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabel.setForeground(Color.GRAY);

        bubble.add(messageLabel, BorderLayout.CENTER);
        bubble.add(timeLabel, BorderLayout.SOUTH);

        panel.add(bubble, isSent ? BorderLayout.EAST : BorderLayout.WEST);

        return panel;
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            try {
                java.lang.reflect.Field field = mensajesView.getClass().getDeclaredField("MessagesScroll");
                field.setAccessible(true);
                JScrollPane scrollPane = (JScrollPane) field.get(mensajesView);
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            } catch (Exception e) {
                System.err.println("❌ Error al hacer scroll: " + e.getMessage());
            }
        });
    }

    // ---------------------------
    // Validation & Utility Methods
    // ---------------------------

    private boolean validateMessageFields() {
        String message = mensajesView.getWriteMessageField().getText().trim();

        if (message.isEmpty() || message.equals("Escribir mensaje...")) {
            showWarning("Por favor escribe un mensaje");
            return false;
        }

        if (selectedUser == null) {
            showWarning("Por favor selecciona un usuario primero");
            return false;
        }

        boolean isActive = usersStatus.getOrDefault(selectedUser, false);
        if (!isActive) {
            showWarning("El usuario " + selectedUser + " no está activo");
            return false;
        }

        if (!clienteMensajeria.isConectado()) {
            showError("No hay conexión con el servidor");
            return false;
        }

        return true;
    }

    private String cleanUserName(String nameWithEmoji) {
        return nameWithEmoji.replaceAll("^[🟢🔴]\\s+", "").trim();
    }

    private <T> void executeAsync(AsyncTask<T> task, ResultHandler<T> onSuccess, String errorMessage) {
        SwingWorker<T, Void> worker = new SwingWorker<>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.execute();
            }

            @Override
            protected void done() {
                try {
                    T result = get();
                    onSuccess.handle(result);
                } catch (Exception ex) {
                    handleError(errorMessage, ex);
                } finally {
                    showLoading(false);
                }
            }
        };
        worker.execute();
    }

    // ---------------------------
    // UI Feedback Methods
    // ---------------------------

    private void showLoading(boolean visible) {
        if (visible) {
            mensajesView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            mensajesView.setCursor(Cursor.getDefaultCursor());
        }
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(
                (JFrame) SwingUtilities.getWindowAncestor(mensajesView.getPrincipalPanel()),
                message,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                (JFrame) SwingUtilities.getWindowAncestor(mensajesView.getPrincipalPanel()),
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
                (JFrame) SwingUtilities.getWindowAncestor(mensajesView.getPrincipalPanel()),
                message,
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void handleError(String message, Exception ex) {
        ex.printStackTrace();
        showError(message + ": " + ex.getMessage());
    }

    // ---------------------------
    // Public Methods
    // ---------------------------

    /**
     * Desconectar del servidor al cerrar
     */
    public void disconnect() {
        if (clienteMensajeria != null) {
            clienteMensajeria.desconectar();
        }
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public boolean isConnected() {
        return clienteMensajeria.isConectado();
    }

    // ---------------------------
    // Functional Interfaces
    // ---------------------------

    @FunctionalInterface
    private interface AsyncTask<T> {
        T execute() throws Exception;
    }

    @FunctionalInterface
    private interface ResultHandler<T> {
        void handle(T result);
    }
}


