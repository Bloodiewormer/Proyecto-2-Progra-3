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
            socket.setSoTimeout(30000);

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
    public void solicitarHistorial(ListMensajeResponseDto dto) {
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

    class EscuchadorMensajes implements Runnable {
        @Override
        public void run() {
            System.out.println("👂 Escuchador de mensajes iniciado para: " + username);

            try {
                Object objeto;
                while (conectado && !Thread.currentThread().isInterrupted()) {
                    try {
                        objeto = in.readObject();
                        procesarObjeto(objeto);
                    } catch (EOFException e) {
                        System.err.println("⚠️ Fin de stream alcanzado");
                        break;
                    }
                }

            } catch (IOException e) {
                if (conectado) {
                    System.err.println("❌ Conexión perdida: " + e.getMessage());
                }
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Clase desconocida: " + e.getMessage());
            } finally {
                if (conectado) {
                    desconectar();
                }
            }
        }

        @SuppressWarnings("unchecked")
        private void procesarObjeto(Object objeto) {
            if (objeto instanceof MensajeResponseDto) {
                MensajeResponseDto mensaje = (MensajeResponseDto) objeto;
                System.out.println("📨 Mensaje recibido de: " + mensaje.getRemitente());
                controller.handleIncomingMessage(mensaje);
            }
            else if (objeto instanceof ListMensajeResponseDto) {
                ListMensajeResponseDto listDto = (ListMensajeResponseDto) objeto;
                System.out.println("📜 Historial recibido: " + listDto.getMensajes().size() + " mensajes");
                controller.handleHistory(listDto.getMensajes());
            }
            else if (objeto instanceof List<?>) {
                List<?> lista = (List<?>) objeto;
                if (!lista.isEmpty() && lista.get(0) instanceof String) {
                    System.out.println("👥 Lista de usuarios recibida: " + lista.size());
                    controller.handleUsersList((List<String>) lista);
                }
            }
            else if (objeto instanceof EstadoUsuarioDto) {
                EstadoUsuarioDto estado = (EstadoUsuarioDto) objeto;
                System.out.println("🔄 Cambio de estado: " + estado.getUsername() +
                        " -> " + (estado.isActivo() ? "ACTIVO" : "INACTIVO"));
                controller.handleUserStatusChange(estado.getUsername(), estado.isActivo());
            }
            else {
                System.err.println("⚠️ Objeto desconocido: " + objeto.getClass().getName());
            }
        }
    }
}
