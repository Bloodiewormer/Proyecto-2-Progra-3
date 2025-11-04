package org.example.Logic;

import org.example.Domain.Dtos.Mensaje.*;
import org.example.Presentation.Controllers.MensajeController;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClienteMensajeria {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final String username;
    private final MensajeController controller;
    private volatile boolean conectado = false;
    private Thread listenerThread;

    public ClienteMensajeria(String username, MensajeController controller) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username no puede estar vacío");
        }
        if (controller == null) {
            throw new IllegalArgumentException("El controller no puede ser null");
        }
        this.username = username;
        this.controller = controller;
    }

    public boolean conectar(String host, int puerto) {
        if (conectado) {
            System.out.println("⚠️ Ya está conectado");
            return true;
        }

        try {
            socket = new Socket(host, puerto);


            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            // Enviar username para autenticación
            out.writeObject(username);
            out.flush();

            conectado = true;

            // Iniciar thread para escuchar mensajes
            listenerThread = new Thread(new EscuchadorMensajes(), "MensajeriaListener-" + username);
            listenerThread.setDaemon(true);
            listenerThread.start();

            System.out.println("✅ Conectado al servidor de mensajería como: " + username);
            return true;

        } catch (IOException e) {
            System.err.println("❌ Error al conectar al servidor: " + e.getMessage());
            conectado = false;
            cerrarRecursos();
            return false;
        }
    }

    /**
     * Enviar mensaje usando AddMensajeRequestDto
     */
    public void enviarMensaje(AddMensajeRequestDto dto) {
        if (!conectado) {
            System.err.println("⚠️ No hay conexión con el servidor");
            return;
        }

        try {
            synchronized (out) {
                out.writeObject(dto);
                out.flush();
            }
            System.out.println("📤 Mensaje enviado a: " + dto.getDestinatario());

        } catch (IOException e) {
            System.err.println("❌ Error al enviar mensaje: " + e.getMessage());
            desconectar();
        }
    }

    /**
     * Solicitar historial usando ListMensajesRequestDto
     */
    public void solicitarHistorial(ListMensajesRequestDto dto) {
        if (!conectado) {
            System.err.println("⚠️ No hay conexión con el servidor");
            return;
        }

        try {
            synchronized (out) {
                out.writeObject(dto);
                out.flush();
            }
            System.out.println("📋 Historial solicitado para: " + dto.getUsuario2());

        } catch (IOException e) {
            System.err.println("❌ Error al solicitar historial: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void desconectar() {
        if (!conectado) return;

        conectado = false;
        System.out.println("🔌 Desconectando del servidor...");

        if (listenerThread != null && listenerThread.isAlive()) {
            listenerThread.interrupt();
        }

        cerrarRecursos();
        System.out.println("✅ Desconectado del servidor");
    }

    private void cerrarRecursos() {
        try {
            if (out != null) {
                out.close();
                out = null;
            }
        } catch (IOException e) {
            System.err.println("Error cerrando output stream: " + e.getMessage());
        }

        try {
            if (in != null) {
                in.close();
                in = null;
            }
        } catch (IOException e) {
            System.err.println("Error cerrando input stream: " + e.getMessage());
        }

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            System.err.println("Error cerrando socket: " + e.getMessage());
        }
    }

    public boolean isConectado() {
        return conectado && socket != null && !socket.isClosed();
    }

    public String getUsername() {
        return username;
    }

    // ---------------------------
    // Inner Class: Listener
    // ---------------------------

    private class EscuchadorMensajes implements Runnable {
        @Override
        public void run() {
            System.out.println("[EscuchadorMensajes] 🎧 Iniciado para: " + username);

            while (conectado) {
                try {
                    Object obj = in.readObject();
                    System.out.println("[EscuchadorMensajes] 📩 Objeto recibido: " + obj.getClass().getName());

                    // Manejar diferentes tipos de mensajes
                    if (obj instanceof NotificationMessage) {
                        handleNotification((NotificationMessage) obj);
                    } else if (obj instanceof MensajeResponseDto) {
                        handleIncomingMessage((MensajeResponseDto) obj);
                    } else if (obj instanceof List) {
                        handleHistoryResponse((List<?>) obj);
                    } else {
                        System.out.println("[EscuchadorMensajes] ⚠️ Tipo desconocido: " +
                                obj.getClass().getName());
                    }

                } catch (EOFException e) {
                    System.out.println("[EscuchadorMensajes] 📴 Conexión cerrada por el servidor");
                    break;
                } catch (Exception e) {
                    if (conectado) {
                        System.err.println("[EscuchadorMensajes] ❌ Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                }
            }

            System.out.println("[EscuchadorMensajes] 🛑 Detenido");
        }

        private void handleNotification(NotificationMessage notification) {
            System.out.println("[EscuchadorMensajes] 🔔 Notificación: " + notification.getType());

            switch (notification.getType()) {
                case "USERS_LIST":
                    handleUsersList(notification.getData());
                    break;

                case "USER_STATUS_CHANGE":
                    handleUserStatusChange(notification.getData());
                    break;

                default:
                    System.out.println("[EscuchadorMensajes] ⚠️ Tipo de notificación desconocido: " +
                            notification.getType());
            }
        }

        /**
         * Manejar lista de usuarios activos
         */
        private void handleUsersList(Object data) {
            try {
                if (data instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> users = (List<String>) data;
                    System.out.println("[EscuchadorMensajes] 👥 Lista de usuarios: " + users);
                    controller.handleUsersList(users);
                }
            } catch (Exception e) {
                System.err.println("[EscuchadorMensajes] ❌ Error procesando lista: " + e.getMessage());
            }
        }

        /**
         * Manejar cambio de estado de usuario
         */
        private void handleUserStatusChange(Object data) {
            try {
                if (data instanceof UserStatusChange) {
                    UserStatusChange change = (UserStatusChange) data;
                    System.out.println("[EscuchadorMensajes] " +
                            (change.isActive() ? "🟢" : "🔴") + " " +
                            change.getUsername() + " → " +
                            (change.isActive() ? "CONECTADO" : "DESCONECTADO"));

                    controller.handleUserStatusChange(change.getUsername(), change.isActive());
                }
            } catch (Exception e) {
                System.err.println("[EscuchadorMensajes] ❌ Error procesando cambio: " + e.getMessage());
            }
        }

        /**
         * Manejar mensaje entrante
         */
        private void handleIncomingMessage(MensajeResponseDto mensaje) {
            System.out.println("[EscuchadorMensajes] 💬 Mensaje de: " + mensaje.getRemitente());
            controller.handleIncomingMessage(mensaje);
        }

        /**
         * Manejar respuesta de historial
         */
        private void handleHistoryResponse(List<?> mensajes) {
            try {
                @SuppressWarnings("unchecked")
                List<MensajeResponseDto> history = (List<MensajeResponseDto>) mensajes;
                System.out.println("[EscuchadorMensajes] 📜 Historial recibido: " + history.size() + " mensajes");
                controller.handleHistory(history);
            } catch (Exception e) {
                System.err.println("[EscuchadorMensajes] ❌ Error procesando historial: " + e.getMessage());
            }
        }

    }
}
