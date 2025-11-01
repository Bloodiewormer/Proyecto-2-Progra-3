package org.example.DataAcces.services;

import org.example.Domain.models.Paciente;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestionar pacientes
 */
public class PacienteService {
    private final SessionFactory sessionFactory;

    public PacienteService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Crea un nuevo paciente
     */
    public Paciente create(String nombre, String telefono, LocalDate fechaNacimiento) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Paciente paciente = new Paciente(nombre, telefono, fechaNacimiento);
            session.persist(paciente);

            tx.commit();
            System.out.println("[PacienteService] Paciente creado: " + nombre);
            return paciente;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[PacienteService] Error creando paciente: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene un paciente por ID
     */
    public Paciente getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Paciente.class, id);
        } catch (Exception e) {
            System.err.println("[PacienteService] Error obteniendo paciente: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene todos los pacientes
     */
    public List<Paciente> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Paciente", Paciente.class).list();
        } catch (Exception e) {
            System.err.println("[PacienteService] Error obteniendo pacientes: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Busca pacientes por nombre
     */
    public List<Paciente> buscarPorNombre(String nombre) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Paciente WHERE LOWER(nombre) LIKE LOWER(:nombre)",
                            Paciente.class
                    )
                    .setParameter("nombre", "%" + nombre + "%")
                    .list();
        } catch (Exception e) {
            System.err.println("[PacienteService] Error buscando paciente: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Busca pacientes por teléfono
     */
    public List<Paciente> buscarPorTelefono(String telefono) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Paciente WHERE telefono LIKE :telefono",
                            Paciente.class
                    )
                    .setParameter("telefono", "%" + telefono + "%")
                    .list();
        } catch (Exception e) {
            System.err.println("[PacienteService] Error buscando por teléfono: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Actualiza un paciente
     */
    public Paciente update(Long id, String nombre, String telefono, LocalDate fechaNacimiento) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Paciente paciente = session.get(Paciente.class, id);
            if (paciente != null) {
                paciente.setNombre(nombre);
                paciente.setTelefono(telefono);
                paciente.setFechaNacimiento(fechaNacimiento);
                session.merge(paciente);
                System.out.println("[PacienteService] Paciente actualizado: " + nombre);
            }

            tx.commit();
            return paciente;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[PacienteService] Error actualizando paciente: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Elimina un paciente
     */
    public boolean delete(Long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Paciente paciente = session.get(Paciente.class, id);
            if (paciente != null) {
                session.remove(paciente);
                tx.commit();
                System.out.println("[PacienteService] Paciente eliminado: " + id);
                return true;
            }

            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[PacienteService] Error eliminando paciente: " + e.getMessage());
            throw e;
        }
    }
}