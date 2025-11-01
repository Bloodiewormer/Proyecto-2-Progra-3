package org.example.API;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;
import org.example.Domain.dtos.Receta.RecetaResponseDto;
import org.example.Domain.dtos.DetalleReceta.DetalleRecetaResponseDto;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController {

    private final List<RecetaResponseDto> recetas;
    private final Gson gson = new Gson();

    public DashboardController(List<RecetaResponseDto> recetas) {
        this.recetas = recetas != null ? recetas : new ArrayList<>();
    }

    public ResponseDto route(RequestDto request) {
        try {
            switch (request.getRequest()) {
                case "recetas":
                    return handleRecetas(request);
                case "line":
                    return handleLine(request);
                case "pie":
                    return handlePie(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

    // Handler para obtener recetas en rango
    private ResponseDto handleRecetas(RequestDto request) {
        Map<String, String> params = gson.fromJson(request.getData(), Map.class);
        String inicio = params.get("inicio");
        String fin = params.get("fin");

        List<RecetaResponseDto> result = getRecetasWithinRange(inicio, fin);
        return new ResponseDto(true, "Recetas obtenidas", gson.toJson(result));
    }

    // Handler para datos de línea
    private ResponseDto handleLine(RequestDto request) {
        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> params = gson.fromJson(request.getData(), mapType);
        String inicio = (String) params.get("inicio");
        String fin = (String) params.get("fin");
        Map<String, String> medsSeleccionados = (Map<String, String>) params.get("medsSeleccionados");

        Map<Integer, String> meds = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : medsSeleccionados.entrySet()) {
            meds.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }

        Map<String, Map<String, Integer>> result = getLineData(inicio, fin, meds);
        return new ResponseDto(true, "Datos de línea", gson.toJson(result));
    }

    // Handler para datos de pastel
    private ResponseDto handlePie(RequestDto request) {
        Map<String, String> params = gson.fromJson(request.getData(), Map.class);
        String inicio = params.get("inicio");
        String fin = params.get("fin");

        Map<String, Long> result = getPieData(inicio, fin);
        return new ResponseDto(true, "Datos de pastel", gson.toJson(result));
    }

    // --- Métodos auxiliares ---

    private List<RecetaResponseDto> getRecetasWithinRange(String inicio, String fin) {
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

    private Map<String, Map<String, Integer>> getLineData(String inicio, String fin, Map<Integer, String> medsSeleccionados) {
        YearMonth ymInicio = YearMonth.parse(inicio);
        YearMonth ymFin = YearMonth.parse(fin);

        List<RecetaResponseDto> recetasEnRango = getRecetasWithinRange(inicio, fin);

        Map<String, Map<String, Integer>> resultado = new LinkedHashMap<>();

        for (Map.Entry<Integer, String> entry : medsSeleccionados.entrySet()) {
            int medId = entry.getKey();
            String etiqueta = entry.getValue();

            Map<YearMonth, Integer> cantidades = sumarUnidadesPorMesYMedicamento(recetasEnRango, medId);

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

    private Map<String, Long> getPieData(String inicio, String fin) {
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
                // Ignorar fechas inválidas
            }
        }

        return mapa;
    }

    private String formatYearMonth(YearMonth ym) {
        return String.format("%02d-%d", ym.getMonthValue(), ym.getYear());
    }
}