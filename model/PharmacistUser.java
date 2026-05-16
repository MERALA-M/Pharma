package com.pharmacy.model;

public class PharmacistUser extends User {
    public PharmacistUser(String id, String username, String password) {
        super(id, username, password, "Pharmacist");
    }

    @Override public boolean canManageUsers() { return false; }
    @Override public boolean canManageSuppliers() { return false; }
    @Override public boolean canDeleteMedicine() { return false; }
    @Override public boolean canAddEditMedicine() { return false; } // Cannot add/edit overall details, only update stock later
    @Override public boolean canViewReports() { return false; }
    @Override public boolean canAccessSettings() { return false; }
    @Override public boolean canAccessPOS() { return true; }
    @Override public boolean canViewDashboard() { return true; }
    @Override public boolean canAccessStorefront() { return false; }
    @Override public boolean canManagePrescriptions() { return true; }
}
