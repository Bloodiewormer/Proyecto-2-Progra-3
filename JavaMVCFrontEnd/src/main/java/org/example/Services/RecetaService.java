package org.example.Services;

/*import org.example.Domain.Dtos.Receta.AddRecetaRequestDto;
import org.example.Domain.Dtos.Receta.DeleteRecetaRequestDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Domain.Dtos.Receta.ListRecetaResponseDto;
import org.example.Domain.Dtos.DetalleReceta.DetalleRecetaResponseDto;
import org.example.domain_layer.Receta;
import org.example.domain_layer.DetalleReceta;
import org.example.domain_layer.Medicamento;
import org.example.utilities.ChangeType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 Requisitos de BD (ejemplo):
 CREATE TABLE recetas (
   id INT PRIMARY KEY,
   id_paciente INT,
   id_medico INT,
   fecha_confeccion DATETIME,
   fecha_retiro DATETIME,
   estado VARCHAR(50)
 );
 CREATE TABLE detalle_receta (
   id INT AUTO_INCREMENT PRIMARY KEY,
   receta_id INT,
   medicamento_codigo INT,
   cantidad INT,
   indicaciones TEXT,
   dias INT,
   FOREIGN KEY (receta_id) REFERENCES recetas(id)
 );
*/
public class RecetaService implements IService<Receta> {

    private final DataSource dataSource;
    private final MedicamentoService medicamentoService;
    private final List<IServiceObserver<Receta>> observers = new ArrayList<>();

    public RecetaService(DataSource dataSource, MedicamentoService medicamentoService) {
        this.dataSource = dataSource;
        this.medicamentoService = medicamentoService;
    }

    // --- CRUD estándar (implementa IService<Receta>) ---

    @Override
    public void agregar(Receta entity) {
        String sqlReceta = "INSERT INTO recetas (id, id_paciente, id_medico, fecha_confeccion, fecha_retiro, estado) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_receta (receta_id, medicamento_codigo, cantidad, indicaciones, dias) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(sqlReceta)) {
                ps.setInt(1, entity.getId());
                ps.setInt(2, entity.getIdPaciente());
                ps.setInt(3, entity.getIdMedico());
                ps.setTimestamp(4, new Timestamp(entity.getFechaConfeccion().getTime()));
                ps.setTimestamp(5, entity.getFechaRetiro() != null ? new Timestamp(entity.getFechaRetiro().getTime()) : null);
                ps.setString(6, entity.getEstado());
                ps.executeUpdate();
            }
            // detalles
            if (entity.getDetalles() != null && !entity.getDetalles().isEmpty()) {
                try (PreparedStatement psd = c.prepareStatement(sqlDetalle)) {
                    for (DetalleReceta d : entity.getDetalles()) {
                        psd.setInt(1, entity.getId());
                        psd.setInt(2, d.getMedicamento().getCodigo());
                        psd.setInt(3, d.getCantidad());
                        psd.setString(4, d.getIndicaciones());
                        psd.setInt(5, d.getDias());
                        psd.addBatch();
                    }
                    psd.executeBatch();
                }
            }
            c.commit();
            notifyObservers(ChangeType.CREATED, entity);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al agregar receta", ex);
        }
    }

    @Override
    public void borrar(int id) {
        String sqlDelDetalle = "DELETE FROM detalle_receta WHERE receta_id = ?";
        String sqlDelReceta = "DELETE FROM recetas WHERE id = ?";
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement psd = c.prepareStatement(sqlDelDetalle)) {
                psd.setInt(1, id);
                psd.executeUpdate();
            }
            try (PreparedStatement psr = c.prepareStatement(sqlDelReceta)) {
                psr.setInt(1, id);
                psr.executeUpdate();
            }
            c.commit();
            notifyObservers(ChangeType.DELETED, null);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al borrar receta", ex);
        }
    }

    @Override
    public void actualizar(Receta entity) {
        String sqlReceta = "UPDATE recetas SET id_paciente = ?, id_medico = ?, fecha_confeccion = ?, fecha_retiro = ?, estado = ? WHERE id = ?";
        String sqlDelDetalle = "DELETE FROM detalle_receta WHERE receta_id = ?";
        String sqlInsDetalle = "INSERT INTO detalle_receta (receta_id, medicamento_codigo, cantidad, indicaciones, dias) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(sqlReceta)) {
                ps.setInt(1, entity.getIdPaciente());
                ps.setInt(2, entity.getIdMedico());
                ps.setTimestamp(3, new Timestamp(entity.getFechaConfeccion().getTime()));
                ps.setTimestamp(4, entity.getFechaRetiro() != null ? new Timestamp(entity.getFechaRetiro().getTime()) : null);
                ps.setString(5, entity.getEstado());
                ps.setInt(6, entity.getId());
                ps.executeUpdate();
            }
            try (PreparedStatement psd = c.prepareStatement(sqlDelDetalle)) {
                psd.setInt(1, entity.getId());
                psd.executeUpdate();
            }
            if (entity.getDetalles() != null && !entity.getDetalles().isEmpty()) {
                try (PreparedStatement psi = c.prepareStatement(sqlInsDetalle)) {
                    for (DetalleReceta d : entity.getDetalles()) {
                        psi.setInt(1, entity.getId());
                        psi.setInt(2, d.getMedicamento().getCodigo());
                        psi.setInt(3, d.getCantidad());
                        psi.setString(4, d.getIndicaciones());
                        psi.setInt(5, d.getDias());
                        psi.addBatch();
                    }
                    psi.executeBatch();
                }
            }
            c.commit();
            notifyObservers(ChangeType.UPDATED, entity);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar receta", ex);
        }
    }

    @Override
    public List<Receta> leerTodos() {
        String sql = "SELECT id, id_paciente, id_medico, fecha_confeccion, fecha_retiro, estado FROM recetas";
        List<Receta> result = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Receta r = mapRecetaFromResultSet(rs);
                r.setDetalles(loadDetallesForReceta(r.getId()));
                result.add(r);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al leer recetas", ex);
        }
        return result;
    }

    @Override
    public Receta leerPorId(int id) {
        String sql = "SELECT id, id_paciente, id_medico, fecha_confeccion, fecha_retiro, estado FROM recetas WHERE id = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Receta r = mapRecetaFromResultSet(rs);
                    r.setDetalles(loadDetallesForReceta(r.getId()));
                    return r;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al leer receta por id", ex);
        }
        return null;
    }

    @Override
    public void addObserver(IServiceObserver<Receta> listener) {
        if (listener != null && !observers.contains(listener)) observers.add(listener);
    }

    private void notifyObservers(ChangeType type, Receta entity) {
        for (IServiceObserver<Receta> obs : observers) {
            obs.onDataChanged(type, entity);
        }
    }

    // --- Métodos adicionales útiles para la UI / DTOs ---

    public void agregarDesdeDto(AddRecetaRequestDto dto) {
        // convertir DTO a entidad mínima
        Receta r = new Receta(getNextId(), dto.getIdPaciente(), dto.getIdMedico(), new Date(), null, dto.getDetalles() == null ? null : dto.getDetalles().toString());
        // Rellenado de detalles desde DTO no implementado completo aquí; se recomienda construir Receta plenamente antes de llamar agregar(Receta)
        agregar(r);
    }

    public void borrarDesdeDto(DeleteRecetaRequestDto dto) {
        borrar(dto.getId());
    }

    public ListRecetaResponseDto listarRecetasDto() {
        List<Receta> recs = leerTodos();
        List<RecetaResponseDto> list = new ArrayList<>();
        for (Receta r : recs) {
            RecetaResponseDto rr = mapRecetaToDto(r);
            list.add(rr);
        }
        return new ListRecetaResponseDto(list);
    }

    public RecetaResponseDto obtenerRecetaDto(int id) {
        Receta r = leerPorId(id);
        return r == null ? null : mapRecetaToDto(r);
    }

    public List<Receta> buscarPorPacienteId(int idPaciente) {
        String sql = "SELECT id, id_paciente, id_medico, fecha_confeccion, fecha_retiro, estado FROM recetas WHERE id_paciente = ?";
        List<Receta> result = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Receta r = mapRecetaFromResultSet(rs);
                    r.setDetalles(loadDetallesForReceta(r.getId()));
                    result.add(r);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar recetas por paciente", ex);
        }
        return result;
    }

    public int getNextId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS nextId FROM recetas";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("nextId");
        } catch (SQLException ex) {
            throw new RuntimeException("Error al obtener next id", ex);
        }
        return 1;
    }

    // --- Utilitarios privados ---

    private Receta mapRecetaFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int idPaciente = rs.getInt("id_paciente");
        int idMedico = rs.getInt("id_medico");
        Timestamp tConfe = rs.getTimestamp("fecha_confeccion");
        Timestamp tRetiro = rs.getTimestamp("fecha_retiro");
        String estado = rs.getString("estado");
        Date fechaConfe = tConfe != null ? new Date(tConfe.getTime()) : null;
        Date fechaRetiro = tRetiro != null ? new Date(tRetiro.getTime()) : null;
        // Se asume que la clase Receta tiene constructor: Receta(id, idPaciente, idMedico, fechaConfeccion, fechaRetiro, estado)
        return new Receta(id, idPaciente, idMedico, fechaConfe, fechaRetiro, estado);
    }

    private List<DetalleReceta> loadDetallesForReceta(int recetaId) {
        String sql = "SELECT medicamento_codigo, cantidad, indicaciones, dias FROM detalle_receta WHERE receta_id = ?";
        List<DetalleReceta> detalles = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, recetaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int codigo = rs.getInt("medicamento_codigo");
                    int cantidad = rs.getInt("cantidad");
                    String indicaciones = rs.getString("indicaciones");
                    int dias = rs.getInt("dias");
                    Medicamento m = medicamentoService != null ? medicamentoService.leerPorId(codigo) : null;
                    // Se asume constructor DetalleReceta(Medicamento, int cantidad, String indicaciones, int dias)
                    detalles.add(new DetalleReceta(m, cantidad, indicaciones, dias));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al cargar detalles de receta", ex);
        }
        return detalles;
    }

    private RecetaResponseDto mapRecetaToDto(Receta r) {
        List<DetalleRecetaResponseDto> detallesDto = new ArrayList<>();
        if (r.getDetalles() != null) {
            for (DetalleReceta d : r.getDetalles()) {
                DetalleRecetaResponseDto dd = new DetalleRecetaResponseDto();
                dd.setCodigoMedicamento(d.getMedicamento() != null ? d.getMedicamento().getCodigo() : 0);
                dd.setCantidad(d.getCantidad());
                dd.setIndicaciones(d.getIndicaciones());
                dd.setDias(d.getDias());
                detallesDto.add(dd);
            }
        }
        RecetaResponseDto rr = new RecetaResponseDto();
        rr.setId(r.getId());
        rr.setIdPaciente(r.getIdPaciente());
        rr.setIdMedico(r.getIdMedico());
        rr.setFechaConfeccion(r.getFechaConfeccion() != null ? r.getFechaConfeccion().toString() : null);
        rr.setFechaRetiro(r.getFechaRetiro() != null ? r.getFechaRetiro().toString() : null);
        rr.setEstado(r.getEstado());
        rr.setDetalles(detallesDto);
        return rr;
    }
}


