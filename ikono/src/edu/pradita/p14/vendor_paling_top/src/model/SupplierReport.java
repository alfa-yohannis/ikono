package model;

public class SupplierReport {
    private String supplierName;
    private double totalPurchase;

    public SupplierReport(String supplierName, double totalPurchase) {
        this.supplierName = supplierName;
        this.totalPurchase = totalPurchase;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public double getTotalPurchase() {
        return totalPurchase;
    }
}
