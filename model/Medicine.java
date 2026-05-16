package com.pharmacy.model;

import com.pharmacy.patterns.Prototype;
import com.pharmacy.patterns.composite.MedicineComponent;
import com.pharmacy.patterns.state.*;

import java.time.LocalDate;

// Prototype and Leaf in Composite Pattern
public class Medicine implements Prototype<Medicine>, MedicineComponent {
    private String id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private LocalDate expiryDate;
    private String supplierId;
    private String description;
    private MedicineState state;

    public Medicine(String id, String name, String category, double price, int quantity, LocalDate expiryDate, String supplierId, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.supplierId = supplierId;
        this.description = description;
        updateState();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
        updateState();
    }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { 
        this.expiryDate = expiryDate; 
        updateState();
    }
    
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { 
        return state != null ? state.getStatusString() : "Unknown"; 
    }

    public boolean canAddToCart() {
        return state != null && state.canAddToCart();
    }

    public void updateState() {
        if (quantity == 0) {
            this.state = new OutOfStockState();
        } else if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            this.state = new ExpiredState();
        } else if (quantity < 10) {
            this.state = new LowStockState();
        } else {
            this.state = new AvailableState();
        }
    }

    // Prototype pattern: clone
    @Override
    public Medicine clone() {
        return new Medicine(this.id + "_copy", this.name, this.category, this.price, this.quantity, this.expiryDate, this.supplierId, this.description);
    }

    // --- Composite Pattern Methods ---
    @Override
    public void displayHierarchy(String indent) {
        System.out.println(indent + "- " + name + " ($" + price + ")");
    }
}
