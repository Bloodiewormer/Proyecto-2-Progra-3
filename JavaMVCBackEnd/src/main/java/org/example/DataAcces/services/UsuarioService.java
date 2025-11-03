package org.example.DataAcces.services;

import org.example.Domain.models.*;
import org.example.Utilities.PasswordUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Servicio para gestionar usuarios (Médicos, Farmaceutas, Administradores)
 */
public class UsuarioService {
    private final SessionFactory sessionFactory;

    public UsuarioService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // ========== MÉDICOS ==========

    /**
     * Crea un nuevo médico
     */
    public Medico createMedico(String nombre, String clave, String especialidad) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Usuario usuarioExistente = getUserByNombre(nombre);
            if (usuarioExistente != null) {
                throw new IllegalArgumentException("Ya existe un usuario con ese nombre");
            }

            // Generar salt y hashear contraseña
            String salt = PasswordUtils.generateSalt();
            String hashedPassword = PasswordUtils.hashPassword(clave, salt);

            Medico medico = new Medico(nombre, hashedPassword, especialidad);
            medico.setSalt(salt);
            session.persist(medico);
            tx.commit();

            System.out.println("[UsuarioService] Médico creado: " + nombre);
            return medico;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[UsuarioService] Error creando médico: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene un médico por ID
     */
    public Medico getMedicoById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Medico.class, id);
        } catch (Exception e) {
            System.err.println("[UsuarioService] Error obteniendo médico: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene todos los médicos
     */
    public List<Medico> getAllMedicos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Medico", Medico.class).list();
        } catch (Exception e) {
            System.err.println("[UsuarioService] Error obteniendo médicos: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Actualiza un médico
     */
    public Medico updateMedico(Long id, String nombre, String clave, String especialidad) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Medico medico = session.find(Medico.class, id);
            if (medico != null) {
                medico.setNombre(nombre);
                if (clave != null && !clave.isEmpty()) {
                    medico.setClave(clave);
                }
                medico.setEspecialidad(especialidad);
                session.merge(medico);
            }

            tx.commit();
            System.out.println("[UsuarioService] Médico actualizado: " + nombre);
            return medico;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[UsuarioService] Error actualizando médico: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Elimina un médico
     */
    public boolean deleteMedico(Long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Medico medico = session.find(Medico.class, id);
            if (medico != null) {
                session.remove(medico);
                tx.commit();
                System.out.println("[UsuarioService] Médico eliminado: " + id);
                return true;
            }

            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[UsuarioService] Error eliminando médico: " + e.getMessage());
            throw e;
        }
    }

    // ========== FARMACEUTAS ==========

    /**
     * Crea un nuevo farmaceuta
     */
    public Farmaceuta createFarmaceuta(String nombre, String clave) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            // Verificar que no exista un usuario con ese nombre
            Usuario usuarioExistente = getUserByNombre(nombre);
            if (usuarioExistente != null) {
                throw new IllegalArgumentException("Ya existe un usuario con ese nombre");
            }

            String salt = PasswordUtils.generateSalt();
            String hashedPassword = PasswordUtils.hashPassword(clave, salt);

            Farmaceuta farmaceuta = new Farmaceuta(nombre, hashedPassword);
            farmaceuta.setSalt(salt);
            session.persist(farmaceuta);
            tx.commit();

            System.out.println("[UsuarioService] Farmaceuta creado: " + nombre);
            return farmaceuta;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[UsuarioService] Error creando farmaceuta: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene un farmaceuta por ID
     */
    public Farmaceuta getFarmaceutaById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Farmaceuta.class, id);
        } catch (Exception e) {
            System.err.println("[UsuarioService] Error obteniendo farmaceuta: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene todos los farmaceutas
     */
    public List<Farmaceuta> getAllFarmaceutas() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Farmaceuta", Farmaceuta.class).list();
        } catch (Exception e) {
            System.err.println("[UsuarioService] Error obteniendo farmaceutas: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Actualiza un farmaceuta
     */
    public Farmaceuta updateFarmaceuta(Long id, String nombre, String clave) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Farmaceuta farmaceuta = session.find(Farmaceuta.class, id);
            if (farmaceuta != null) {
                farmaceuta.setNombre(nombre);
                if (clave != null && !clave.isEmpty()) {
                    farmaceuta.setClave(clave);
                }
                session.merge(farmaceuta);
            }

            tx.commit();
            System.out.println("[UsuarioService] Farmaceuta actualizado: " + nombre);
            return farmaceuta;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[UsuarioService] Error actualizando farmaceuta: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Elimina un farmaceuta
     */
    public boolean deleteFarmaceuta(Long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Farmaceuta farmaceuta = session.find(Farmaceuta.class, id);
            if (farmaceuta != null) {
                session.remove(farmaceuta);
                tx.commit();
                System.out.println("[UsuarioService] Farmaceuta eliminado: " + id);
                return true;
            }

            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[UsuarioService] Error eliminando farmaceuta: " + e.getMessage());
            throw e;
        }
    }

    // ========== AUTENTICACIÓN ==========

    /**
     * Realiza login de un usuario
     */
    public Usuario login(int userId, String clave) {
        try (Session session = sessionFactory.openSession()) {
            // Convertir int a Long para JPA
            Usuario usuario = session.find(Usuario.class, (long) userId);

            if (usuario == null) {
                System.out.println("[UsuarioService] Usuario no encontrado con ID: " + userId);
                return null;
            }

            // Verificar que el usuario esté activo
            if (!usuario.getIsActive()) {
                System.out.println("[UsuarioService] Usuario inactivo con ID: " + userId);
                return null;
            }

            // Verificar contraseña con salt
            if (PasswordUtils.verifyPassword(clave, usuario.getSalt(), usuario.getClave())) {
                System.out.println("[UsuarioService] Login exitoso - ID: " + userId +
                        ", Nombre: " + usuario.getNombre());
                return usuario;
            }

            System.out.println("[UsuarioService] Contraseña incorrecta para ID: " + userId);
            return null;
        } catch (Exception e) {
            System.err.println("[UsuarioService] Error en login para ID " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene el tipo de usuario (MEDICO, FARMACEUTA, ADMINISTRADOR)
     */
    public String getTipoUsuario(Usuario usuario) {
        if (usuario instanceof Administrador) {
            return "ADMINISTRADOR";
        } else if (usuario instanceof Medico) {
            return "MEDICO";
        } else if (usuario instanceof Farmaceuta) {
            return "FARMACEUTA";
        }
        return "DESCONOCIDO";
    }

    /**
     * Obtiene un usuario por nombre (método auxiliar)
     */
    public Usuario getUserByNombre(String nombre) {
        try (Session session = sessionFactory.openSession()) {
            Query<Usuario> query = session.createQuery(
                    "FROM Usuario WHERE nombre = :nombre",
                    Usuario.class
            );
            query.setParameter("nombre", nombre);
            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("[UsuarioService] ❌ Error obteniendo por nombre: " + e.getMessage());
            return null;
        }
    }

    public Usuario getUserById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Usuario.class, id);
        } catch (Exception e) {
            System.err.println("[UsuarioService] Error obteniendo usuario: " + e.getMessage());
            throw e;
        }
    }

    public void updatePassword(Long userId, String newHash, String newSalt) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Usuario usuario = session.find(Usuario.class, userId);
            if (usuario != null) {
                usuario.setClave(newHash);
                usuario.setSalt(newSalt);
                usuario.setIsActive(true);
                session.merge(usuario);
            }

            tx.commit();
            System.out.println("[UsuarioService] Contraseña actualizada y usuario activado: " + userId);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[UsuarioService] Error actualizando contraseña: " + e.getMessage());
            throw e;
        }
    }

    public void activateUser(Long userId) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Usuario usuario = session.find(Usuario.class, userId);
            if (usuario != null) {
                usuario.setIsActive(true);
                session.merge(usuario);
            }

            tx.commit();
            System.out.println("[UsuarioService] Usuario activado: " + userId);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[UsuarioService] Error activando usuario: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Desactivar usuario (logout o desconexión)
     */
    public void deactivateUser(Long userId) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Usuario usuario = session.find(Usuario.class, userId);
            if (usuario != null) {
                usuario.setIsActive(false);
                session.merge(usuario);
            }

            tx.commit();
            System.out.println("[UsuarioService] 👋 Usuario desactivado: " + userId);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[UsuarioService] ❌ Error desactivando: " + e.getMessage());
            throw e;
        }
    }

    public List<Usuario> getActiveUsers() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Usuario u WHERE u.isActive = true ORDER BY u.nombre";
            Query<Usuario> query = session.createQuery(hql, Usuario.class);
            List<Usuario> usuarios = query.list();
            System.out.println("[UsuarioService] 📋 Usuarios activos: " + usuarios.size());
            return usuarios;
        } catch (Exception e) {
            System.err.println("[UsuarioService] ❌ Error obteniendo activos: " + e.getMessage());
            throw e;
        }
    }



}