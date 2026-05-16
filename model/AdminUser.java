package com.pharmacy.model;

public class AdminUser extends User {
    public AdminUser(String id, String username, String password) {
        super(id, username, password, "Admin");
    }

    @Override public boolean canManageUsers() { return true; }
    @Override public boolean canManageSuppliers() { return true; }
    @Override public boolean canDeleteMedicine() { return true; }
    @Override public boolean canAddEditMedicine() { return true; }
    @Override public boolean canViewReports() { return true; }
    @Override public boolean canAccessSettings() { return true; }
    @Override public boolean canAccessPOS() { return true; }
    @Override public boolean canViewDashboard() { return true; }
    @Override public boolean canAccessStorefront() { return false; }
    @Override public boolean canManagePrescriptions() { return true; }
}
