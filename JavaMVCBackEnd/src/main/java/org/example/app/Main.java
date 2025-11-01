package org.example.app;

import org.example.API.Controllers.*;
import org.example.DataAcces.HibernateUtil;
import org.example.DataAcces.services.*;
import org.example.Domain.dtos.Receta.RecetaResponseDto;
import org.example.Domain.dtos.Farmaceuta.FarmaceutaResponseDto;
import org.example.Server.MessageBroadcaster;
import org.example.Server.SocketServer;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Iniciando Sistema de Gestión Médica ===");

        // Obtener SessionFactory de Hibernate
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        System.out.println("✓ Hibernate SessionFactory inicializado");

        // Inicializar servicios de datos
        UsuarioService usuarioService = new UsuarioService(sessionFactory);
        MedicamentoService medicamentoService = new MedicamentoService(sessionFactory);
        PacienteService pacienteService = new PacienteService(sessionFactory);
        RecetaService recetaService = new RecetaService(sessionFactory);
        DashboardService dashboardService = new DashboardService(sessionFactory);
        System.out.println("✓ Servicios de datos inicializados");

        // Obtener datos para controladores que los necesitan
        List<RecetaResponseDto> recetas = obtenerRecetasParaControladores(recetaService);
        List<FarmaceutaResponseDto> farmaceutas = obtenerFarmaceutasParaControladores(usuarioService);
        System.out.println("✓ Datos iniciales cargados");

        // Inicializar controladores API
        AuthController authController = new AuthController(usuarioService);
        DashboardController dashboardController = new DashboardController(recetas);
        DespachoController despachoController = new DespachoController(recetas);
        FarmaceutaController farmaceutaController = new FarmaceutaController(farmaceutas);
        System.out.println("✓ Controladores API inicializados");

        // Configurar puertos
        int requestPort = 7000;
        int messagePort = 7001;

        // Crear servidores
        SocketServer socketServer = new SocketServer(
                requestPort,
                authController,
                dashboardController,
                despachoController,
                farmaceutaController
        );

        MessageBroadcaster messageBroadcaster = new MessageBroadcaster(messagePort);

        // Registrar broadcaster con el servidor
        socketServer.setMessageBroadcaster(messageBroadcaster);

        // Hook para apagado limpio
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n=== Apagando sistema ===");
            socketServer.stop();
            messageBroadcaster.stop();
            HibernateUtil.shutdown();
            System.out.println("✓ Sistema apagado correctamente");
        }));

        // Iniciar servidores
        System.out.println("\n=== Iniciando servidores ===");
        new Thread(() -> socketServer.start(), "SocketServer-Thread").start();
        messageBroadcaster.start();

        System.out.println("✓ Servidor de peticiones: puerto " + requestPort);
        System.out.println("✓ Servidor de mensajes: puerto " + messagePort);
        System.out.println("\n=== Sistema listo y esperando conexiones ===\n");
    }

    /**
     * Obtener recetas para los controladores que las necesitan
     */
    private static List<RecetaResponseDto> obtenerRecetasParaControladores(RecetaService recetaService) {
        // Por ahora retornamos lista vacía, pero podrías cargar desde BD
        // o desde archivos XML si fuera necesario
        return new ArrayList<>();
    }

    /**
     * Obtener farmaceutas para el controlador
     */
    private static List<FarmaceutaResponseDto> obtenerFarmaceutasParaControladores(UsuarioService usuarioService) {
        List<FarmaceutaResponseDto> farmaceutas = new ArrayList<>();

        try {
            // Cargar farmaceutas de la base de datos
            usuarioService.getAllFarmaceutas().forEach(f -> {
                farmaceutas.add(new FarmaceutaResponseDto(
                        f.getId().intValue(),
                        f.getNombre()
                ));
            });
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudieron cargar farmaceutas: " + e.getMessage());
        }

        return farmaceutas;
    }
}