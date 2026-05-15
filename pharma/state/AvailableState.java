package com.pharmacy.patterns.state;

import com.pharmacy.model.Medicine;

public class AvailableState implements MedicineState {
    @Override
    public void handleState(Medicine medicine) {
        // No specific action required, medicine is available.
    }

    @Override
    public String getStatusString() {
        return "Available";
    }

    @Override
    public boolean canAddToCart() {
        return true;
    }
}
