package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.DetalleReceta.DetalleRecetaResponseDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController {
    private final List<RecetaResponseDto> recetas;

    public DashboardController(List<RecetaResponseDto> recetas) {
        this.recetas = recetas != null ? recetas : new ArrayList<>();
    }

    public List<RecetaResponseDto> getRecetasWithinRange(YearMonth inicio, YearMonth fin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return recetas.stream()
                .filter(r -> {
                    try {
                        if (r.getFechaConfeccion() == null) return false;
                        LocalDate fecha = LocalDate.parse(r.getFechaConfeccion(), formatter);
                        YearMonth ym = YearMonth.from(fecha);
                        return !ym.isBefore(inicio) && !ym.isAfter(fin);
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public DefaultCategoryDataset buildLineDataset(List<RecetaResponseDto> recetasEnRango,
                                                   YearMonth inicio,
                                                   YearMonth fin,
                                                   Map<Integer, String> medsSeleccionados) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (medsSeleccionados.isEmpty()) return dataset;

        List<YearMonth> meses = new ArrayList<>();
        YearMonth cursor = inicio;
        while (!cursor.isAfter(fin)) {
            meses.add(cursor);
            cursor = cursor.plusMonths(1);
        }

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

    private Map<YearMonth, Integer> sumarUnidadesPorMesYMedicamento(List<RecetaResponseDto> recetas, int medicamentoId) {
        Map<YearMonth, Integer> mapa = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (RecetaResponseDto r : recetas) {
            if (r == null || r.getDetalles() == null || r.getFechaConfeccion() == null) continue;

            try {
                LocalDate fecha = LocalDate.parse(r.getFechaConfeccion(), formatter);
                YearMonth ym = YearMonth.from(fecha);

                int acumulado = mapa.getOrDefault(ym, 0);
                int sumaReceta = r.getDetalles().stream()
                        .filter(d -> d != null && d.getIdMedicamento() == medicamentoId)
                        .mapToInt(DetalleRecetaResponseDto::getCantidad)
                        .sum();

                if (sumaReceta > 0) {
                    mapa.put(ym, acumulado + sumaReceta);
                }
            } catch (DateTimeParseException e) {
                // Skip invalid dates
            }
        }

        return mapa;
    }

    public DefaultPieDataset<String> buildPieDataset(List<RecetaResponseDto> recetasEnRango) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        if (recetasEnRango.isEmpty()) {
            dataset.setValue("Sin datos", 1);
            return dataset;
        }

        Map<String, Long> byState = recetasEnRango.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getEstado() != null ? r.getEstado() : "DESCONOCIDO",
                        Collectors.counting()
                ));

        byState.forEach((estado, cantidad) -> dataset.setValue(estado, cantidad));

        return dataset;
    }

    private String formatYearMonth(YearMonth ym) {
        return String.format("%02d-%d", ym.getMonthValue(), ym.getYear());
    }
}