package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Mensaje.*;
import org.example.Logic.ClienteMensajeria;
import org.example.Presentation.Views.MensajesView;
import org.example.Services.MensajeService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MensajeController {
    private final MensajesView mensajesView;
    private final ClienteMensajeria clienteMensajeria;
    private final MensajeService mensajeService;

    private String selectedUser;
    private final String currentUser;
    private final Map<String, Boolean> usersStatus;
    private final List<String> allUsers = new ArrayList<>();
    private final Map<String, List<MensajeResponseDto>> chatHistory = new HashMap<>();

    public MensajeController(MensajesView mensajesView, String currentUser, String serverHost, int serverPort) {
        this.mensajesView = mensajesView;
        this.currentUser = currentUser;
        this.usersStatus = new HashMap<>();
        this.mensajeService = new MensajeService("localhost", 7002);

        System.out.println("========================================");
        System.out.println("[MensajeController] Inicializando:");
        System.out.println("  Usuario actual: " + currentUser);
        System.out.println("  Servidor: " + serverHost + ":" + serverPort);
        System.out.println("========================================");

        this.clienteMensajeria = new ClienteMensajeria(currentUser, this);

        connectToServerAsync(serverHost, serverPort);

        addListeners();

        setupInitialState();
    }

    // ---------------------------
    // Initialization
    // ---------------------------

    private void connectToServerAsync(String host, int port) {
        showLoading(true);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
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

        mensajesView.getVerSoloUsuariosActivosCheckBox().addActionListener(e -> updateUserListFilter());
 
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

    private void updateUserListFilter() {
        SwingUtilities.invokeLater(() -> {
            DefaultListModel<String> model = mensajesView.getUsersModel();
            model.clear();

            boolean onlyOnline = mensajesView.getVerSoloUsuariosActivosCheckBox().isSelected();

            System.out.println("[MensajeController] 🔄 Actualizando lista - Solo online: " + onlyOnline);

            int count = 0;
            for (String user : allUsers) {
                if (user.equals(currentUser)) continue;  // No mostrar el usuario actual

                boolean isOnline = usersStatus.getOrDefault(user, false);

                // ✅ Si el filtro está activo, solo mostrar usuarios online
                if (onlyOnline && !isOnline) {
                    continue;
                }

                // Agregar con emoji según estado
                String emoji = isOnline ? "🟢" : "🔴";
                model.addElement(emoji + " " + user);
                count++;

                System.out.println("   → " + emoji + " " + user);
            }

            System.out.println("[MensajeController] ✅ Lista actualizada: " + count + " usuarios mostrados");
        });
    }

    // ---------------------------
    // Action Handlers
    // ---------------------------

// REEMPLAZAR handleSendMessage():

    private void handleSendMessage() {
        if (!validateMessageFields()) return;

        try {
            String message = mensajesView.getWriteMessageField().getText().trim();

            AddMensajeRequestDto dto = new AddMensajeRequestDto(
                    currentUser,
                    selectedUser,
                    message
            );

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));

            addSentMessage(message, timestamp);

            // ✅ Agregar al historial en caché
            MensajeResponseDto cachedMessage = new MensajeResponseDto(
                    null, // ID será asignado por el servidor
                    currentUser,
                    selectedUser,
                    message,
                    "SENT",
                    LocalDateTime.now().toString(),
                    null
            );
            chatHistory.computeIfAbsent(selectedUser, k -> new ArrayList<>()).add(cachedMessage);

            clearMessageField();

            clienteMensajeria.enviarMensaje(dto);

            System.out.println("✅ Mensaje enviado correctamente");

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

        System.out.println("[MensajeController] 💬 Cargando chat con: " + cleanUser);

        updateUserLabel(cleanUser);

        boolean isActive = usersStatus.getOrDefault(cleanUser, false);
        updateStatusIndicator(isActive);

        clearMessages(); // Limpiar mensajes anteriores

        if (clienteMensajeria.isConectado()) {
            loadHistoryAsync(cleanUser); // ✅ Cargar historial SIEMPRE
        } else {
            showWarning("No hay conexión con el servidor");
        }
    }

    private void loadHistoryAsync(String contactUser) {
        showLoading(true);

        System.out.println("[MensajeController] 📜 Solicitando historial con: " + contactUser);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    ListMensajesRequestDto dto = new ListMensajesRequestDto(currentUser, contactUser);
                    clienteMensajeria.solicitarHistorial(dto);
                } catch (Exception e) {
                    System.err.println("[MensajeController] ❌ Error solicitando historial: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                showLoading(false);
            }
        };
        worker.execute();
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

// ACTUALIZAR handleIncomingMessage():

    public void handleIncomingMessage(MensajeResponseDto mensaje) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = mensaje.getFechaHora();
            if (timestamp != null && timestamp.length() > 16) {
                timestamp = timestamp.substring(11, 16);
            }

            // ✅ Agregar al historial en caché
            String otherUser = mensaje.getRemitente().equals(currentUser)
                    ? mensaje.getDestinatario()
                    : mensaje.getRemitente();

            chatHistory.computeIfAbsent(otherUser, k -> new ArrayList<>()).add(mensaje);

            // Solo mostrar si es del usuario seleccionado
            if (mensaje.getRemitente().equals(selectedUser)) {
                addReceivedMessage(
                        mensaje.getContenido(),
                        mensaje.getRemitente(),
                        timestamp
                );
                scrollToBottom();
            }

            java.awt.Toolkit.getDefaultToolkit().beep();
        });
    }

    public void handleUsersList(List<String> users) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("[MensajeController] 👥 Lista recibida: " + users.size() + " usuarios");

            // ✅ Guardar TODOS los usuarios
            allUsers.clear();
            allUsers.addAll(users);

            // ✅ Marcar todos como online inicialmente
            for (String user : users) {
                if (!user.equals(currentUser)) {
                    usersStatus.put(user, true);
                }
            }

            // Actualizar la vista con filtro
            updateUserListFilter();
        });
    }

    public void handleUserStatusChange(String username, boolean isActive) {
        System.out.println("[MensajeController] " +
                (isActive ? "🟢" : "🔴") + " Estado actualizado: " + username);

        // ✅ Actualizar estado
        usersStatus.put(username, isActive);

        // ✅ Agregar a la lista de todos los usuarios si no existe
        if (!allUsers.contains(username) && !username.equals(currentUser)) {
            allUsers.add(username);
        }

        // ✅ Actualizar la vista con el filtro
        updateUserListFilter();

        // ✅ Si es el usuario seleccionado, actualizar indicador
        if (username.equals(selectedUser)) {
            updateStatusIndicator(isActive);
        }
    }

    public void handleHistory(List<MensajeResponseDto> mensajes) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("[MensajeController] 📜 Historial recibido: " + mensajes.size() + " mensajes para " + selectedUser);

            // ✅ Guardar historial en caché
            if (selectedUser != null) {
                chatHistory.put(selectedUser, new ArrayList<>(mensajes));
            }

            clearMessages();

            for (MensajeResponseDto mensaje : mensajes) {
                String timestamp = mensaje.getFechaHora();
                if (timestamp != null && timestamp.length() > 16) {
                    timestamp = timestamp.substring(11, 16);
                }

                if (mensaje.getRemitente().equals(currentUser)) {
                    addSentMessage(mensaje.getContenido(), timestamp);
                } else {
                    addReceivedMessage(mensaje.getContenido(), mensaje.getRemitente(), timestamp);
                }
            }

            showLoading(false);
            scrollToBottom();
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

    private void updateStatusIndicator(boolean isOnline) {
        SwingUtilities.invokeLater(() -> {
            try {
                JButton statusButton = mensajesView.getStatusButton();
                if (statusButton != null) {
                    if (isOnline) {
                        statusButton.setBackground(new Color(76, 175, 80)); // Verde
                        statusButton.setToolTipText("Usuario en línea");
                    } else {
                        statusButton.setBackground(new Color(244, 67, 54)); // Rojo
                        statusButton.setToolTipText("Usuario desconectado");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error actualizando indicador: " + e.getMessage());
            }
        });
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
            showError("Por favor escribe un mensaje");
            return false;
        }

        if (selectedUser == null || selectedUser.isEmpty()) {
            showError("Por favor selecciona un usuario");
            return false;
        }

        // ✅ DEBUG: Ver estado del usuario


        if (!clienteMensajeria.isConectado()) {
            showError("El usuario " + selectedUser + " no está en línea");
            return false;
        }

        return true;
    }

    private String cleanUserName(String userWithEmoji) {
        if (userWithEmoji == null || userWithEmoji.isEmpty()) return "";

        // Remover cualquier emoji y espacios al inicio
        String cleaned = userWithEmoji.replaceAll("^[🟢🔴\\s]+", "").trim();

        System.out.println("[DEBUG] cleanUserName: '" + userWithEmoji + "' → '" + cleaned + "'");

        return cleaned;
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


