package org.example.service_layer;

import org.example.utilities.ChangeType;

public interface IServiceObserver<T> {
    void onDataChanged(ChangeType type, T entity);
}
