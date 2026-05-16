package com.pharmacy.model;

public class StockAlert {
    private String id;
    private Medicine medicine;
    private String pharmacistUsername;
    private String priority; // Low, Medium, High
    private String status;   // Pending, Resolved
    private String message;
    private long timestamp;

    public StockAlert(Medicine medicine, String pharmacistUsername, String priority, String message) {
        this.id = "ALT-" + System.currentTimeMillis();
        this.medicine = medicine;
        this.pharmacistUsername = pharmacistUsername;
        this.priority = priority;
        this.message = message;
        this.status = "Pending";
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() { return id; }
    public Medicine getMedicine() { return medicine; }
    public String getPharmacistUsername() { return pharmacistUsername; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }
}
