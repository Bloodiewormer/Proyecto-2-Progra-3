package org.example.Presentation.Views;

import org.example.Presentation.Components.CustomButton;
import org.example.Presentation.Controllers.DashboardController;
import org.example.Presentation.Controllers.HistoricoRecetasController;
import org.example.Presentation.Controllers.LoginController;
import org.example.Presentation.Models.UserType;
import org.example.Services.MedicamentoService;
import org.example.Services.PacienteService;
import org.example.Services.RecetaService;
import org.example.Services.UsuarioService;
import org.example.Presentation.Controllers.PrescribirController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

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

    private boolean menuVisible = false;
    private static final int MENU_WIDTH = 170;
    private static final int MENU_COLLAPSED_WIDTH = 30;




    private final UsuarioService usuarioService;
    private final PacienteService pacienteService;
    private final MedicamentoService medicamentoService;
    private final RecetaService recetaService;
    private final int userid;

    public MenuPrincipalView(UserType userType,
                             LoginController controller,
                             UsuarioService usuarioService,
                             PacienteService pacienteService,
                             MedicamentoService medicamentoService,
                             RecetaService recetaService, int userId)  {

        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LogoAPP.png")));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.printf("Icon could not be loaded: %s%n", e.getMessage());
        }
        this.usuarioService = usuarioService;
        this.pacienteService = pacienteService;
        this.medicamentoService = medicamentoService;
        this.recetaService = recetaService;
        this.userid = userId;
        setContentPane(mainPanel);
        setTitle("Login");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        setLocationRelativeTo(null);

        ButtonEnable(userType,true);
        contentPanel.setLayout(new BorderLayout());


        contentPanel.setLayout(new BorderLayout());
        collapsiblePanel.setPreferredSize(new Dimension(MENU_WIDTH, collapsiblePanel.getHeight()));
        collapsiblePanel.setMinimumSize(new Dimension(MENU_COLLAPSED_WIDTH, 0));
        collapsiblePanel.setMaximumSize(new Dimension(MENU_WIDTH, Integer.MAX_VALUE));
        collapsiblePanel.revalidate();
        collapsiblePanel.repaint();



        toggleButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                toggleMenu();
            }
        });

        // Ensure everything is validated and visible before showing the frame
        mainPanel.revalidate();
        mainPanel.repaint();
        setVisible(true);


        salirButton.addActionListener(_ -> {
            dispose();
            @SuppressWarnings("unused")
            LoginView loginView = new LoginView(controller); // Show LoginView again
        });

        medicosButton.addActionListener(_ -> showMedicosView() );
        farmaceutasButton.addActionListener(_ -> showFarmaceutasView() );
        pacientesButton.addActionListener(_ -> showPacientesView() );
        medicamentosButton.addActionListener(_ -> showMedicamentosView() );
        dashboardButton.addActionListener(_ -> showDashboardView() );
        acercadeButton.addActionListener(_ -> showAcercaDeView() );
        prescribirButton.addActionListener(_ -> showPrescribirView() );
        despachoButton.addActionListener(_ -> showDespachoview() );
        historicoRecetasButton.addActionListener(_ ->showHistoricoRecetasView() );


        init(userType);
    }

    private void showHistoricoRecetasView() {

        HistoricoRecetasController historicoRecetasController = new HistoricoRecetasController(pacienteService, recetaService);
        HistoricoRecetasView historicoRecetasView = new HistoricoRecetasView(historicoRecetasController, pacienteService, recetaService, medicamentoService, usuarioService);
        contentPanel.removeAll();
        contentPanel.add(historicoRecetasView.getMainPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        //icons
        Image DoctorIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Doctor.png"))).getImage();
        Image FarmaceuticoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Farmaceuta.png"))).getImage();
        Image PacienteIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Paciente.png"))).getImage();
        Image MedicamentoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Medicamento.png"))).getImage();
        Image DashboardIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/DashBoard.png"))).getImage();
        Image LogoutIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Logout.png"))).getImage();
        //Image AdminIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Admin.png"))).getImage();
        Image PrescribirIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Prescripcion.png"))).getImage();
        Image InfoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Info.png"))).getImage();



        salirButton = new CustomButton("Salir", new Color(244, 243, 248), Color.BLACK, LogoutIcon);
        medicosButton = new CustomButton("Medicos", new Color(244, 243, 248), Color.BLACK, DoctorIcon);
        farmaceutasButton = new CustomButton("Farmaceutas", new Color(244, 243, 248), Color.BLACK, FarmaceuticoIcon);
        pacientesButton = new CustomButton("Pacientes", new Color(244, 243, 248), Color.BLACK, PacienteIcon);
        medicamentosButton = new CustomButton("Medicamentos", new Color(244, 243, 248), Color.BLACK, MedicamentoIcon);
        dashboardButton = new CustomButton("Dashboard", new Color(244, 243, 248), Color.BLACK, DashboardIcon);
        acercadeButton = new CustomButton("Acerca de", new Color(244, 243, 248), Color.BLACK,InfoIcon);
        prescribirButton = new CustomButton("Prescribir", new Color(244, 243, 248), Color.BLACK, PrescribirIcon);
        despachoButton = new CustomButton("Despacho", new Color(244, 243, 248), Color.BLACK, MedicamentoIcon);
        historicoRecetasButton = new CustomButton("Histórico Recetas", new Color(244, 243, 248), Color.BLACK, InfoIcon);

    }

    public void ButtonEnable(UserType userType, boolean enable) {
        JButton[] buttons = {medicosButton, farmaceutasButton, pacientesButton,historicoRecetasButton ,medicamentosButton, dashboardButton, acercadeButton, prescribirButton, salirButton, despachoButton};
        switch (userType) {
            case ADMINISTRADOR:
                medicosButton.setEnabled(enable);
                farmaceutasButton.setEnabled(enable);
                pacientesButton.setEnabled(enable);
                medicamentosButton.setEnabled(enable);
                dashboardButton.setEnabled(enable);
                acercadeButton.setEnabled(enable);
                prescribirButton.setEnabled(false);
                despachoButton.setEnabled(false);
                historicoRecetasButton.setEnabled(enable);
                break;
            case FARMACEUTA:
                medicosButton.setEnabled(false);
                farmaceutasButton.setEnabled(false);
                pacientesButton.setEnabled(false);
                medicamentosButton.setEnabled(false);
                dashboardButton.setEnabled(enable);
                acercadeButton.setEnabled(enable);
                despachoButton.setEnabled(enable);
                historicoRecetasButton.setEnabled(enable);
                prescribirButton.setEnabled(false);
                break;
            case MEDICO:
                medicosButton.setEnabled(false);
                farmaceutasButton.setEnabled(false);
                pacientesButton.setEnabled(false);
                medicamentosButton.setEnabled(false);
                prescribirButton.setEnabled(enable);
                historicoRecetasButton.setEnabled(enable);
                dashboardButton.setEnabled(enable);
                acercadeButton.setEnabled(enable);
                despachoButton.setEnabled(false);

                break;
            default:
                throw new IllegalArgumentException("Tipo de usuario no soportado: " + userType);
        }
        for (JButton btn : buttons) {
            btn.setVisible(btn.isEnabled());
        }
    }

    void toggleMenu() {
        menuVisible = !menuVisible;
        int targetWidth = menuVisible ? MENU_WIDTH : 30;
        Timer timer = new Timer(10, null);
        timer.addActionListener(_ -> {
            int currentWidth = collapsiblePanel.getWidth();
            if (currentWidth < targetWidth) { // Expand
                MenuLabel.setVisible(true);
                currentWidth += 10;
                if (currentWidth > targetWidth) currentWidth = targetWidth;
            } else if (currentWidth > targetWidth) {// Collapse
                MenuLabel.setVisible(false);
                currentWidth -= 10;
                if (currentWidth < targetWidth) currentWidth = targetWidth;
            }
            collapsiblePanel.setPreferredSize(new Dimension(currentWidth, collapsiblePanel.getHeight()));
            collapsiblePanel.revalidate();
            collapsiblePanel.repaint();


            JButton[] buttons = {medicosButton, farmaceutasButton, pacientesButton,historicoRecetasButton ,medicamentosButton, dashboardButton, despachoButton  ,acercadeButton, prescribirButton, salirButton};
            for (JButton btn : buttons) {
                if (btn.isEnabled()) {
                    btn.setVisible(menuVisible || currentWidth > 30);
                }
            }

            if (currentWidth == targetWidth) {
                timer.stop();
            }
        });
        timer.start();
    }

    public void showMedicosView() {
        MedicoForm medicoForm = new MedicoForm(usuarioService);
        contentPanel.removeAll();
        contentPanel.add(medicoForm.getMainPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showFarmaceutasView() {
        FarmaceutaForm farmaceutaForm = new FarmaceutaForm(usuarioService);
        contentPanel.removeAll();
        contentPanel.add(farmaceutaForm.getMainPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showPacientesView() {
        PacienteForm pacienteForm = new PacienteForm(pacienteService);
        contentPanel.removeAll();
        contentPanel.add(pacienteForm.getMainPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showMedicamentosView() {
        MedicamentoForm medicamentoForm = new MedicamentoForm(medicamentoService);
        contentPanel.removeAll();
        contentPanel.add(medicamentoForm.getMainPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showDashboardView() {
        DashboardController dashboardController = new DashboardController(recetaService);
        DashboardView dashboardView = new DashboardView(dashboardController, medicamentoService);
        contentPanel.removeAll();
        contentPanel.add(dashboardView.getMainPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    public void showAcercaDeView() {
        //AcercaDeView acercaDeView = new AcercaDeView();
        contentPanel.removeAll();
        //ContentPanel.add(acercaDeView.getMainPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showDespachoview() {
        DespachoForm despachoForm = new DespachoForm(pacienteService , recetaService, medicamentoService);
        contentPanel.removeAll();
        contentPanel.add(despachoForm.getMainPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showPrescribirView( ) {
        PrescribirForm prescribirForm = new PrescribirForm(usuarioService, pacienteService, medicamentoService, recetaService, userid);
        contentPanel.removeAll();
        contentPanel.add(prescribirForm.getMainPanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    public void init ( UserType userType) {
        toggleMenu();
        toggleMenu();
        switch (userType){
            case ADMINISTRADOR -> showMedicosView();
            case FARMACEUTA -> showDespachoview();
            case MEDICO -> showPrescribirView();
            default -> throw new IllegalArgumentException("Tipo de usuario no soportado: " + userType);
        }
    }





}
