package edu.pradita.p14.javafx;

public interface IForm {

    /**
     * This method must be implemented by any class that represents a form
     * from which a report can be printed. It's designed to return a unique
     * document code (like an order number) for report generation.
     * @return A string representing the document's code.
     */
    public String getDocumentCode();
}