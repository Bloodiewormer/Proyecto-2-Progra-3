package org.example.Presentation;

import org.example.Utilities.ChangeType;
import java.util.ArrayList;
import java.util.List;

public class Observable {

    private final List<IObserver> observers = new ArrayList<>();

    public void addObserver(IObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(ChangeType changeType, Object data) {
        for (IObserver observer : observers) {
            observer.update(changeType, data);
        }
    }
}