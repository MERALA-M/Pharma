package com.pharmacy.controller;

import com.pharmacy.model.Medicine;
import com.pharmacy.model.User;
import com.pharmacy.patterns.InventoryObserver;
import com.pharmacy.patterns.InventorySubject;
import com.pharmacy.service.DataStore;
import com.pharmacy.service.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable, InventoryObserver {

    @FXML private Label lblWelcome;
    @FXML private Label lblSubtitle;
    @FXML private GridPane adminCards;
    @FXML private GridPane customerCards;
    @FXML private VBox alertsSection;
    @FXML private Label lblAlertsTitle;

    @FXML private Label lblTotalMedicines;
    @FXML private Label lblLowStock;
    @FXML private Label lblOutOfStock;
    @FXML private Label lblTotalCustomers;
    @FXML private Label lblCustomerOrders;
    
    @FXML private ListView<String> listAlerts;

    private DataStore dataStore;
    private ObservableList<String> alerts;
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataStore = DataStore.getInstance();
        currentUser = SessionManager.getInstance().getCurrentUser();
        alerts = FXCollections.observableArrayList();
        listAlerts.setItems(alerts);

        // Abstract Factory Pattern
        com.pharmacy.patterns.ui.UIFactory uiFactory = new com.pharmacy.patterns.ui.LuxuryUIFactory();
        javafx.scene.control.Label luxuryLabel = uiFactory.createLabel("💎 LUXURY PHARMA ACCESS", "header-label");
        luxuryLabel.setStyle("-fx-text-fill: #0D9488; -fx-font-weight: bold; -fx-font-size: 14px;");
        alertsSection.getChildren().add(0, luxuryLabel);

        configureViewForRole();

        if (!"CUSTOMER".equals(currentUser.getRole().toUpperCase())) {
            // Register as Observer only for staff
            InventorySubject.getInstance().addObserver(this);
        }

        updateDashboard();
    }

    private void configureViewForRole() {
        if ("CUSTOMER".equals(currentUser.getRole().toUpperCase())) {
            lblWelcome.setText("Welcome, " + currentUser.getUsername());
            lblSubtitle.setText("Your personal health and wellness dashboard");
            adminCards.setVisible(false);
            adminCards.setManaged(false);
            customerCards.setVisible(true);
            customerCards.setManaged(true);
            lblAlertsTitle.setText("Your Recent Notifications");
        } else {
            lblWelcome.setText("Overview Dashboard");
            lblSubtitle.setText("Real-time analytics and inventory alerts");
            adminCards.setVisible(true);
            adminCards.setManaged(true);
            customerCards.setVisible(false);
            customerCards.setManaged(false);
            lblAlertsTitle.setText("Live Inventory Alerts");
        }
    }

    private void updateDashboard() {
        alerts.clear();

        if ("CUSTOMER".equals(currentUser.getRole().toUpperCase())) {
            System.out.println("[DASHBOARD] current customer = " + currentUser.getUsername());
            long customerOrders = dataStore.getInvoices().stream()
                .filter(inv -> inv.getCustomer() != null && inv.getCustomer().getEmail().toLowerCase().startsWith(currentUser.getUsername().toLowerCase()))
                .count();
            System.out.println("[DASHBOARD] my orders count = " + customerOrders);
            lblCustomerOrders.setText(String.valueOf(customerOrders));
            
            // Show notifications for customer
            alerts.add("Welcome back! Check out our new health products in the store.");
            dataStore.getRequests().stream()
                .filter(r -> r.getCustomer() != null && r.getCustomer().getName().equals(currentUser.getUsername()) && r.getReply() != null)
                .forEach(r -> alerts.add("Reply from Pharmacist: " + r.getReply()));
            
            dataStore.getInvoices().stream()
                .filter(inv -> inv.getCustomer() != null && inv.getCustomer().getEmail().toLowerCase().startsWith(currentUser.getUsername().toLowerCase()))
                .limit(3)
                .forEach(inv -> alerts.add("Order status: " + inv.getId() + " - COMPLETED"));

        } else if ("PHARMACIST".equals(currentUser.getRole().toUpperCase())) {
            updateStaffStats();
            // Pharmacists see low stock and resolved requests
            dataStore.getMedicines().stream()
                .filter(m -> "Low Stock".equals(m.getStatus()) || "Out of Stock".equals(m.getStatus()))
                .forEach(m -> alerts.add("Inventory Alert: " + m.getName() + " is " + m.getStatus()));
            
            dataStore.getStockAlerts().stream()
                .filter(a -> a.getPharmacistUsername().equals(currentUser.getUsername()) && "Resolved".equals(a.getStatus()))
                .forEach(a -> alerts.add("Stock Request Resolved: " + a.getMedicine().getName()));
                
        } else if ("ADMIN".equals(currentUser.getRole().toUpperCase())) {
            updateStaffStats();
            // Admin sees everything
            dataStore.getMedicines().stream()
                .filter(m -> !"Normal".equals(m.getStatus()))
                .forEach(m -> alerts.add(m.getStatus() + ": " + m.getName()));
            
            long pendingRequests = dataStore.getStockAlerts().stream()
                .filter(a -> "Pending".equals(a.getStatus()))
                .count();
            if (pendingRequests > 0) {
                alerts.add("SYSTEM: There are " + pendingRequests + " pending stock requests from Pharmacists.");
            }
        }
    }

    private void updateStaffStats() {
        int totalMedicines = dataStore.getMedicines().size();
        int lowStockCount = (int) dataStore.getMedicines().stream().filter(m -> "Low Stock".equals(m.getStatus())).count();
        int outOfStockCount = (int) dataStore.getMedicines().stream().filter(m -> "Out of Stock".equals(m.getStatus())).count();

        lblTotalMedicines.setText(String.valueOf(totalMedicines));
        lblLowStock.setText(String.valueOf(lowStockCount));
        lblOutOfStock.setText(String.valueOf(outOfStockCount));
        lblTotalCustomers.setText(String.valueOf(dataStore.getCustomers().size()));
    }

    @Override
    public void onStockAlert(Medicine medicine, String alertType) {
        if (!"CUSTOMER".equals(currentUser.getRole().toUpperCase())) {
            Platform.runLater(() -> {
                alerts.add(alertType + ": " + medicine.getName() + " (Qty: " + medicine.getQuantity() + ")");
                updateDashboard();
            });
        }
    }
    
    public void cleanup() {
        if (!"CUSTOMER".equals(currentUser.getRole().toUpperCase())) {
            InventorySubject.getInstance().removeObserver(this);
        }
    }
}
