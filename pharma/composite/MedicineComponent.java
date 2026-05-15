package com.pharmacy.patterns.composite;

import java.util.List;

// Composite Pattern - Component
public interface MedicineComponent {
    String getName();
    double getPrice();
    void displayHierarchy(String indent);
    default void add(MedicineComponent component) {
        throw new UnsupportedOperationException();
    }
    default void remove(MedicineComponent component) {
        throw new UnsupportedOperationException();
    }
    default List<MedicineComponent> getChildren() {
        throw new UnsupportedOperationException();
    }
}
