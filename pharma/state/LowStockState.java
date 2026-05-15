package com.pharmacy.patterns.state;

import com.pharmacy.model.Medicine;

public class LowStockState implements MedicineState {
    @Override
    public void handleState(Medicine medicine) {
        // System could trigger reorder logic here.
    }

    @Override
    public String getStatusString() {
        return "Low Stock";
    }

    @Override
    public boolean canAddToCart() {
        return true;
    }
}
