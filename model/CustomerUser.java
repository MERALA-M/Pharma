package com.pharmacy.model;

import java.util.HashSet;
import java.util.Set;

public class CustomerUser extends User {
    private Set<String> favoriteMedicines = new HashSet<>();

    public CustomerUser(String id, String username, String password) {
        super(id, username, password, "Customer");
    }

    public Set<String> getFavoriteMedicines() {
        return favoriteMedicines;
    }

    public void addFavorite(String medicineId) {
        favoriteMedicines.add(medicineId);
    }

    public void removeFavorite(String medicineId) {
        favoriteMedicines.remove(medicineId);
    }

    @Override public boolean canManageUsers() { return false; }
    @Override public boolean canManageSuppliers() { return false; }
    @Override public boolean canDeleteMedicine() { return false; }
    @Override public boolean canAddEditMedicine() { return false; }
    @Override public boolean canViewReports() { return false; }
    @Override public boolean canAccessSettings() { return false; }
    @Override public boolean canAccessPOS() { return true; } // Can access cart/store
    @Override public boolean canViewDashboard() { return true; }
    @Override public boolean canAccessStorefront() { return true; }
    @Override public boolean canManagePrescriptions() { return false; }
}
