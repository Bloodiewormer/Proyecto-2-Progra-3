package org.example.Server;

import org.example.DataAcces.services;
import services.MensajeriaService;
import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ManejadorCliente implements Runnable {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private Gson gson;
    private String usuarioId;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                procesarMensaje(mensaje);
            }
        } catch (IOException e) {
            System.err.println("Error en cliente: " + e.getMessage());
        } finally {
            desconectar();
        }
    }

    @SuppressWarnings("unchecked")
    private void procesarMensaje(String mensajeJson) {
        try {
            Map<String, Object> mensaje = gson.fromJson(mensajeJson, Map.class);
            String tipo = (String) mensaje.get("tipo");

            switch (tipo) {
                case "conectar":
                    usuarioId = (String) mensaje.get("usuarioId");
                    String nombre = (String) mensaje.get("nombre");
                    GestionUsuariosService.getInstance().conectarUsuario(usuarioId, nombre);
                    break;

                case "enviar_mensaje":
                    String destinatarioId = (String) mensaje.get("destinatarioId");
                    String contenido = (String) mensaje.get("contenido");
                    MensajeriaService.getInstance().enviarMensaje(usuarioId, destinatarioId, contenido);
                    break;

                case "solicitar_usuarios":
                    enviarListaUsuarios();
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
        }
    }

    private void enviarListaUsuarios() {
        // Implementar envío de lista de usuarios conectados
    }

    private void desconectar() {
        if (usuarioId != null) {
            GestionUsuariosService.getInstance().desconectarUsuario(usuarioId);
        }
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar socket: " + e.getMessage());
        }
    }
}
