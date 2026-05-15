package com.pharmacy.patterns.state;

import com.pharmacy.model.Medicine;

// State Pattern - State interface
public interface MedicineState {
    void handleState(Medicine medicine);
    String getStatusString();
    boolean canAddToCart();
}
