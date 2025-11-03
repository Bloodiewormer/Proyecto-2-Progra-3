package org.example.app;

import org.example.API.Controllers.*;
import org.example.DataAcces.HibernateUtil;
import org.example.DataAcces.services.*;
import org.example.Domain.models.Usuario;
import org.example.Server.ConnectionMonitor;
import org.example.Server.MessageBroadcaster;
import org.example.Server.MensajeriaServer;
import org.example.Server.SocketServer;
import org.hibernate.SessionFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTIÓN MÉDICA - BACKEND                 ║");
        System.out.println("║   Universidad Nacional - EIF206 Programación 3        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        // ========== INICIALIZACIÓN DE HIBERNATE ==========
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        System.out.println("✓ Hibernate SessionFactory inicializado");

        // ========== SERVICIOS DE DATOS ==========
        UsuarioService usuarioService = new UsuarioService(sessionFactory);
        MedicamentoService medicamentoService = new MedicamentoService(sessionFactory);
        PacienteService pacienteService = new PacienteService(sessionFactory);
        RecetaService recetaService = new RecetaService(sessionFactory);
        DashboardService dashboardService = new DashboardService(sessionFactory);
        MensajeService mensajeService = new MensajeService(sessionFactory);
        System.out.println("✓ Servicios de datos inicializados (6)");

        // ========== MONITOR DE CONEXIONES ==========
        ConnectionMonitor connectionMonitor = new ConnectionMonitor(usuarioService);
        System.out.println("✓ Monitor de conexiones con heartbeat iniciado");

        // ========== CONTROLADORES API ==========
        AuthController authController = new AuthController(usuarioService);
        MedicamentoController medicamentoController = new MedicamentoController(medicamentoService);
        PacienteController pacienteController = new PacienteController(pacienteService);
        MedicoController medicoController = new MedicoController(usuarioService);
        FarmaceutaController farmaceutaController = new FarmaceutaController(usuarioService);
        RecetaController recetaController = new RecetaController(recetaService);
        PrescribirController prescribirController = new PrescribirController(recetaService, pacienteService, medicamentoService);
        HistoricoRecetasController historicoRecetasController = new HistoricoRecetasController(recetaService, pacienteService);
        DashboardController dashboardController = new DashboardController(dashboardService);
        DespachoController despachoController = new DespachoController(recetaService, pacienteService);
        MensajeController mensajeController = new MensajeController(mensajeService, usuarioService);
        System.out.println("✓ Controladores API inicializados (11)");

        // ========== CONFIGURACIÓN DE PUERTOS ==========
        int requestPort = 7000;        // REST-like requests
        int broadcastPort = 7001;      // Notificaciones del sistema
        int mensajeriaPort = 7002;     // Chat entre usuarios

        // ========== SERVIDOR 1: PETICIONES REST (Puerto 7000) ==========
        SocketServer socketServer = new SocketServer(
                requestPort,
                authController,
                medicamentoController,
                pacienteController,
                medicoController,
                farmaceutaController,
                recetaController,
                prescribirController,
                despachoController,
                historicoRecetasController,
                dashboardController,
                mensajeController
        );

        // ========== SERVIDOR 2: NOTIFICACIONES SISTEMA (Puerto 7001) ==========
        MessageBroadcaster messageBroadcaster = new MessageBroadcaster(broadcastPort);

        // ========== SERVIDOR 3: CHAT USUARIOS (Puerto 7002) ==========
        MensajeriaServer mensajeriaServer = new MensajeriaServer(
                mensajeriaPort,
                mensajeService,
                usuarioService
        );

        // Registrar broadcaster con servidor principal
        socketServer.setMessageBroadcaster(messageBroadcaster);

        // ========== SHUTDOWN HOOK ==========
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║             APAGANDO SISTEMA                         ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");

            // Desactivar todos los usuarios activos
            try {
                List<Usuario> activeUsers = usuarioService.getActiveUsers();
                System.out.println("📋 Desactivando " + activeUsers.size() + " usuarios activos...");

                for (Usuario user : activeUsers) {
                    usuarioService.deactivateUser(user.getId());
                    System.out.println("   ✓ " + user.getNombre() + " desactivado");
                }
            } catch (Exception e) {
                System.err.println(" Error desactivando usuarios: " + e.getMessage());
            }

            // Cerrar servidores
            System.out.println("\n Cerrando servidores...");
            socketServer.stop();
            messageBroadcaster.stop();
            mensajeriaServer.stop();
            connectionMonitor.shutdown();

            // Cerrar Hibernate
            HibernateUtil.shutdown();

            System.out.println("\n Sistema cerrado correctamente");
            System.out.println("═══════════════════════════════════════════════════════════\n");
        }));

        // ========== INICIAR SERVIDORES ==========
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║             INICIANDO SERVIDORES                     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        new Thread(() -> socketServer.start(), "SocketServer-Thread").start();
        messageBroadcaster.start();
        new Thread(() -> mensajeriaServer.start(), "MensajeriaServer-Thread").start();

        System.out.println("\n📡 SERVIDORES ACTIVOS:");
        System.out.println("   🔵 Puerto " + requestPort + " → Peticiones REST (Auth, CRUD, etc.)");
        System.out.println("   🟢 Puerto " + broadcastPort + " → Notificaciones del sistema");
        System.out.println("   🟡 Puerto " + mensajeriaPort + " → Chat entre usuarios");

        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println(" SISTEMA LISTO Y ESPERANDO CONEXIONES");
        System.out.println("═══════════════════════════════════════════════════════════\n");
    }
}