package org.example.patterns.decorator;

import org.example.model.SalesReturn;

public interface ReturnProcess {
    void handleReturn(SalesReturn salesReturn);
    String getDescription();
}
