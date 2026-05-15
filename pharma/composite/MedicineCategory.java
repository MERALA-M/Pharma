package com.pharmacy.patterns.composite;

import java.util.ArrayList;
import java.util.List;

// Composite Pattern - Composite
public class MedicineCategory implements MedicineComponent {
    private String categoryName;
    private List<MedicineComponent> children = new ArrayList<>();

    public MedicineCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String getName() {
        return categoryName;
    }

    @Override
    public double getPrice() {
        return children.stream().mapToDouble(MedicineComponent::getPrice).sum();
    }

    @Override
    public void displayHierarchy(String indent) {
        System.out.println(indent + "+ Category: " + categoryName);
        for (MedicineComponent child : children) {
            child.displayHierarchy(indent + "  ");
        }
    }

    @Override
    public void add(MedicineComponent component) {
        children.add(component);
    }

    @Override
    public void remove(MedicineComponent component) {
        children.remove(component);
    }

    @Override
    public List<MedicineComponent> getChildren() {
        return children;
    }
}
