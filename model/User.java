package com.pharmacy.model;

public abstract class User {
    private String id;
    private String username;
    private String password;
    private String role;

    public User(String id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setPassword(String password) { this.password = password; }
    
    // Abstract permission methods
    public abstract boolean canManageUsers();
    public abstract boolean canManageSuppliers();
    public abstract boolean canDeleteMedicine();
    public abstract boolean canAddEditMedicine();
    public abstract boolean canViewReports();
    public abstract boolean canAccessSettings();
    public abstract boolean canAccessPOS();
    public abstract boolean canViewDashboard();
    public abstract boolean canAccessStorefront();
    public abstract boolean canManagePrescriptions();
}
