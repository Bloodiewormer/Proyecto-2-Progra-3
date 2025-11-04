package org.example.Server;

import org.example.DataAcces.services.MensajeService;
import org.example.DataAcces.services.UsuarioService;
import org.example.Domain.Dtos.Mensaje.AddMensajeRequestDto;
import org.example.Domain.Dtos.Mensaje.ListMensajesRequestDto;
import org.example.Domain.Dtos.Mensaje.MensajeResponseDto;
import org.example.Domain.Dtos.Mensaje.NotificationMessage;
import org.example.Domain.Dtos.Mensaje.UserStatusChange;
import org.example.Domain.models.Mensaje;
import org.example.Domain.models.Usuario;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Servidor especializado para mensajería en tiempo real (Puerto 7002)
 * Mantiene conexiones persistentes con clientes para:
 * - Chat entre usuarios
 * - Notificaciones de usuarios activos/inactivos
 * - Envío/recepción de mensajes instantáneos
 */
public class MensajeriaServer {
    private final int port;
    private final MensajeService mensajeService;
    private final UsuarioService usuarioService;

    private ServerSocket serverSocket;
    private boolean running = false;

    // Mapeo de usuarios conectados: username -> handler
    private final Map<String, ClienteMensajeriaHandler> connectedClients = new ConcurrentHashMap<>();
    private final List<ClienteMensajeriaHandler> allClients = new CopyOnWriteArrayList<>();

    public MensajeriaServer(int port, MensajeService mensajeService, UsuarioService usuarioService) {
        this.port = port;
        this.mensajeService = mensajeService;
        this.usuarioService = usuarioService;
    }

    /**
     * Inicia el servidor de mensajería
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("[MensajeriaServer] 🚀 Servidor iniciado en puerto " + port);

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[MensajeriaServer] 📱 Nueva conexión desde: " +
                            clientSocket.getInetAddress());

                    ClienteMensajeriaHandler handler = new ClienteMensajeriaHandler(clientSocket);
                    allClients.add(handler);
                    new Thread(handler, "MensajeriaHandler-" + allClients.size()).start();

                } catch (IOException e) {
                    if (running) {
                        System.err.println("[MensajeriaServer] ❌ Error aceptando cliente: " +
                                e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[MensajeriaServer] ❌ Error iniciando servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Detiene el servidor
     */
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            for (ClienteMensajeriaHandler client : allClients) {
                client.close();
            }
            System.out.println("[MensajeriaServer] 🛑 Servidor detenido");
        } catch (IOException e) {
            System.err.println("[MensajeriaServer] ❌ Error deteniendo: " + e.getMessage());
        }
    }

    /**
     * 📢 Broadcast: notificar a TODOS los usuarios conectados
     */
    private void broadcastNotification(String type, Object data) {
        NotificationMessage notification = new NotificationMessage(type, data);

        for (ClienteMensajeriaHandler client : allClients) {
            if (client.isConnected()) {
                client.send(notification);
            }
        }
        System.out.println("[MensajeriaServer] 📢 Broadcast: " + type +
                " → " + allClients.size() + " clientes");
    }

    /**
     * 👤 Unicast: enviar mensaje a un usuario específico
     */
    private void sendToUser(String username, Object message) {
        ClienteMensajeriaHandler client = connectedClients.get(username);
        if (client != null && client.isConnected()) {
            client.send(message);
            System.out.println("[MensajeriaServer] 📨 Mensaje enviado a: " + username);
        }
    }

    /**
     * 📋 Enviar lista de usuarios activos a un cliente
     */
    private void sendActiveUsersToClient(ClienteMensajeriaHandler client) {
        try {
            // ✅ Obtener TODOS los usuarios (con is_active = true)
            List<Usuario> allUsers = usuarioService.getAllActiveUsers();

            List<String> usernames = allUsers.stream()
                    .map(Usuario::getNombre)
                    .toList();

            NotificationMessage notification = new NotificationMessage("USERS_LIST", usernames);
            client.send(notification);
            System.out.println("[MensajeriaServer] 📋 Lista enviada: " + usernames.size() + " usuarios totales");
        } catch (Exception e) {
            System.err.println("[MensajeriaServer] ❌ Error enviando lista: " + e.getMessage());
        }
    }

    /**
     * 🔌 Handler para cada cliente conectado
     */
    private class ClienteMensajeriaHandler implements Runnable {
        private final Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private String username;
        private boolean connected = false;

        public ClienteMensajeriaHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Inicializar streams (¡orden importa!)
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());

                // 🔐 Autenticación: leer username del cliente
                username = (String) in.readObject();
                connectedClients.put(username, this);
                connected = true;

                System.out.println("[MensajeriaHandler] ✅ Autenticado: " + username);

                // Enviar lista inicial de usuarios activos
                sendActiveUsersToClient(this);

                // 📢 Notificar a TODOS que este usuario se conectó
                broadcastNotification("USER_STATUS_CHANGE",
                        new UserStatusChange(username, true));

                // 🔄 Loop principal: escuchar peticiones del cliente
                while (connected) {
                    try {
                        Object obj = in.readObject();

                        if (obj instanceof AddMensajeRequestDto) {
                            handleSendMessage((AddMensajeRequestDto) obj);
                        } else if (obj instanceof ListMensajesRequestDto) {
                            handleGetHistory((ListMensajesRequestDto) obj);
                        } else {
                            System.out.println("[MensajeriaHandler] ⚠️ Objeto desconocido: " +
                                    obj.getClass().getName());
                        }

                    } catch (EOFException e) {
                        System.out.println("[MensajeriaHandler] 📴 Cliente desconectado: " + username);
                        break;
                    } catch (ClassNotFoundException e) {
                        System.err.println("[MensajeriaHandler] ❌ Clase no encontrada: " +
                                e.getMessage());
                    }
                }

            } catch (Exception e) {
                System.err.println("[MensajeriaHandler] ❌ Error en handler: " + e.getMessage());
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }

        /**
         * 💬 Manejar envío de mensaje
         */
        private void handleSendMessage(AddMensajeRequestDto dto) {
            try {
                System.out.println("[MensajeriaHandler] 💬 Mensaje de: " + dto.getRemitente() +
                        " → " + dto.getDestinatario());

                // Buscar usuarios por nombre
                Usuario sender = usuarioService.getUserByNombre(dto.getRemitente());
                Usuario recipient = usuarioService.getUserByNombre(dto.getDestinatario());

                if (sender == null || recipient == null) {
                    System.err.println("[MensajeriaHandler] ❌ Usuario no encontrado");
                    return;
                }

                // Guardar en base de datos
                Mensaje mensaje = mensajeService.enviarMensaje(
                        sender.getId(),
                        recipient.getId(),
                        dto.getContenido()
                );

                // Crear DTO de respuesta
                MensajeResponseDto responseDto = new MensajeResponseDto(
                        mensaje.getId(),
                        sender.getNombre(),
                        recipient.getNombre(),
                        mensaje.getMessage(),
                        mensaje.getStatus(),
                        mensaje.getCreatedAt() != null ? mensaje.getCreatedAt().toString() : null,  // ✅ Convertir a String
                        mensaje.getReadAt() != null ? mensaje.getReadAt().toString() : null         // ✅ Convertir a String
                );


                // Enviar al destinatario si está conectado
                sendToUser(dto.getDestinatario(), responseDto);

                System.out.println("[MensajeriaHandler] ✅ Mensaje guardado y enviado");

            } catch (Exception e) {
                System.err.println("[MensajeriaHandler] ❌ Error enviando mensaje: " +
                        e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * 📜 Manejar solicitud de historial
         */
        private void handleGetHistory(ListMensajesRequestDto dto) {
            try {
                System.out.println("[MensajeriaHandler] 📜 Historial solicitado: " +
                        dto.getUsuario1() + " ↔ " + dto.getUsuario2());

                Usuario user1 = usuarioService.getUserByNombre(dto.getUsuario1());
                Usuario user2 = usuarioService.getUserByNombre(dto.getUsuario2());

                if (user1 == null || user2 == null) {
                    System.err.println("[MensajeriaHandler] ❌ Usuario no encontrado");
                    return;
                }

                List<Mensaje> mensajes = mensajeService.obtenerHistorial(user1.getId(), user2.getId());
                List<MensajeResponseDto> responseDtos = new CopyOnWriteArrayList<>();

                for (Mensaje mensaje : mensajes) {
                    Usuario sender = usuarioService.getUserById(mensaje.getSenderId());
                    Usuario recipient = usuarioService.getUserById(mensaje.getRecipientId());

                    if (sender != null && recipient != null) {
                        responseDtos.add(new MensajeResponseDto(
                                mensaje.getId(),
                                sender.getNombre(),
                                recipient.getNombre(),
                                mensaje.getMessage(),
                                mensaje.getStatus(),
                                mensaje.getCreatedAt() != null ? mensaje.getCreatedAt().toString() : null,  // ✅ Convertir a String
                                mensaje.getReadAt() != null ? mensaje.getReadAt().toString() : null         // ✅ Convertir a String
                        ));
                    }
                }

                send(responseDtos);
                System.out.println("[MensajeriaHandler] ✅ Historial enviado: " +
                        responseDtos.size() + " mensajes");

            } catch (Exception e) {
                System.err.println("[MensajeriaHandler] ❌ Error historial: " + e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * 📤 Enviar objeto al cliente
         */
        public void send(Object message) {
            try {
                if (out != null && connected) {
                    synchronized (out) {
                        out.writeObject(message);
                        out.flush();
                    }
                }
            } catch (IOException e) {
                System.err.println("[MensajeriaHandler] ❌ Error enviando a " + username);
                disconnect();
            }
        }

        public boolean isConnected() {
            return connected && socket != null && !socket.isClosed();
        }

        public void close() {
            disconnect();
        }

        /**
         * 🔌 Desconectar cliente
         */
        private void disconnect() {
            if (!connected) return;

            connected = false;
            connectedClients.remove(username);
            allClients.remove(this);

            // Notificar a TODOS que este usuario se desconectó
            if (username != null) {
                broadcastNotification("USER_STATUS_CHANGE",
                        new UserStatusChange(username, false));
                System.out.println("[MensajeriaHandler] 👋 Desconectado: " + username);
            }

            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.println("[MensajeriaHandler] ❌ Error cerrando: " + e.getMessage());
            }
        }
    }


}