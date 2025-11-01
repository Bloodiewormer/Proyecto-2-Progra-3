package org.example.Server;

import org.example.API.Controllers.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Servidor de sockets para manejar múltiples clientes del FrontEnd.
 * Cada cliente se maneja en un hilo mediante ClientHandler.
 */
public class SocketServer {

    private final int port;
    private final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());

    // Controladores API del BackEnd
    private final AuthController authController;
    private final DashboardController dashboardController;
    private final DespachoController despachoController;
    private final FarmaceutaController farmaceutaController;

    private MessageBroadcaster messageBroadcaster;
    private boolean running;
    private ServerSocket serverSocket;

    public SocketServer(int port,
                        AuthController authController,
                        DashboardController dashboardController,
                        DespachoController despachoController,
                        FarmaceutaController farmaceutaController) {
        this.port = port;
        this.authController = authController;
        this.dashboardController = dashboardController;
        this.despachoController = despachoController;
        this.farmaceutaController = farmaceutaController;
    }

    /**
     * Inicia el servidor de sockets y escucha nuevas conexiones.
     */
    public void start() {
        running = true;
        System.out.println("[SocketServer] Iniciando servidor en el puerto " + port + "...");

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("[SocketServer] Servidor escuchando en el puerto " + port);

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[SocketServer] Nuevo cliente conectado desde: " + clientSocket.getInetAddress());

                    ClientHandler handler = new ClientHandler(
                            clientSocket,
                            authController,
                            dashboardController,
                            despachoController,
                            farmaceutaController,
                            this
                    );

                    clients.add(handler);
                    new Thread(handler, "ClientHandler-" + clients.size()).start();

                } catch (IOException e) {
                    if (running) {
                        System.err.println("[SocketServer] Error aceptando cliente: " + e.getMessage());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("[SocketServer] Error en el servidor: " + e.getMessage());
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("[SocketServer] Error cerrando ServerSocket: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Detiene el servidor y cierra todas las conexiones activas.
     */
    public void stop() {
        running = false;

        synchronized (clients) {
            for (ClientHandler client : clients) {
                try {
                    // Notificar al cliente que el servidor se está deteniendo
                    client.sendMessage("[Servidor] El servidor se ha detenido.");
                } catch (Exception ignore) {}
            }
            clients.clear();
        }

        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("[SocketServer] Error cerrando servidor: " + e.getMessage());
            }
        }

        System.out.println("[SocketServer] Servidor detenido.");
    }

    /**
     * Envía un mensaje a todos los clientes conectados.
     */
    public void broadcast(Object message) {
        if (messageBroadcaster != null) {
            messageBroadcaster.broadcastToAll(message);
        } else {
            // Fallback: enviar directamente a los clientes conectados
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    try {
                        client.sendMessage(message);
                    } catch (Exception e) {
                        System.err.println("[SocketServer] Error enviando mensaje: " + e.getMessage());
                    }
                }
            }
        }
        System.out.println("[SocketServer] Broadcast enviado a todos los clientes.");
    }

    /**
     * Registra el MessageBroadcaster para notificaciones push
     */
    public void setMessageBroadcaster(MessageBroadcaster broadcaster) {
        this.messageBroadcaster = broadcaster;
        System.out.println("[SocketServer] MessageBroadcaster registrado");
    }

    /**
     * Elimina un cliente desconectado de la lista de clientes activos.
     */
    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("[SocketServer] Cliente eliminado. Conexiones activas: " + clients.size());
    }

    public int getActiveClientCount() {
        return clients.size();
    }
}