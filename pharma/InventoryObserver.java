package com.pharmacy.patterns;

import com.pharmacy.model.Medicine;

// Observer Pattern Interface
public interface InventoryObserver {
    void onStockAlert(Medicine medicine, String alertType);
}
