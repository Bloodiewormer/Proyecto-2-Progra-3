package org.example.API;
// package org.example.API.Controllers;

import org.example.Domain.dtos.DetalleReceta.DetalleRecetaResponseDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador del BACKEND que provee datos procesados
 * al FrontEnd (DashboardController de Presentation).
 *
 * En lugar de devolver datasets (gráficos),
 * devuelve estructuras de datos JSON (Map/List)
 * para que el FrontEnd pueda construir los gráficos.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    // ⚙️ En un entorno real esto vendría de un servicio o base de datos
    private final List<RecetaResponseDto> recetas;

    public DashboardController() {
        this.recetas = new ArrayList<>();
    }

    public DashboardController(List<RecetaResponseDto> recetas) {
        this.recetas = recetas != null ? recetas : new ArrayList<>();
    }

    // ===========================================================
    // 1️⃣ Obtener recetas dentro de un rango de fechas (por mes)
    // ===========================================================
    @GetMapping("/recetas")
    public List<RecetaResponseDto> getRecetasWithinRange(
            @RequestParam String inicio,
            @RequestParam String fin) {

        YearMonth ymInicio = YearMonth.parse(inicio);
        YearMonth ymFin = YearMonth.parse(fin);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return recetas.stream()
                .filter(r -> {
                    try {
                        if (r.getFechaConfeccion() == null) return false;
                        LocalDate fecha = LocalDate.parse(r.getFechaConfeccion(), formatter);
                        YearMonth ym = YearMonth.from(fecha);
                        return !ym.isBefore(ymInicio) && !ym.isAfter(ymFin);
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // ===========================================================
    // 2️⃣ Datos para gráfico de líneas (unidades por mes/medicamento)
    // ===========================================================
    @PostMapping("/line")
    public Map<String, Map<String, Integer>> getLineData(
            @RequestParam String inicio,
            @RequestParam String fin,
            @RequestBody Map<Integer, String> medsSeleccionados) {

        YearMonth ymInicio = YearMonth.parse(inicio);
        YearMonth ymFin = YearMonth.parse(fin);

        List<RecetaResponseDto> recetasEnRango = getRecetasWithinRange(inicio, fin);

        Map<String, Map<String, Integer>> resultado = new LinkedHashMap<>();

        for (Map.Entry<Integer, String> entry : medsSeleccionados.entrySet()) {
            int medId = entry.getKey();
            String etiqueta = entry.getValue();

            Map<YearMonth, Integer> cantidades = sumarUnidadesPorMesYMedicamento(recetasEnRango, medId);

            // Convertimos YearMonth -> String (para serializar bien a JSON)
            Map<String, Integer> dataFormateada = new LinkedHashMap<>();
            YearMonth cursor = ymInicio;
            while (!cursor.isAfter(ymFin)) {
                String mes = formatYearMonth(cursor);
                dataFormateada.put(mes, cantidades.getOrDefault(cursor, 0));
                cursor = cursor.plusMonths(1);
            }

            resultado.put(etiqueta, dataFormateada);
        }

        return resultado;
    }

    // ===========================================================
    // 3️⃣ Datos para gráfico de pastel (estado de recetas)
    // ===========================================================
    @GetMapping("/pie")
    public Map<String, Long> getPieData(
            @RequestParam String inicio,
            @RequestParam String fin) {

        List<RecetaResponseDto> recetasEnRango = getRecetasWithinRange(inicio, fin);

        if (recetasEnRango.isEmpty()) {
            return Map.of("Sin datos", 1L);
        }

        return recetasEnRango.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getEstado() != null ? r.getEstado() : "DESCONOCIDO",
                        Collectors.counting()
                ));
    }

    // ===========================================================
    // Métodos auxiliares (idénticos al FrontEnd original)
    // ===========================================================
    private Map<YearMonth, Integer> sumarUnidadesPorMesYMedicamento(
            List<RecetaResponseDto> recetas, int medicamentoId) {

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
                // Ignorar fechas inválidas
            }
        }

        return mapa;
    }

    private String formatYearMonth(YearMonth ym) {
        return String.format("%02d-%d", ym.getMonthValue(), ym.getYear());
    }
}
