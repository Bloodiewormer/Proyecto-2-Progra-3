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
    private Thread acceptorThread;

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
            acceptorThread = new Thread(this::acceptConnections, "MessageBroadcaster-Acceptor");
            acceptorThread.start();

        } catch (IOException e) {
            System.err.println("[MessageBroadcaster] Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Acepta las conexiones entrantes de los clientes que quieren recibir notificaciones.
     */
    private void acceptConnections() {
        try {
            while (running && !serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[MessageBroadcaster] Nuevo cliente de mensajes conectado desde: "
                            + clientSocket.getInetAddress());

                    BroadcastClient client = new BroadcastClient(clientSocket);
                    connectedClients.add(client);

                    System.out.println("[MessageBroadcaster] Clientes de difusión activos: " + connectedClients.size());

                } catch (IOException e) {
                    if (running) {
                        System.err.println("[MessageBroadcaster] Error al aceptar conexión: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            if (running) {
                System.err.println("[MessageBroadcaster] Error en loop de aceptación: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Envía un mensaje (en formato JSON) a todos los clientes conectados.
     */
    public void broadcastToAll(Object message) {
        String jsonMessage = gson.toJson(message);
        System.out.println("[MessageBroadcaster] Enviando mensaje a " + connectedClients.size() + " clientes: " + jsonMessage);

        // Crear una lista de clientes a remover (los que fallen al enviar)
        List<BroadcastClient> clientsToRemove = new CopyOnWriteArrayList<>();

        for (BroadcastClient client : connectedClients) {
            if (!client.sendMessage(jsonMessage)) {
                clientsToRemove.add(client);
            }
        }

        // Remover clientes desconectados
        for (BroadcastClient client : clientsToRemove) {
            connectedClients.remove(client);
            client.close();
            System.out.println("[MessageBroadcaster] Cliente desconectado removido. Activos: " + connectedClients.size());
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

        // Cerrar todos los clientes conectados
        for (BroadcastClient client : connectedClients) {
            client.close();
        }
        connectedClients.clear();

        // Esperar a que el thread termine
        if (acceptorThread != null && acceptorThread.isAlive()) {
            try {
                acceptorThread.join(2000); // Esperar máximo 2 segundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Representa a un cliente que está escuchando notificaciones de difusión.
     */
    private class BroadcastClient {
        private final Socket socket;
        private final PrintWriter out;

        public BroadcastClient(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }

        /**
         * Envía un mensaje al cliente.
         * @return true si se envió exitosamente, false si hubo error
         */
        public boolean sendMessage(String message) {
            if (out != null && !out.checkError()) {
                out.println(message);
                return !out.checkError();
            }
            return false;
        }

        public void close() {
            try {
                if (out != null) {
                    out.close();
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("[BroadcastClient] Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}