package org.example.Server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Servidor auxiliar encargado de difundir mensajes de notificación a todos los clientes
 * conectados en tiempo real (por ejemplo: actualizaciones de recetas o pacientes).
 */
public class MessageBroadcaster {

    private final int port;
    private ServerSocket serverSocket;
    private final List<BroadcastClient> connectedClients = new CopyOnWriteArrayList<>();
    private final Gson gson = new Gson();
    private boolean running = false;

    public MessageBroadcaster(int port) {
        this.port = port;
    }

    /**
     * Inicia el servidor de difusión (en un hilo separado).
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("[MessageBroadcaster] Servidor de difusión iniciado en el puerto " + port);

            // Aceptar conexiones en un hilo aparte
            new Thread(this::acceptConnections, "MessageBroadcaster-Acceptor").start();

        } catch (IOException e) {
            System.err.println("[MessageBroadcaster] Error al iniciar el servidor: " + e.getMessage());
        }
    }

    /**
     * Acepta las conexiones entrantes de los clientes que quieren recibir notificaciones.
     */
    private void acceptConnections() {
        try {
            while (running && !serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[MessageBroadcaster] Nuevo cliente de mensajes conectado desde: "
                        + clientSocket.getInetAddress());

                BroadcastClient client = new BroadcastClient(clientSocket);
                connectedClients.add(client);

                System.out.println("[MessageBroadcaster] Clientes de difusión activos: " + connectedClients.size());
            }
        } catch (IOException e) {
            if (running)
                System.err.println("[MessageBroadcaster] Error al aceptar conexión: " + e.getMessage());
        }
    }

    /**
     * Envía un mensaje (en formato JSON) a todos los clientes conectados.
     */
    public void broadcastToAll(Object message) {
        String jsonMessage = gson.toJson(message);
        System.out.println("[MessageBroadcaster] Enviando mensaje a " + connectedClients.size() + " clientes: " + jsonMessage);

        for (BroadcastClient client : connectedClients) {
            client.sendMessage(jsonMessage);
        }
    }

    /**
     * Devuelve la cantidad actual de clientes conectados.
     */
    public int getConnectedClientCount() {
        return connectedClients.size();
    }

    /**
     * Detiene el servidor de difusión y cierra todas las conexiones.
     */
    public void stop() {
        running = false;

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("[MessageBroadcaster] Servidor de difusión detenido.");
            }
        } catch (IOException e) {
            System.err.println("[MessageBroadcaster] Error al detener el servidor: " + e.getMessage());
        }

        for (BroadcastClient client : connectedClients) {
            client.close();
        }
        connectedClients.clear();
    }

    /**
     * Representa a un cliente que está escuchando notificaciones de difusión.
     */
    private static class BroadcastClient {
        private final Socket socket;
        private final PrintWriter out;

        public BroadcastClient(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }

        public void sendMessage(Object message) {
            if (out != null && !out.checkError()) {
                out.println(message);
            }
        }

        public void close() {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("[BroadcastClient] Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}

// Revisar