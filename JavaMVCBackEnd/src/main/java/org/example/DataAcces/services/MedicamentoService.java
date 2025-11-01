package org.example.DataAcces.services;

import org.example.Domain.models.Medicamento;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Servicio para gestionar medicamentos
 */
public class MedicamentoService {
    private final SessionFactory sessionFactory;

    public MedicamentoService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Crea un nuevo medicamento
     */
    public Medicamento create(String nombre, String presentacion) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Medicamento medicamento = new Medicamento(nombre, presentacion);
            session.persist(medicamento);

            tx.commit();
            System.out.println("[MedicamentoService] Medicamento creado: " + nombre);
            return medicamento;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[MedicamentoService] Error creando medicamento: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene un medicamento por ID
     */
    public Medicamento getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Medicamento.class, id);
        } catch (Exception e) {
            System.err.println("[MedicamentoService] Error obteniendo medicamento: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene todos los medicamentos
     */
    public List<Medicamento> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Medicamento", Medicamento.class).list();
        } catch (Exception e) {
            System.err.println("[MedicamentoService] Error obteniendo medicamentos: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Busca medicamentos por nombre
     */
    public List<Medicamento> buscarPorNombre(String nombre) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Medicamento WHERE LOWER(nombre) LIKE LOWER(:nombre)",
                            Medicamento.class
                    )
                    .setParameter("nombre", "%" + nombre + "%")
                    .list();
        } catch (Exception e) {
            System.err.println("[MedicamentoService] Error buscando medicamento: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Actualiza un medicamento
     */
    public Medicamento update(Long id, String nombre, String presentacion) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Medicamento medicamento = session.find(Medicamento.class, id);
            if (medicamento != null) {
                medicamento.setNombre(nombre);
                medicamento.setPresentacion(presentacion);
                session.merge(medicamento);
                System.out.println("[MedicamentoService] Medicamento actualizado: " + nombre);
            }

            tx.commit();
            return medicamento;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[MedicamentoService] Error actualizando medicamento: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Elimina un medicamento
     */
    public boolean delete(Long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Medicamento medicamento = session.find(Medicamento.class, id);
            if (medicamento != null) {
                session.remove(medicamento);
                tx.commit();
                System.out.println("[MedicamentoService] Medicamento eliminado: " + id);
                return true;
            }

            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[MedicamentoService] Error eliminando medicamento: " + e.getMessage());
            throw e;
        }
    }
}