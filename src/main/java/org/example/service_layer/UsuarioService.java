package org.example.service_layer;

import org.example.data_access_layer.IFileStore;
import org.example.domain_layer.Usuario;
import org.example.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService implements IService<Usuario> {

    private final IFileStore<Usuario> fileStore;
    private final List<IServiceObserver<Usuario>> observers ;

    public UsuarioService(IFileStore<Usuario> fileStore) {
        this.fileStore = fileStore;
        this.observers = new ArrayList<>();
    }

    @Override
    public void agregar(Usuario usuario) {
        List <Usuario> usuarios = fileStore.readAll();
        //check if id is unique
        for (Usuario u : usuarios) {
            if (u.getId() == usuario.getId()) {
                throw new IllegalArgumentException("ID ya existe");
            }
        }
        usuarios.add(usuario);
        fileStore.writeAll(usuarios);
        notifyObservers(ChangeType.CREATED, usuario);
    }

    @Override
    public void borrar(int id) {
        List<Usuario> usuarios = fileStore.readAll();
        Usuario removed = null;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == id) { removed = usuarios.remove(i); break; }
        }
        fileStore.writeAll(usuarios);
        if (removed != null) notifyObservers(ChangeType.DELETED, removed);
    }

    @Override
    public void actualizar(Usuario usuario) {
        List<Usuario> usuarios = fileStore.readAll();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuario.getId()) {
                usuarios.set(i, usuario);
                break;
            }
        }
        fileStore.writeAll(usuarios);
        notifyObservers(ChangeType.UPDATED, usuario);
    }

    @Override
    public List<Usuario> leerTodos() {
        return fileStore.readAll();
    }

    @Override
    public Usuario leerPorId(int id) {
        return fileStore.readAll()
                .stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addObserver(IServiceObserver<Usuario> listener) {
        if(listener != null) observers.add(listener);
    }

    private void notifyObservers(ChangeType type, Usuario entity) {
        for (IServiceObserver<Usuario> observer : observers) {
            observer.onDataChanged(type, entity);
        }
    }
}
