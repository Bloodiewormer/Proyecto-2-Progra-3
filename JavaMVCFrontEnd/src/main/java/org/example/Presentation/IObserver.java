package org.example.Presentation;

import org.example.Utilities.ChangeType;

public interface IObserver {
    void update(ChangeType changeType, Object data);
}
