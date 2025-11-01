package org.example.Server;

import com.google.gson.Gson;
import org.example.API.Controllers.DashboardController;
import org.example.API.Controllers.HistoricoRecetasController;
import org.example.API.Controllers.PrescribirController;
import org.example.API.Controllers.RecetaController;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

import java.io.*;
import java.net.Socket;

/**
 * Clase que maneja la comunicación entre un cliente y el servidor.
 * Recibe peticiones JSON, las interpreta y las redirige al controlador API correspondiente.
 */
public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final DashboardController dashboardController;
    private final HistoricoRecetasController historicoRecetasController;
    private final PrescribirController prescribirController;
    private final RecetaController recetaController;
    private final SocketServer server;
    private final Gson gson = new Gson();
    private PrintWriter out;

    public ClientHandler(Socket clientSocket,
                         DashboardController dashboardController,
                         HistoricoRecetasController historicoRecetasController,
                         PrescribirController prescribirController,
                         RecetaController recetaController,
                         SocketServer server) {
        this.clientSocket = clientSocket;
        this.dashboardController = dashboardController;
        this.historicoRecetasController = historicoRecetasController;
        this.prescribirController = prescribirController;
        this.recetaController = recetaController;
        this.server = server;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.println("[ClientHandler] Cliente conectado: " + Thread.currentThread().getName());

            String inputJson;
            while ((inputJson = in.readLine()) != null) {
                System.out.println("[ClientHandler] Recibido (" + Thread.currentThread().getName() + "): " + inputJson);

                RequestDto request = gson.fromJson(inputJson, RequestDto.class);
                ResponseDto response = handleRequest(request);

                // Simulación de procesamiento
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                out.println(gson.toJson(response));
            }
        } catch (IOException e) {
            System.err.println("[ClientHandler] Cliente desconectado: " + Thread.currentThread().getName());
        } finally {
            try { clientSocket.close(); } catch (IOException ignore) {}
            server.removeClient(this);
        }
    }

    /**
     * Encaminamiento de peticiones a los controladores API correspondientes.
     */
    private ResponseDto handleRequest(RequestDto request) {
        if (request == null || request.getController() == null) {
            return new ResponseDto(false, "Petición inválida o sin controlador", null);
        }

        ResponseDto response;
        switch (request.getController()) {
            case "Dashboard":
                response = dashboardController.route(request);
                break;

            case "HistoricoRecetas":
                response = historicoRecetasController.route(request);
                break;

            case "Prescribir":
                response = prescribirController.route(request);
                break;

            case "Receta":
                response = recetaController.route(request);
                break;

            default:
                response = new ResponseDto(false, "Controlador desconocido: " + request.getController(), null);
        }

        return response;
    }

    /**
     * Envía un mensaje JSON a este cliente específico.
     */
    public void sendMessage(Object message) {
        if (out != null) {
            String jsonMessage = gson.toJson(message);
            out.println(jsonMessage);
            System.out.println("[ClientHandler] Enviado a " + Thread.currentThread().getName() + ": " + jsonMessage);
        }
    }
}

// Revisar