package org.example.Server;

import com.google.gson.Gson;
import org.example.API.Controllers.*;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;

import java.io.*;
import java.net.Socket;

/**
 * Clase que maneja la comunicación entre un cliente y el servidor.
 * Recibe peticiones JSON, las interpreta y las redirige al controlador API correspondiente.
 */
public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final AuthController authController;
    private final MedicamentoController medicamentoController;
    private final PacienteController pacienteController;
    private final MedicoController medicoController;
    private final RecetaController recetaController;
    private final PrescribirController prescribirController;
    private final FarmaceutaController farmaceutaController;
    private final HistoricoRecetasController historicoRecetasController;
    private final DashboardController dashboardController;
    private final DespachoController despachoController;
    private final SocketServer server;
    private final MensajeController mensajeController;
    private final Gson gson = new Gson();
    private PrintWriter out;
    private BufferedReader in;


    public ClientHandler(Socket clientSocket,
                         AuthController authController,
                         MedicamentoController medicamentoController,
                         PacienteController pacienteController,
                         MedicoController medicoController,
                         FarmaceutaController farmaceutaController,
                         RecetaController recetaController,
                         PrescribirController prescribirController,
                         DespachoController despachoController,
                         HistoricoRecetasController historicoRecetasController,
                         DashboardController dashboardController,
                         MensajeController mensajeController,
                         SocketServer server) {
        this.clientSocket = clientSocket;
        this.authController = authController;
        this.medicamentoController = medicamentoController;
        this.pacienteController = pacienteController;
        this.medicoController = medicoController;
        this.farmaceutaController = farmaceutaController;
        this.recetaController = recetaController;
        this.prescribirController = prescribirController;
        this.historicoRecetasController = historicoRecetasController;
        this.dashboardController = dashboardController;
        this.despachoController = despachoController;
        this.mensajeController = mensajeController;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            System.out.println("[ClientHandler] Cliente conectado: " + Thread.currentThread().getName());

            String inputJson;
            while ((inputJson = in.readLine()) != null) {
                System.out.println("[ClientHandler] Recibido (" + Thread.currentThread().getName() + "): " + inputJson);

                try {
                    RequestDto request = gson.fromJson(inputJson, RequestDto.class);
                    ResponseDto response = handleRequest(request);

                    String responseJson = gson.toJson(response);
                    out.println(responseJson);
                    System.out.println("[ClientHandler] Enviado (" + Thread.currentThread().getName() + "): " + responseJson);

                } catch (Exception e) {
                    System.err.println("[ClientHandler] Error procesando petición: " + e.getMessage());
                    e.printStackTrace();

                    ResponseDto errorResponse = new ResponseDto(false, "Error interno del servidor: " + e.getMessage(), null);
                    out.println(gson.toJson(errorResponse));
                }
            }

        } catch (IOException e) {
            System.err.println("[ClientHandler] Cliente desconectado: " + Thread.currentThread().getName());
        } finally {
            cleanup();
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
        try {
            switch (request.getController()) {
                case "Auth":
                    response = authController.route(request);
                    break;

                case "Medicamento":
                case "Medicamentos":
                    response = medicamentoController.route(request);
                    break;

                case "Paciente":
                case "Pacientes":
                    response = pacienteController.route(request);
                    break;

                case "Medico":
                case "Medicos":
                    response = medicoController.route(request);
                    break;

                case "Farmaceutas":
                case "Farmaceuta":
                    response = farmaceutaController.route(request);
                    break;

                case "Receta":
                case "Recetas":
                    response = recetaController.route(request);
                    break;

                case "Prescribir":
                case "Prescripcion":
                    response = prescribirController.route(request);
                    break;

                case "HistoricoRecetas":
                case "Historico":
                    response = historicoRecetasController.route(request);
                    break;

                case "Mensaje":
                case "Mensajes":
                    response = mensajeController.route(request);
                    break;

                case "Dashboard":
                    response = dashboardController.handleRequest(request);
                    break;

                case "Despacho":
                    return despachoController.route(request);


                default:
                    response = new ResponseDto(false, "Controlador desconocido: " + request.getController(), null);
            }
        } catch (Exception e) {
            System.err.println("[ClientHandler] Error en controlador " + request.getController() + ": " + e.getMessage());
            e.printStackTrace();
            response = new ResponseDto(false, "Error procesando petición: " + e.getMessage(), null);
        }

        return response;
    }

    /**
     * Envía un mensaje JSON a este cliente específico.
     */
    public void sendMessage(Object message) {
        if (out != null) {
            try {
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                System.out.println("[ClientHandler] Enviado a " + Thread.currentThread().getName() + ": " + jsonMessage);
            } catch (Exception e) {
                System.err.println("[ClientHandler] Error enviando mensaje: " + e.getMessage());
            }
        }
    }

    /**
     * Limpia recursos al desconectar cliente
     */
    private void cleanup() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("[ClientHandler] Error en cleanup: " + e.getMessage());
        }

        server.removeClient(this);
        System.out.println("[ClientHandler] Recursos liberados para: " + Thread.currentThread().getName());
    }
}