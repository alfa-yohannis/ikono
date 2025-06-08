package org.example.patterns.decorator;

import org.example.model.SalesReturn;

public abstract class ReturnProcessDecorator implements ReturnProcess {
    protected ReturnProcess decoratedProcess;

    public ReturnProcessDecorator(ReturnProcess decoratedProcess) {
        this.decoratedProcess = decoratedProcess;
    }

    @Override
    public void handleReturn(SalesReturn salesReturn) {
        decoratedProcess.handleReturn(salesReturn);
    }

    @Override
    public String getDescription() {
        return decoratedProcess.getDescription();
    }
}