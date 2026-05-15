package com.pharmacy.patterns;

import com.pharmacy.model.AdminUser;
import com.pharmacy.model.CustomerUser;
import com.pharmacy.model.PharmacistUser;
import com.pharmacy.model.User;

// Factory Method Pattern
public class UserFactory {
    
    public static User createUser(String role, String id, String username, String password) {
        if (role == null) {
            return null;
        }
        
        switch (role.toUpperCase()) {
            case "ADMIN":
                return new AdminUser(id, username, password);
            case "PHARMACIST":
                return new PharmacistUser(id, username, password);
            case "CUSTOMER":
                return new CustomerUser(id, username, password);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
