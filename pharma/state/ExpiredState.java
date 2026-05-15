package com.pharmacy.patterns.state;

import com.pharmacy.model.Medicine;

public class ExpiredState implements MedicineState {
    @Override
    public void handleState(Medicine medicine) {
        // Must be removed from shelves and sales blocked.
    }

    @Override
    public String getStatusString() {
        return "Expired";
    }

    @Override
    public boolean canAddToCart() {
        return false;
    }
}
