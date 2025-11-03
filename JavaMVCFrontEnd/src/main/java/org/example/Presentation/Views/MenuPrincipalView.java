package org.example.Presentation.Views;

import org.example.Presentation.Components.CustomButton;
import org.example.Presentation.Controllers.*;
import org.example.Presentation.Models.UserType;
import org.example.Services.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;
import java.util.function.Function;

public class MenuPrincipalView extends JFrame {

    private JPanel mainPanel;
    private JPanel collapsiblePanel;
    @SuppressWarnings("unused")
    private JPanel optionsPanel;
    private JPanel contentPanel;

    private JButton salirButton;
    private JButton medicosButton;
    private JButton farmaceutasButton;
    private JButton pacientesButton;
    private JButton medicamentosButton;
    private JButton dashboardButton;
    private JButton acercadeButton;
    private JButton prescribirButton;
    private JLabel toggleButton;
    private JLabel MenuLabel;
    private JButton despachoButton;
    private JButton historicoRecetasButton;
    private JButton mensajesButton;

    private boolean menuVisible = true;
    private static final int MENU_WIDTH = 170;
    private static final int MENU_COLLAPSED_WIDTH = 30;

    private final UsuarioService usuarioService;
    private final PacienteService pacienteService;
    private final MedicamentoService medicamentoService;
    private final int userId;
    private final UserType userType;
    private final LoginController loginController;

    private MedicoForm medicoForm;
    private MedicoController medicoController;

    private MedicamentoForm medicamentoForm;
    private MedicamentoController medicamentoController;

    private PacienteController pacienteController;
    private PacienteForm pacienteForm;

    private FarmaceutaForm farmaceutaForm;
    private FarmaceutaController farmaceutaController;

    private DashboardView dashboardView;
    private DashboardController dashboardController;
    private DashboardService dashboardService;

    private HistoricoRecetasView historicoRecetasView;
    private HistoricoRecetasController historicoRecetasController;

    private PrescribirForm prescribirForm;
    private PrescribirController prescribirController;
    private PrescribirService prescribirService;

    private DespachoService despachoService;
    private DespachoForm despachoForm;

    // Sistema de mensajería
    private MensajesView mensajesView;
    private MensajeController mensajeController;

    public MenuPrincipalView(UserType userType,
                             LoginController loginController,
                             UsuarioService usuarioService,
                             PacienteService pacienteService,
                             MedicamentoService medicamentoService,
                             DashboardService dashboardService,
                             PrescribirService prescribirService,
                             DespachoService despachoService,
                             int userId) {

        this.userType = userType;
        this.loginController = loginController;
        this.usuarioService = usuarioService;
        this.pacienteService = pacienteService;
        this.medicamentoService = medicamentoService;
        this.dashboardService = dashboardService;
        this.userId = userId;
        this.prescribirService = prescribirService;
        this.despachoService = despachoService;

        initializeUI();
        configureMenu();
        initializeMedicoView();
        initializePacienteView();
        initializeFarmaceutaView();
        initializeDespachoView();
        initializeHistoricoRecetasView();
        initializeMedicamentoView();
        initializeDashboardView();
        initializePrescribirView();
        initializeMensajesView();  // Nueva inicialización
        wireEvents();
        initializeView();
    }

    private void initializeDespachoView() {
        despachoForm = new DespachoForm(despachoService, userId, this);
    }

    private void initializePrescribirView() {
        prescribirForm = new PrescribirForm(this);
        prescribirController = new PrescribirController(prescribirForm, prescribirService, userId);
    }

    /**
     * Inicializar el sistema de mensajería
     */
    private void initializeMensajesView() {
        try {
            mensajesView = new MensajesView();

            // Obtener el username del usuario actual desde el servicio
            String username = obtenerUsername(userId);

            // Crear el controlador con la configuración del servidor
            mensajeController = new MensajeController(
                    mensajesView,
                    username,
                    "localhost",  // Host del servidor de mensajería
                    7002          // Puerto del servidor de mensajería
            );

            System.out.println("✅ Sistema de mensajería inicializado para: " + username);

        } catch (Exception e) {
            System.err.println(" Error al inicializar mensajería: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtener el username del usuario actual
     */
    private String obtenerUsername(int userId) {
        try {
            // Usar el servicio de usuarios para obtener el nombre
            var usuario = usuarioService.getUsuarioById(userId);
            if (usuario != null) {
                return usuario.getNombre(); // Ajustar según tu modelo
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo username: " + e.getMessage());
        }
        return "Usuario_" + userId; // Fallback
    }

    private void initializeUI() {
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LogoAPP.png")));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.printf("Icon could not be loaded: %s%n", e.getMessage());
        }

        setContentPane(mainPanel);
        setTitle("Sistema de Gestión - " + getUserTypeDisplayName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        setLocationRelativeTo(null);

        contentPanel.setLayout(new BorderLayout());
    }

    private void configureMenu() {
        collapsiblePanel.setPreferredSize(new Dimension(MENU_WIDTH, collapsiblePanel.getHeight()));
        collapsiblePanel.setMinimumSize(new Dimension(MENU_COLLAPSED_WIDTH, 0));
        collapsiblePanel.setMaximumSize(new Dimension(MENU_WIDTH, Integer.MAX_VALUE));

        configureButtonsForUserType();

        collapsiblePanel.revalidate();
        collapsiblePanel.repaint();
    }

    private void configureButtonsForUserType() {
        JButton[] allButtons = {medicosButton, farmaceutasButton, pacientesButton,
                medicamentosButton, dashboardButton, acercadeButton,
                prescribirButton, despachoButton, mensajesButton, historicoRecetasButton};

        switch (userType) {
            case ADMINISTRADOR -> {
                medicosButton.setEnabled(true);
                farmaceutasButton.setEnabled(true);
                pacientesButton.setEnabled(true);
                medicamentosButton.setEnabled(true);
                dashboardButton.setEnabled(true);
                acercadeButton.setEnabled(true);
                historicoRecetasButton.setEnabled(true);
                prescribirButton.setEnabled(false);
                mensajesButton.setEnabled(true);
                despachoButton.setEnabled(false);
            }
            case FARMACEUTA -> {
                medicosButton.setEnabled(false);
                farmaceutasButton.setEnabled(false);
                pacientesButton.setEnabled(false);
                medicamentosButton.setEnabled(false);
                dashboardButton.setEnabled(true);
                acercadeButton.setEnabled(true);
                despachoButton.setEnabled(true);
                historicoRecetasButton.setEnabled(true);
                mensajesButton.setEnabled(true);
                prescribirButton.setEnabled(false);
            }
            case MEDICO -> {
                medicosButton.setEnabled(false);
                farmaceutasButton.setEnabled(false);
                pacientesButton.setEnabled(false);
                medicamentosButton.setEnabled(false);
                prescribirButton.setEnabled(true);
                historicoRecetasButton.setEnabled(true);
                dashboardButton.setEnabled(true);
                acercadeButton.setEnabled(true);
                mensajesButton.setEnabled(true);
                despachoButton.setEnabled(false);
            }
        }

        // Ocultar botones deshabilitados
        for (JButton btn : allButtons) {
            btn.setVisible(btn.isEnabled());
        }
    }

    private void initializeMedicoView() {
        medicoForm = new MedicoForm(this);
        medicoController = new MedicoController(medicoForm, usuarioService);
    }

    private void initializePacienteView() {
        pacienteForm = new PacienteForm(this);
        pacienteController = new PacienteController(pacienteForm, pacienteService);
    }

    private void initializeFarmaceutaView() {
        farmaceutaForm = new FarmaceutaForm(this);
        farmaceutaController = new FarmaceutaController(farmaceutaForm, usuarioService);
    }

    private void initializeHistoricoRecetasView() {
        historicoRecetasView = new HistoricoRecetasView(this);
        historicoRecetasController = new HistoricoRecetasController(
                historicoRecetasView,
                new RecetaService("localhost", 7000),
                new PacienteService("localhost", 7000)
        );
    }

    private void initializeMedicamentoView() {
        medicamentoForm = new MedicamentoForm(this);
        medicamentoController = new MedicamentoController(medicamentoForm, medicamentoService);
    }

    private void initializeDashboardView() {
        dashboardController = new DashboardController(dashboardService);
        dashboardView = new DashboardView(dashboardController, medicamentoService);
    }

    private void showMedicamentoView() {
        switchContent(medicamentoForm, "Medicamentos");
    }

    private void wireEvents() {
        toggleButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                toggleMenu();
            }
        });

        salirButton.addActionListener(e -> handleLogout());
        medicosButton.addActionListener(e -> showMedicoView());
        farmaceutasButton.addActionListener(e -> showFarmaceutaView());
        pacientesButton.addActionListener(e -> showPacienteView());
        medicamentosButton.addActionListener(e -> showMedicamentoView());
        dashboardButton.addActionListener(e -> showDashboardView());
        acercadeButton.addActionListener(e -> showAcercaDeView());
        prescribirButton.addActionListener(e -> showPrescribirView());
        despachoButton.addActionListener(e -> showDespachoView());
        mensajesButton.addActionListener(e -> showMensajesView());  // Cambio aquí
        historicoRecetasButton.addActionListener(e -> showHistoricoRecetasView());
    }

    private void showDespachoView() {
        switchContent(despachoForm.getMainPanel(), "Despacho");
    }

    private void showPrescribirView() {
        switchContent(prescribirForm.getMainPanel(), "Prescribir");
    }

    /**
     * Mostrar la vista de mensajes
     */
    private void showMensajesView() {
        if (mensajesView != null && mensajeController != null) {
            switchContent(mensajesView.getPrincipalPanel(), "Mensajes");
        } else {
            showError("El sistema de mensajería no está disponible");
        }
    }

    private void initializeView() {
        // Mostrar vista inicial según tipo de usuario
        switch (userType) {
            case ADMINISTRADOR, FARMACEUTA, MEDICO -> showWelcomeView();
        }

        setVisible(true);
    }

    private void showWelcomeView() {
        JPanel welcomePanel = createWelcomePanel();
        switchContent(welcomePanel, getUserTypeDisplayName());
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 248));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Bienvenido al Sistema de Gestión");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(0, 102, 204));

        JLabel userTypeLabel = new JLabel("Usuario: " + getUserTypeDisplayName());
        userTypeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userTypeLabel.setForeground(Color.DARK_GRAY);

        panel.add(welcomeLabel, gbc);
        gbc.gridy++;
        panel.add(userTypeLabel, gbc);

        return panel;
    }

    private void showMedicoView() {
        switchContent(medicoForm, "Médicos");
    }

    private void showPacienteView() {
        switchContent(pacienteForm, "Pacientes");
    }

    private void showFarmaceutaView() {
        switchContent(farmaceutaForm, "Farmaceutas");
    }

    private void showHistoricoRecetasView() {
        historicoRecetasController.recargarRecetas();
        switchContent(historicoRecetasView, "Histórico de Recetas");
    }

    private void showDashboardView() {
        switchContent(dashboardView, "Dashboard");
    }

    private void showAcercaDeView() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 248));

        String info = """
                <html><center>
                <h2>Sistema de Gestión Médica</h2>
                <p><b>Versión:</b> 2.0</p>
                <p><b>Universidad Nacional de Costa Rica</b></p>
                <p>EIF206 - Programación 3</p>
                <br>
                <p>Miembros del Grupo:</p>
                <p>- David González Córdoba</p>
                <p>- Dany Montero Romero</p>
                <p>- Emmanuel Núñez Jiménez</p>
                </center></html>
                """;

        JLabel label = new JLabel(info);
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(label);
        switchContent(panel, "Acerca de");
    }

    private void switchContent(JPanel newContent, String title) {
        contentPanel.removeAll();
        contentPanel.add(newContent, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
        setTitle("Sistema de Gestión - " + title);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Desconectar del servidor de mensajería
            if (mensajeController != null) {
                mensajeController.disconnect();
            }

            setVisible(false);
            contentPanel.removeAll();
            loginController.logout();
            dispose();
        }
    }

    private void toggleMenu() {
        menuVisible = !menuVisible;
        int targetWidth = menuVisible ? MENU_WIDTH : MENU_COLLAPSED_WIDTH;

        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            int currentWidth = collapsiblePanel.getWidth();

            if (currentWidth < targetWidth) { // Expand
                MenuLabel.setVisible(true);
                currentWidth += 10;
                if (currentWidth > targetWidth) currentWidth = targetWidth;
            } else if (currentWidth > targetWidth) { // Collapse
                MenuLabel.setVisible(false);
                currentWidth -= 10;
                if (currentWidth < targetWidth) currentWidth = targetWidth;
            }

            collapsiblePanel.setPreferredSize(new Dimension(currentWidth, collapsiblePanel.getHeight()));
            collapsiblePanel.revalidate();
            collapsiblePanel.repaint();

            // Update button visibility
            JButton[] buttons = {medicosButton, farmaceutasButton, pacientesButton,
                    medicamentosButton, dashboardButton, acercadeButton,
                    prescribirButton, despachoButton, mensajesButton, historicoRecetasButton, salirButton};
            for (JButton btn : buttons) {
                if (btn.isEnabled()) {
                    btn.setVisible(menuVisible || currentWidth > MENU_COLLAPSED_WIDTH);
                }
            }

            if (currentWidth == targetWidth) {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    private String getUserTypeDisplayName() {
        return switch (userType) {
            case ADMINISTRADOR -> "Administrador";
            case FARMACEUTA -> "Farmaceuta";
            case MEDICO -> "Médico";
            default -> "Usuario";
        };
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        Function<String, Image> loadIcon = (path) -> {
            try {
                var resource = getClass().getResource(path);
                if (resource != null) {
                    return new ImageIcon(resource).getImage();
                }
            } catch (Exception e) {
                System.err.println("No se pudo cargar icono: " + path);
            }
            return null;
        };

        Image doctorIcon = loadIcon.apply("/Doctor.png");
        Image farmaceuticoIcon = loadIcon.apply("/Farmaceuta.png");
        Image pacienteIcon = loadIcon.apply("/Paciente.png");
        Image medicamentoIcon = loadIcon.apply("/Medicamento.png");
        Image logoutIcon = loadIcon.apply("/Logout.png");
        Image dashboardIcon = loadIcon.apply("/DashBoard.png");
        Image prescribirIcon = loadIcon.apply("/Prescripcion.png");
        Image infoIcon = loadIcon.apply("/Info.png");
        Image mensajesIcon = loadIcon.apply("/Message.png");

        Color buttonColor = new Color(244, 243, 248);
        Color textColor = Color.BLACK;

        salirButton = new CustomButton("Salir", buttonColor, textColor, logoutIcon);
        medicosButton = new CustomButton("Médicos", buttonColor, textColor, doctorIcon);
        farmaceutasButton = new CustomButton("Farmaceutas", buttonColor, textColor, farmaceuticoIcon);
        pacientesButton = new CustomButton("Pacientes", buttonColor, textColor, pacienteIcon);
        medicamentosButton = new CustomButton("Medicamentos", buttonColor, textColor, medicamentoIcon);
        dashboardButton = new CustomButton("Dashboard", buttonColor, textColor, dashboardIcon);
        prescribirButton = new CustomButton("Prescribir", buttonColor, textColor, prescribirIcon);
        despachoButton = new CustomButton("Despacho", buttonColor, textColor, medicamentoIcon);
        mensajesButton = new CustomButton("Mensajes", buttonColor, textColor, mensajesIcon);
        historicoRecetasButton = new CustomButton("Histórico", buttonColor, textColor, infoIcon);
        acercadeButton = new CustomButton("Acerca de", buttonColor, textColor, infoIcon);
    }
}


