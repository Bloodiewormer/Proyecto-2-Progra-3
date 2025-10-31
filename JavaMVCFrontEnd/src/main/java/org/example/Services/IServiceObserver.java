package org.example.Services;

import org.example.utilities.ChangeType;

import java.util.List;

public interface IServiceObserver<T> {
    void onDataChanged(ChangeType type, T entity);

    void onDataChanged(List<T> medicamentos);
}
