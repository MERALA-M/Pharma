package com.pharmacy.patterns;

import com.pharmacy.model.Medicine;
import java.util.ArrayList;
import java.util.List;

// Subject in Observer Pattern
public class InventorySubject {
    private List<InventoryObserver> observers = new ArrayList<>();
    
    // Singleton for the subject
    private static InventorySubject instance;
    
    private InventorySubject() {}
    
    public static synchronized InventorySubject getInstance() {
        if (instance == null) {
            instance = new InventorySubject();
        }
        return instance;
    }

    public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Medicine medicine, String alertType) {
        for (InventoryObserver observer : observers) {
            observer.onStockAlert(medicine, alertType);
        }
    }

    public void checkMedicine(Medicine medicine) {
        if (medicine.getQuantity() == 0) {
            notifyObservers(medicine, "Out of Stock");
        } else if (medicine.getQuantity() < 10) {
            notifyObservers(medicine, "Low Stock");
        }
    }
}
