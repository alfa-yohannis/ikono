package org.example.patterns.observer;

import org.example.model.SalesReturn;

public interface Observer {
    void update(SalesReturn salesReturn, String action);
}
