package org.example.Presentation.Views;

import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Controllers.DashboardController;
import org.example.Domain.Dtos.Medicamento.MedicamentoResponseDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Services.MedicamentoService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.MessageFormat;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardView extends JPanel {
    private JPanel mainPanel;
    @SuppressWarnings( "unused" )
    private JPanel datosPanel;
    private JPanel medicamentosGraphPanel;
    private JComboBox<String> selectMedicamentoComboBox;
    private JButton addButton;
    private JTable medicamentosTable;
    private JPanel startYearPicker;
    private JPanel endYearPicker;
    private JPanel startMonthPicker;
    private JPanel endMonthPicker;
    private JComboBox<String> selectGraphicTypeComboBox;
    private JButton deleteSelectedButton;

    private DefaultTableModel medicamentosTableModel;

    private final DashboardController dashboardController;
    private final MedicamentoService medicamentoService;

    private JMonthChooser startMonthChooser;
    private JYearChooser startYearChooser;
    private JMonthChooser endMonthChooser;
    private JYearChooser endYearChooser;

    private static final String COL_ID = "ID";
    private static final String COL_NOMBRE = "Medicamento";

    public DashboardView(DashboardController dashboardController, MedicamentoService medicamentoService) {
        this.dashboardController = dashboardController;
        this.medicamentoService = medicamentoService;
        initComboBoxes();
        initDatePickers();
        initTableModel();
        initListeners();
        initChartsContainer();
        refreshCharts();
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    private void initTableModel() {
        medicamentosTableModel = new DefaultTableModel(new Object[]{COL_ID, COL_NOMBRE}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        medicamentosTable.setModel(medicamentosTableModel);
    }

    private void initListeners() {
        addButton.addActionListener(e -> {
            String nombreSel = (String) selectMedicamentoComboBox.getSelectedItem();
            if (nombreSel == null) return;
            List<MedicamentoResponseDto> meds;
            try {
                meds = medicamentoService.listMedicamentosAsync().get();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading medicamentos: " + ex.getMessage());
                return;
            }
            MedicamentoResponseDto med = meds.stream()
                    .filter(m -> nombreSel.equals(m.getNombre()))
                    .findFirst()
                    .orElse(null);
            if (med == null) return;
            if (!containsMedicamento(med.getId())) {
                medicamentosTableModel.addRow(new Object[]{med.getId(), med.getNombre()});
                refreshCharts();
            } else {
                JOptionPane.showMessageDialog(this, "Ya agregado.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        deleteSelectedButton.addActionListener(e -> {
            int row = medicamentosTable.getSelectedRow();
            if (row >= 0) {
                medicamentosTableModel.removeRow(row);
                refreshCharts();
            }
        });

        addMonthYearSync(startMonthChooser);
        addMonthYearSync(endMonthChooser);

        selectGraphicTypeComboBox.addActionListener(e -> refreshCharts());
    }

    private void addMonthYearSync(JMonthChooser chooser) {
        if (chooser == null) return;
        chooser.addPropertyChangeListener(evt -> {
            if ("month".equals(evt.getPropertyName())) {
                refreshCharts();
            }
        });
    }

    private boolean containsMedicamento(int id) {
        for (int i = 0; i < medicamentosTableModel.getRowCount(); i++) {
            if ((int) medicamentosTableModel.getValueAt(i, 0) == id) return true;
        }
        return false;
    }

    private void initChartsContainer() {
        medicamentosGraphPanel.setLayout(new GridLayout(1, 2));
    }

    private String getSelectedChartType() {
        Object v = selectGraphicTypeComboBox.getSelectedItem();
        return v == null ? "" : v.toString();
    }

    private void initDatePickers() {
        startYearPicker.setLayout(new BorderLayout());
        startMonthPicker.setLayout(new BorderLayout());
        endYearPicker.setLayout(new BorderLayout());
        endMonthPicker.setLayout(new BorderLayout());

        JMonthChooser startMonth = new JMonthChooser();
        JYearChooser  startYear = new JYearChooser();
        JMonthChooser endMonth = new JMonthChooser();
        JYearChooser  endYear = new JYearChooser();

        this.startMonthChooser = startMonth;
        this.startYearChooser = startYear;
        this.endMonthChooser = endMonth;
        this.endYearChooser = endYear;

        startMonthPicker.add(startMonth, BorderLayout.CENTER);
        startYearPicker.add(startYear, BorderLayout.CENTER);
        endMonthPicker.add(endMonth, BorderLayout.CENTER);
        endYearPicker.add(endYear, BorderLayout.CENTER);

        startMonthPicker.revalidate();
        startMonthPicker.repaint();
        startYearPicker.revalidate();
        startYearPicker.repaint();
        endMonthPicker.revalidate();
        endMonthPicker.repaint();
        endYearPicker.revalidate();
        endYearPicker.repaint();
    }

    private void refreshCharts() {
        if (startYearChooser == null || endYearChooser == null) return;

        YearMonth inicio = YearMonth.of(startYearChooser.getYear(), startMonthChooser.getMonth() + 1);
        YearMonth fin = YearMonth.of(endYearChooser.getYear(), endMonthChooser.getMonth() + 1);

        if (fin.isBefore(inicio)) {
            YearMonth tmp = inicio;
            inicio = fin;
            fin = tmp;
        }

        List<RecetaResponseDto> recetasEnRango = dashboardController.getRecetasWithinRange(inicio, fin);

        String tipo = getSelectedChartType();
        medicamentosGraphPanel.removeAll();

        if ("Medicamentos".equalsIgnoreCase(tipo)) {
            Map<Integer, String> medsSeleccionados = getSelectedMedicamentos();
            DefaultCategoryDataset lineDataset = dashboardController.buildLineDataset(recetasEnRango, inicio, fin, medsSeleccionados);
            JFreeChart lineChart = ChartFactory.createLineChart(
                    MessageFormat.format("Unidades recetadas por medicamento ({0} a {1})", formatoYM(inicio), formatoYM(fin)),
                    "Mes",
                    "Unidades",
                    lineDataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );
            medicamentosGraphPanel.add(new ChartPanel(lineChart), BorderLayout.CENTER);
        } else if ("Recetas".equalsIgnoreCase(tipo)) {
            DefaultPieDataset<String> pieDataset = dashboardController.buildPieDataset(recetasEnRango);
            JFreeChart pieChart = ChartFactory.createPieChart(
                    MessageFormat.format("Recetas por Estado ({0} a {1})", formatoYM(inicio), formatoYM(fin)),
                    pieDataset,
                    true,
                    true,
                    false
            );
            medicamentosGraphPanel.add(new ChartPanel(pieChart), BorderLayout.CENTER);
        } else {
            JLabel vacio = new JLabel("Seleccione un tipo de gráfica", SwingConstants.CENTER);
            medicamentosGraphPanel.add(vacio, BorderLayout.CENTER);
        }

        medicamentosGraphPanel.revalidate();
        medicamentosGraphPanel.repaint();
    }

    private Map<Integer, String> getSelectedMedicamentos() {
        Map<Integer, String> medsSeleccionados = new LinkedHashMap<>();
        for (int i = 0; i < medicamentosTableModel.getRowCount(); i++) {
            int id = (int) medicamentosTableModel.getValueAt(i, 0);
            String nombre = (String) medicamentosTableModel.getValueAt(i, 1);
            medsSeleccionados.put(id, nombre);
        }
        return medsSeleccionados;
    }

    private String formatoYM(YearMonth ym) {
        return String.format("%02d-%d", ym.getMonthValue(), ym.getYear());
    }

    private void initComboBoxes() {
        List<MedicamentoResponseDto> meds;
        try {
            meds = medicamentoService.listMedicamentosAsync().get();
        } catch (Exception ex) {
            meds = List.of();
        }
        selectMedicamentoComboBox.setModel(new DefaultComboBoxModel<>(meds.stream()
                .map(MedicamentoResponseDto::getNombre)
                .toArray(String[]::new)));
        selectGraphicTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Medicamentos", "Recetas"}));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        addButton = new BlueRoundedButton("Agregar");
        deleteSelectedButton = new BlueRoundedButton("Eliminar Seleccionado");
    }
}