package com.pharmacy.patterns;

import com.pharmacy.model.User;
import com.pharmacy.service.SessionManager;

// Proxy Pattern
public class ScreenAccessProxy {
    
    private static User getCurrentUser() {
        return SessionManager.getInstance().getCurrentUser();
    }

    public static boolean canManageUsers() {
        User user = getCurrentUser();
        return user != null && user.canManageUsers();
    }

    public static boolean canManageSuppliers() {
        User user = getCurrentUser();
        return user != null && user.canManageSuppliers();
    }

    public static boolean canDeleteMedicine() {
        User user = getCurrentUser();
        return user != null && user.canDeleteMedicine();
    }

    public static boolean canAddEditMedicine() {
        User user = getCurrentUser();
        return user != null && user.canAddEditMedicine();
    }

    public static boolean canViewReports() {
        User user = getCurrentUser();
        return user != null && user.canViewReports();
    }

    public static boolean canAccessSettings() {
        User user = getCurrentUser();
        return user != null && user.canAccessSettings();
    }

    public static boolean canAccessPOS() {
        User user = getCurrentUser();
        return user != null && user.canAccessPOS();
    }

    public static boolean canViewDashboard() {
        User user = getCurrentUser();
        return user != null && user.canViewDashboard();
    }

    public static boolean canAccessStorefront() {
        User user = getCurrentUser();
        return user != null && user.canAccessStorefront();
    }

    public static boolean canManagePrescriptions() {
        User user = getCurrentUser();
        return user != null && user.canManagePrescriptions();
    }
}
