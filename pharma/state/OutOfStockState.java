package com.pharmacy.patterns.state;

import com.pharmacy.model.Medicine;

public class OutOfStockState implements MedicineState {
    @Override
    public void handleState(Medicine medicine) {
        // Stop sales for this item.
    }

    @Override
    public String getStatusString() {
        return "Out of Stock";
    }

    @Override
    public boolean canAddToCart() {
        return false;
    }
}
