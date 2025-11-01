package org.example.DataAcces.services;

import org.example.Domain.models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UsuarioService {
    private final SessionFactory sessionFactory;

    public UsuarioService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // ========== MÉDICOS ==========
    public Medico createMedico(String nombre, String clave, String especialidad) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Medico medico = new Medico(nombre, clave, especialidad);
            session.persist(medico);
            tx.commit();
            return medico;
        }
    }

    public Medico getMedicoById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Medico.class, id);
        }
    }

    public List<Medico> getAllMedicos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Medico", Medico.class).list();
        }
    }

    public Medico updateMedico(Long id, String nombre, String clave, String especialidad) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Medico medico = session.get(Medico.class, id);
            if (medico != null) {
                medico.setNombre(nombre);
                if (clave != null && !clave.isEmpty()) {
                    medico.setClave(clave);
                }
                medico.setEspecialidad(especialidad);
                session.merge(medico);
            }
            tx.commit();
            return medico;
        }
    }

    public boolean deleteMedico(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Medico medico = session.get(Medico.class, id);
            if (medico != null) {
                session.remove(medico);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        }
    }

    // ========== FARMACEUTAS ==========
    public Farmaceuta createFarmaceuta(String nombre, String clave) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Farmaceuta farmaceuta = new Farmaceuta(nombre, clave);
            session.persist(farmaceuta);
            tx.commit();
            return farmaceuta;
        }
    }

    public Farmaceuta getFarmaceutaById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Farmaceuta.class, id);
        }
    }

    public List<Farmaceuta> getAllFarmaceutas() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Farmaceuta", Farmaceuta.class).list();
        }
    }

    public Farmaceuta updateFarmaceuta(Long id, String nombre, String clave) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Farmaceuta farmaceuta = session.get(Farmaceuta.class, id);
            if (farmaceuta != null) {
                farmaceuta.setNombre(nombre);
                if (clave != null && !clave.isEmpty()) {
                    farmaceuta.setClave(clave);
                }
                session.merge(farmaceuta);
            }
            tx.commit();
            return farmaceuta;
        }
    }

    public boolean deleteFarmaceuta(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Farmaceuta farmaceuta = session.get(Farmaceuta.class, id);
            if (farmaceuta != null) {
                session.remove(farmaceuta);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        }
    }

    // ========== AUTENTICACIÓN ==========
    public Usuario login(String nombre, String clave) {
        try (Session session = sessionFactory.openSession()) {
            Query<Usuario> query = session.createQuery(
                    "FROM Usuario WHERE nombre = :nombre AND clave = :clave",
                    Usuario.class
            );
            query.setParameter("nombre", nombre);
            query.setParameter("clave", clave);
            return query.uniqueResult();
        }
    }

    public String getTipoUsuario(Usuario usuario) {
        if (usuario instanceof Administrador) return "ADMINISTRADOR";
        if (usuario instanceof Medico) return "MEDICO";
        if (usuario instanceof Farmaceuta) return "FARMACEUTA";
        return "DESCONOCIDO";
    }
}