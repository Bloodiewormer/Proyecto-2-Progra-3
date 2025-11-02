package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Services.DashboardService;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Controlador para el Dashboard
 * Maneja la lógica de presentación y construcción de datasets para gráficos
 */
public class DashboardController {
    private final DashboardService dashboardService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Obtiene recetas dentro del rango de fechas especificado
     * (Compatibilidad con DashboardView existente)
     */
    public List<RecetaResponseDto> getRecetasWithinRange(YearMonth inicio, YearMonth fin) {
        try {
            String startDate = inicio.atDay(1).format(DATE_FORMATTER);
            String endDate = fin.atEndOfMonth().format(DATE_FORMATTER);

            Future<List<DashboardService.RecetaDataDto>> future =
                    dashboardService.getRecetasInRangeAsync(startDate, endDate);

            List<DashboardService.RecetaDataDto> recetasData = future.get();

            if (recetasData == null) {
                return new ArrayList<>();
            }

            // Convertir RecetaDataDto a RecetaResponseDto
            List<RecetaResponseDto> recetas = new ArrayList<>();
            for (DashboardService.RecetaDataDto data : recetasData) {
                RecetaResponseDto receta = new RecetaResponseDto();
                receta.setId(data.getId().intValue()); // Long -> int
                receta.setEstado(data.getEstado());
                // Agregar más campos si RecetaResponseDto los tiene
                recetas.add(receta);
            }

            return recetas;

        } catch (Exception e) {
            System.err.println("[DashboardController] Error obteniendo recetas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Construye el dataset para el gráfico de líneas (medicamentos por mes)
     * (Compatibilidad con DashboardView existente - FIRMA CON 4 PARÁMETROS)
     */
    public DefaultCategoryDataset buildLineDataset(
            List<RecetaResponseDto> recetasEnRango,
            YearMonth inicio,
            YearMonth fin,
            Map<Integer, String> medicamentosSeleccionados) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (medicamentosSeleccionados.isEmpty()) {
            return dataset;
        }

        try {
            // Convertir Map<Integer, String> a Map<Long, String>
            Map<Long, String> medicamentosLong = new LinkedHashMap<>();
            for (Map.Entry<Integer, String> entry : medicamentosSeleccionados.entrySet()) {
                medicamentosLong.put(Long.valueOf(entry.getKey()), entry.getValue());
            }

            // Generar lista de meses
            List<YearMonth> meses = new ArrayList<>();
            YearMonth cursor = inicio;
            while (!cursor.isAfter(fin)) {
                meses.add(cursor);
                cursor = cursor.plusMonths(1);
            }

            // Obtener datos del backend
            String startDate = inicio.atDay(1).format(DATE_FORMATTER);
            String endDate = fin.atEndOfMonth().format(DATE_FORMATTER);
            List<Long> medicamentoIds = new ArrayList<>(medicamentosLong.keySet());

            Future<Map<String, Map<String, Integer>>> future =
                    dashboardService.getMedicamentosPorMesAsync(startDate, endDate, medicamentoIds);

            Map<String, Map<String, Integer>> unidadesPorMes = future.get();

            if (unidadesPorMes == null) {
                return dataset;
            }

            // Construir dataset
            for (Map.Entry<Long, String> entry : medicamentosLong.entrySet()) {
                Long medId = entry.getKey();
                String etiqueta = entry.getValue();

                Map<String, Integer> cantidades = unidadesPorMes.get(String.valueOf(medId));
                if (cantidades == null) {
                    cantidades = new HashMap<>();
                }

                for (YearMonth mes : meses) {
                    String mesStr = mes.format(YEAR_MONTH_FORMATTER);
                    int valor = cantidades.getOrDefault(mesStr, 0);
                    dataset.addValue(valor, etiqueta, formatYearMonth(mes));
                }
            }

        } catch (Exception e) {
            System.err.println("[DashboardController] Error construyendo dataset de líneas: " + e.getMessage());
            e.printStackTrace();
        }

        return dataset;
    }

    /**
     * Construye el dataset para el gráfico de pastel (recetas por estado)
     * (Compatibilidad con DashboardView existente - RECIBE LISTA DE RECETAS)
     */
    public DefaultPieDataset<String> buildPieDataset(List<RecetaResponseDto> recetasEnRango) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        if (recetasEnRango == null || recetasEnRango.isEmpty()) {
            dataset.setValue("Sin datos", 1);
            return dataset;
        }

        try {
            // Contar recetas por estado
            Map<String, Integer> distribucion = new HashMap<>();
            for (RecetaResponseDto receta : recetasEnRango) {
                String estado = receta.getEstado();
                if (estado != null && !estado.trim().isEmpty()) {
                    distribucion.put(estado, distribucion.getOrDefault(estado, 0) + 1);
                }
            }

            System.out.println("[DashboardController] Distribución de estados encontrada: " + distribucion);

            // Si no hay distribución, mostrar "Sin datos"
            if (distribucion.isEmpty()) {
                dataset.setValue("Sin datos", 1);
                return dataset;
            }

            // Traducir estados del enum a español
            // enum EstadoReceta { CONFECCIONADA, PROCESO, LISTA, ENTREGADA }
            Map<String, String> traduccionEstados = new HashMap<>();
            traduccionEstados.put("CONFECCIONADA", "Confeccionada");
            traduccionEstados.put("PROCESO", "En Proceso");
            traduccionEstados.put("LISTA", "Lista");
            traduccionEstados.put("ENTREGADA", "Entregada");



            for (Map.Entry<String, Integer> entry : distribucion.entrySet()) {
                String estado = entry.getKey();
                Integer cantidad = entry.getValue();
                String estadoTraducido = traduccionEstados.getOrDefault(estado, estado);
                dataset.setValue(estadoTraducido, cantidad);
            }

        } catch (Exception e) {
            System.err.println("[DashboardController] Error construyendo dataset de pastel: " + e.getMessage());
            e.printStackTrace();
            dataset.setValue("Error", 1);
        }

        return dataset;
    }

    /**
     * Formatea YearMonth a String legible
     */
    private String formatYearMonth(YearMonth ym) {
        String[] meses = {"Ene", "Feb", "Mar", "Abr", "May", "Jun",
                "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
        return meses[ym.getMonthValue() - 1] + " " + ym.getYear();
    }
}