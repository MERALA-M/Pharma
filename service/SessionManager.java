package com.pharmacy.service;

import com.pharmacy.model.User;
import com.pharmacy.model.InvoiceItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Singleton Pattern
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private ObservableList<InvoiceItem> cart;

    private SessionManager() {
        cart = FXCollections.observableArrayList();
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ObservableList<InvoiceItem> getCart() {
        return cart;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
        this.cart.clear();
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
