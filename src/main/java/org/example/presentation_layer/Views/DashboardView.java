package org.example.presentation_layer.Views;

import javax.swing.*;


// Librerías para los gráficos JFreeChart
import java.awt.*;
import java.util.Date;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;




public class DashboardView extends JPanel/* implements IServiceObserver<Object>*/ {
    private JPanel MainPanel;
    private JPanel DatosPanel;
    private JPanel MedicamentosGrapPanel;
    private JComboBox comboBox1;
    private JButton agregarButton;
    private JTable DashBoardTable;
    private JPanel DesdeYearPicker;
    private JPanel HastaYearPicker;
    private JPanel DesdeMesPicker;
    private JPanel HastaMesPicker;
    private JComboBox comboBox2;
    private JButton eliminarSelecionadoButton;

    public DashboardView() {
        initDatePickers();
    }


    private void initDatePickers() {
        DesdeYearPicker.setLayout(new BorderLayout());
        DesdeMesPicker.setLayout(new BorderLayout());
        HastaYearPicker.setLayout(new BorderLayout());
        HastaMesPicker.setLayout(new BorderLayout());

        JMonthChooser desdeMes = new JMonthChooser();
        JYearChooser desdeYear = new JYearChooser();
        JMonthChooser hastaMes = new JMonthChooser();
        JYearChooser hastaYear = new JYearChooser();

        DesdeMesPicker.add(desdeMes, BorderLayout.CENTER);
        DesdeYearPicker.add(desdeYear, BorderLayout.CENTER);
        HastaMesPicker.add(hastaMes, BorderLayout.CENTER);
        HastaYearPicker.add(hastaYear, BorderLayout.CENTER);

        DesdeMesPicker.revalidate();
        DesdeMesPicker.repaint();
        DesdeYearPicker.revalidate();
        DesdeYearPicker.repaint();
        HastaMesPicker.revalidate();
        HastaMesPicker.repaint();
        HastaYearPicker.revalidate();
        HastaYearPicker.repaint();
    }






//    private final DashboardService dashboardService;
//    private JPanel chartPanelContainer;
//    private JComboBox<String> monthRangeSelector;
//
//    public DashboardView(DashboardService dashboardService) {
//        this.dashboardService = dashboardService;
//        this.dashboardService.addObserver(this);
//
//        setTitle("Dashboard - Estadísticas");
//        setSize(900, 600);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        initUI();
//    }
//
//    private void initUI() {
//        setLayout(new BorderLayout());
//
//        // Panel de filtros
//        JPanel filterPanel = new JPanel();
//        filterPanel.add(new JLabel("Rango de meses:"));
//        monthRangeSelector = new JComboBox<>(new String[]{"Últimos 3 meses", "Últimos 6 meses", "Últimos 12 meses"});
//        filterPanel.add(monthRangeSelector);
//
//        JButton refreshButton = new JButton("Actualizar");
//        refreshButton.addActionListener(e -> updateCharts(Collections.emptyList(), Collections.emptyList()));
//        filterPanel.add(refreshButton);
//
//        add(filterPanel, BorderLayout.NORTH);
//
//        // Panel principal donde van los gráficos
//        chartPanelContainer = new JPanel(new GridLayout(1, 2));
//        add(chartPanelContainer, BorderLayout.CENTER);
//
//        // Carga inicial vacía
//        updateCharts(Collections.emptyList(), Collections.emptyList());
//    }
//
//    /**
//     * Actualiza ambos gráficos (línea + pastel)
//     */
//    private void updateCharts(List<Medicamento> medicamentos, List<Receta> recetas) {
//        chartPanelContainer.removeAll();
//
//        // 1. Gráfico de línea (Medicamentos por mes)
//        JFreeChart lineChart = ChartFactory.createLineChart(
//                "Medicamentos prescritos por mes",
//                "Mes",
//                "Cantidad",
//                buildMedicamentosDataset(medicamentos)
//        );
//        chartPanelContainer.add(new ChartPanel(lineChart));
//
//        // 2. Gráfico de pastel (Recetas por estado)
//        JFreeChart pieChart = ChartFactory.createPieChart(
//                "Recetas por estado",
//                buildRecetasDataset(recetas),
//                true, true, false
//        );
//        chartPanelContainer.add(new ChartPanel(pieChart));
//
//        chartPanelContainer.revalidate();
//        chartPanelContainer.repaint();
//    }
//
//    /**
//     * Construye dataset de medicamentos prescritos agrupados por mes
//     */
//    private DefaultCategoryDataset buildMedicamentosDataset(List<Medicamento> medicamentos) {
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//
//        // Simulación: contar medicamentos por mes según código (ejemplo si no hay fecha real en Medicamento)
//        Map<String, Long> medicamentosPorMes = medicamentos.stream()
//                .collect(Collectors.groupingBy(m -> {
//                    LocalDate fakeDate = LocalDate.now().minusMonths(new Random().nextInt(12));
//                    return fakeDate.getMonth().toString();
//                }, Collectors.counting()));
//
//        medicamentosPorMes.forEach((mes, cantidad) -> dataset.addValue(cantidad, "Medicamentos", mes));
//
//        return dataset;
//    }
//
//    /**
//     * Construye dataset de recetas agrupadas por estado
//     */
//    private DefaultPieDataset buildRecetasDataset(List<Receta> recetas) {
//        DefaultPieDataset dataset = new DefaultPieDataset();
//
//        //Map<EstadoReceta, Long> recetasPorEstado = recetas.stream()
//          //      .collect(Collectors.groupingBy(Receta::getEstado, Collectors.counting()));
//
//       // recetasPorEstado.forEach(dataset::setValue);
//
//        return dataset;
//    }
//
//    @Override
//    public void onDataChanged(ChangeType type, Object entity) {
//        // Aquí se debería poder obtener las listas completas actualizadas de servicios xd
//        // Por ahora lo dejo como "simulación" xd
//        updateCharts(Collections.emptyList(), Collections.emptyList());
//    }
//
//    @Override
//    public void onDataChanged(List<Object> entities) {
//        updateCharts(Collections.emptyList(), Collections.emptyList());
//    }

    public JPanel  getMainPanel() {return MainPanel;}




}
