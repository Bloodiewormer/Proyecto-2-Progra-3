package org.example.Server;

import org.example.API.Controllers.DashboardController;
import org.example.API.Controllers.HistoricoRecetasController;
import org.example.API.Controllers.PrescribirController;
import org.example.API.Controllers.RecetaController;

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
    private final DashboardController dashboardController;
    private final HistoricoRecetasController historicoRecetasController;
    private final PrescribirController prescribirController;
    private final RecetaController recetaController;

    private boolean running;

    public SocketServer(int port,
                        DashboardController dashboardController,
                        HistoricoRecetasController historicoRecetasController,
                        PrescribirController prescribirController,
                        RecetaController recetaController) {
        this.port = port;
        this.dashboardController = dashboardController;
        this.historicoRecetasController = historicoRecetasController;
        this.prescribirController = prescribirController;
        this.recetaController = recetaController;
    }

    /**
     * Inicia el servidor de sockets y escucha nuevas conexiones.
     */
    public void start() {
        running = true;
        System.out.println("[SocketServer] Iniciando servidor en el puerto " + port + "...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[SocketServer] Servidor escuchando en el puerto " + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SocketServer] Nuevo cliente conectado desde: " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(
                        clientSocket,
                        dashboardController,
                        historicoRecetasController,
                        prescribirController,
                        recetaController,
                        this
                );

                clients.add(handler);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.err.println("[SocketServer] Error en el servidor: " + e.getMessage());
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
                    client.sendMessage("[Servidor] El servidor se ha detenido.");
                } catch (Exception ignore) {}
            }
            clients.clear();
        }
        System.out.println("[SocketServer] Servidor detenido.");
    }

    /**
     * Envía un mensaje a todos los clientes conectados.
     */
    public void broadcast(Object message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
        System.out.println("[SocketServer] Broadcast enviado a todos los clientes.");
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

// Revisar