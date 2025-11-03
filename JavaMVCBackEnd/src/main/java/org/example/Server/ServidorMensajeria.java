package org.example.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorMensajeria {
    private static final int PUERTO = 8080;
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private boolean ejecutando;

    public ServidorMensajeria() {
        this.pool = Executors.newCachedThreadPool();
        this.ejecutando = false;
    }

    public void iniciar() {
        try {
            serverSocket = new ServerSocket(PUERTO);
            ejecutando = true;
            System.out.println("Servidor de mensajería iniciado en puerto " + PUERTO);

            while (ejecutando) {
                Socket clienteSocket = serverSocket.accept();
                ManejadorCliente manejador = new ManejadorCliente(clienteSocket);
                pool.execute(manejador);
            }
        } catch (IOException e) {
            System.err.println("Error en servidor: " + e.getMessage());
        }
    }

    public void detener() {
        ejecutando = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            pool.shutdown();
        } catch (IOException e) {
            System.err.println("Error al detener servidor: " + e.getMessage());
        }
    }
}
