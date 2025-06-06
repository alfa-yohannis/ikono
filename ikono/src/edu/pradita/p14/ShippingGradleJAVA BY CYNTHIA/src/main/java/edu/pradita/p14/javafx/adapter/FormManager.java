package edu.pradita.p14.javafx.adapter;

import edu.pradita.p14.javafx.IForm;

public class FormManager {
    private IForm form;
    public FormManager(IForm form) { this.form = form; }
    public String getDocumentCode() {
        return form.getDocumentCode();
    }
}