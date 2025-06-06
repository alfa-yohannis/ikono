package edu.pradita.p14.javafx.adapter;

import edu.pradita.p14.javafx.IForm;

// Adapter: Tambahkan "implements IReportDataProvider"
public class ReportAdapter implements IReportDataProvider {
    private FormManager formManager;

    public ReportAdapter(IForm form) {
        this.formManager = new FormManager(form);
    }

    @Override
    public String getReportDataAsXML() {
        // Adapts the simple string from getDocumentCode into an XML format
        String code = formManager.getDocumentCode();
        return "<report><orderCode>" + code + "</orderCode></report>";
    }
}