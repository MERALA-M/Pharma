package com.pharmacy.model;

public class InvoiceItem {
    private Medicine medicine;
    private int quantity;
    private double unitPrice;
    private double total;

    public InvoiceItem(Medicine medicine, int quantity, double unitPrice) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = quantity * unitPrice;
    }

    public Medicine getMedicine() { return medicine; }
    public void setMedicine(Medicine medicine) { this.medicine = medicine; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
        this.total = this.quantity * this.unitPrice;
    }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { 
        this.unitPrice = unitPrice; 
        this.total = this.quantity * this.unitPrice;
    }

    public double getTotal() { return total; }
    
    public String getMedicineName() {
        return medicine != null ? medicine.getName() : "Unknown";
    }
}
