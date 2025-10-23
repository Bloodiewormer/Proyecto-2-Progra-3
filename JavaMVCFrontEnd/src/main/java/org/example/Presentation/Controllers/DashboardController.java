package org.example.presentation_layer.Controllers;


import org.example.domain_layer.DetalleReceta;
import org.example.domain_layer.Receta;
import org.example.service_layer.RecetaService;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {
    private final RecetaService recetaService;

    public DashboardController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    public List<Receta> getRecetasWithinRange(YearMonth inicio, YearMonth fin) {
        return recetaService.leerTodos().stream()
                .filter(r -> {
                    YearMonth ym = YearMonth.from(r.getFecha().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                    return !ym.isBefore(inicio) && !ym.isAfter(fin);
                })
                .collect(Collectors.toList());
    }

    public DefaultCategoryDataset buildLineDataset(List<Receta> recetasEnRango, YearMonth inicio, YearMonth fin, Map<Integer, String> medsSeleccionados) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<YearMonth> meses = new ArrayList<>();
        YearMonth cursor = inicio;
        while (!cursor.isAfter(fin)) {
            meses.add(cursor);
            cursor = cursor.plusMonths(1);
        }
        if (medsSeleccionados.isEmpty()) return dataset;
        for (Map.Entry<Integer, String> entry : medsSeleccionados.entrySet()) {
            int medId = entry.getKey();
            String etiqueta = entry.getValue();
            Map<YearMonth, Integer> cantidades = sumarUnidadesPorMesYMedicamento(recetasEnRango, medId);
            for (YearMonth m : meses) {
                int valor = cantidades.getOrDefault(m, 0);
                dataset.addValue(valor, etiqueta, formatYearMonth(m));
            }
        }
        return dataset;
    }

    private Map<YearMonth, Integer> sumarUnidadesPorMesYMedicamento(List<Receta> recetas, int medicamentoId) {
        Map<YearMonth, Integer> mapa = new HashMap<>();
        for (Receta r : recetas) {
            if (r == null || r.getDetalles() == null) continue;
            YearMonth ym = YearMonth.from(r.getFecha().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
            int acumulado = mapa.getOrDefault(ym, 0);
            int sumaReceta = r.getDetalles().stream()
                    .filter(d -> d != null
                            && d.getMedicamento() != null
                            && d.getMedicamento().getCodigo() == medicamentoId)
                    .mapToInt(DetalleReceta::getCantidad)
                    .sum();
            if (sumaReceta > 0) {
                mapa.put(ym, acumulado + sumaReceta);
            }
        }
        return mapa;
    }

    public DefaultPieDataset<String> buildPieDataset(List<Receta> recetasEnRango) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        Map<String, Long> byState = recetasEnRango.stream()
                .collect(Collectors.groupingBy(Receta::getEstado, Collectors.counting()));
        if (byState.isEmpty()) {
            dataset.setValue("Sin datos", 1);
            return dataset;
        }
        byState.forEach((estado, cantidad) -> dataset.setValue(
                estado != null ? estado : "DESCONOCIDO",
                cantidad
        ));
        return dataset;
    }

    private String formatYearMonth(YearMonth ym) {
        return String.format("%02d-%d", ym.getMonthValue(), ym.getYear());
    }

}

