package org.example.Presentation.Views;

import org.example.Presentation.Components.CustomButton;
import org.example.Presentation.Controllers.*;
import org.example.Services.MedicamentoService;
import org.example.Services.PacienteService;
import org.example.Services.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

public class MenuPrincipalView extends JFrame {

    private JPanel mainPanel;
    private JPanel collapsiblePanel;
    private JPanel optionsPanel;
    private JPanel contentPanel;

    private JButton salirButton;
    private JButton medicosButton;
    private JButton farmaceutasButton;
    private JButton pacientesButton;
    private JButton medicamentosButton;
    private JLabel toggleButton;
    private JLabel MenuLabel;

    private boolean menuVisible = false;
    private static final int MENU_WIDTH = 170;
    private static final int MENU_COLLAPSED_WIDTH = 30;

    private final UsuarioService usuarioService;
    private final PacienteService pacienteService;
    private final MedicamentoService medicamentoService;
    private final int userId;
    private final LoginController.UserType userType;
    private final LoginController loginController;

    public MenuPrincipalView(LoginController.UserType userType,
                             LoginController loginController,
                             UsuarioService usuarioService,
                             PacienteService pacienteService,
                             MedicamentoService medicamentoService,
                             int userId) {

        this.userType = userType;
        this.loginController = loginController;
        this.usuarioService = usuarioService;
        this.pacienteService = pacienteService;
        this.medicamentoService = medicamentoService;
        this.userId = userId;

        initializeUI();
        configureMenu();
        wireEvents();
        initializeView();
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
        // Reset all buttons
        medicosButton.setEnabled(false);
        farmaceutasButton.setEnabled(false);
        pacientesButton.setEnabled(false);
        medicamentosButton.setEnabled(false);

        switch (userType) {
            case ADMINISTRADOR -> {
                medicosButton.setEnabled(true);
                farmaceutasButton.setEnabled(true);
                pacientesButton.setEnabled(true);
                medicamentosButton.setEnabled(true);
            }
            case FARMACEUTA -> {
                // Farmaceutas pueden ver pacientes y medicamentos
                pacientesButton.setEnabled(true);
                medicamentosButton.setEnabled(true);
            }
            case MEDICO -> {
                // Médicos pueden ver pacientes y medicamentos
                pacientesButton.setEnabled(true);
                medicamentosButton.setEnabled(true);
            }
        }

        // Update visibility
        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        JButton[] buttons = {medicosButton, farmaceutasButton, pacientesButton, medicamentosButton};
        for (JButton btn : buttons) {
            btn.setVisible(btn.isEnabled());
        }
    }

    private void wireEvents() {
        toggleButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                toggleMenu();
            }
        });

        salirButton.addActionListener(e -> handleLogout());
        medicosButton.addActionListener(e -> showMedicosView());
        farmaceutasButton.addActionListener(e -> showFarmaceutasView());
        pacientesButton.addActionListener(e -> showPacientesView());
        medicamentosButton.addActionListener(e -> showMedicamentosView());
    }

    private void initializeView() {
        // Animate menu open
        toggleMenu();
        toggleMenu();

        // Show initial view based on user type
        switch (userType) {
            case ADMINISTRADOR -> showMedicosView();
            case FARMACEUTA -> showMedicamentosView();
            case MEDICO -> showPacientesView();
            default -> showWelcomeView();
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // View switching methods
    private void showMedicosView() {
        MedicoForm medicoForm = new MedicoForm(this);
        new MedicoController(medicoForm, usuarioService);
        switchContent(medicoForm.getMainPanel(), "Gestión de Médicos");
    }

    private void showFarmaceutasView() {
        FarmaceutaForm farmaceutaForm = new FarmaceutaForm(this);
        new FarmaceutaController(farmaceutaForm, usuarioService);
        switchContent(farmaceutaForm.getMainPanel(), "Gestión de Farmaceutas");
    }

    private void showPacientesView() {
        PacienteForm pacienteForm = new PacienteForm(this);
        new PacienteController(pacienteForm, pacienteService);
        switchContent(pacienteForm.getMainPanel(), "Gestión de Pacientes");
    }

    private void showMedicamentosView() {
        MedicamentoForm medicamentoForm = new MedicamentoForm(this);
        new MedicamentoController(medicamentoForm, medicamentoService);
        switchContent(medicamentoForm.getMainPanel(), "Gestión de Medicamentos");
    }

    private void showWelcomeView() {
        JPanel welcomePanel = createWelcomePanel();
        switchContent(welcomePanel, "Bienvenido");
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));

        JLabel welcomeLabel = new JLabel("Bienvenido al Sistema de Gestión");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel userTypeLabel = new JLabel("Tipo de usuario: " + getUserTypeDisplayName());
        userTypeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        panel.add(welcomeLabel, gbc);
        panel.add(userTypeLabel, gbc);

        return panel;
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
            dispose();
            loginController.logout();
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
            JButton[] buttons = {medicosButton, farmaceutasButton, pacientesButton, medicamentosButton, salirButton};
            for (JButton btn : buttons) {
                if (btn.isEnabled()) {
                    btn.setVisible(menuVisible || currentWidth > MENU_COLLAPSED_WIDTH);
                }
            }

            if (currentWidth == targetWidth) {
                timer.stop();
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

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        // Load icons
        Image doctorIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Doctor.png"))).getImage();
        Image farmaceuticoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Farmaceuta.png"))).getImage();
        Image pacienteIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Paciente.png"))).getImage();
        Image medicamentoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Medicamento.png"))).getImage();
        Image logoutIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Logout.png"))).getImage();

        // Create buttons with icons
        Color buttonColor = new Color(244, 243, 248);
        Color textColor = Color.BLACK;

        salirButton = new CustomButton("Salir", buttonColor, textColor, logoutIcon);
        medicosButton = new CustomButton("Médicos", buttonColor, textColor, doctorIcon);
        farmaceutasButton = new CustomButton("Farmaceutas", buttonColor, textColor, farmaceuticoIcon);
        pacientesButton = new CustomButton("Pacientes", buttonColor, textColor, pacienteIcon);
        medicamentosButton = new CustomButton("Medicamentos", buttonColor, textColor, medicamentoIcon);
    }
}