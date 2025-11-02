package org.example.app;

import org.example.API.Controllers.*;
import org.example.DataAcces.HibernateUtil;
import org.example.DataAcces.services.*;
import org.example.Server.MessageBroadcaster;
import org.example.Server.SocketServer;
import org.hibernate.SessionFactory;

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

        // Inicializar controladores API
        AuthController authController = new AuthController(usuarioService);
        MedicamentoController medicamentoController = new MedicamentoController(medicamentoService);
        PacienteController pacienteController = new PacienteController(pacienteService);
        MedicoController medicoController = new MedicoController(usuarioService);
        FarmaceutaController farmaceutaController = new FarmaceutaController(usuarioService);
        RecetaController recetaController = new RecetaController(recetaService);
        PrescribirController prescribirController = new PrescribirController(recetaService, pacienteService, medicamentoService);
        HistoricoRecetasController historicoRecetasController = new HistoricoRecetasController(recetaService, pacienteService);
        DashboardController dashboardController = new DashboardController(dashboardService); // ← NUEVO
        System.out.println("✓ Controladores API inicializados");

        // Configurar puertos
        int requestPort = 7000;
        int messagePort = 7001;

        // Crear servidores
        SocketServer socketServer = new SocketServer(
                requestPort,
                authController,
                medicamentoController,
                pacienteController,
                medicoController,
                farmaceutaController,
                recetaController,
                prescribirController,
                historicoRecetasController,
                dashboardController  // ← AGREGAR AQUÍ
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
}