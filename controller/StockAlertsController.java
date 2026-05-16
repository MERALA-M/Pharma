package com.pharmacy.controller;

import com.pharmacy.model.StockAlert;
import com.pharmacy.model.Medicine;
import com.pharmacy.service.DataStore;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StockAlertsController implements Initializable {

    @FXML private TableView<StockAlert> tableAlerts;
    @FXML private TableColumn<StockAlert, String> colMedicine;
    @FXML private TableColumn<StockAlert, String> colPharmacist;
    @FXML private TableColumn<StockAlert, String> colPriority;
    @FXML private TableColumn<StockAlert, String> colStatus;
    @FXML private TableColumn<StockAlert, String> colMessage;
    
    @FXML private HBox adminActionsBox;
    @FXML private Label lblTitle;

    private DataStore dataStore;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataStore = DataStore.getInstance();
        com.pharmacy.model.User currentUser = com.pharmacy.service.SessionManager.getInstance().getCurrentUser();
        
        setupTable(currentUser);
        configureViewForRole(currentUser);
    }

    private void configureViewForRole(com.pharmacy.model.User user) {
        boolean isAdmin = "ADMIN".equals(user.getRole().toUpperCase());
        
        if (adminActionsBox != null) {
            adminActionsBox.setVisible(isAdmin);
            adminActionsBox.setManaged(isAdmin);
        }
        
        if (lblTitle != null) {
            lblTitle.setText(isAdmin ? "Stock Alerts / Requests" : "Inventory Warnings / My Stock Requests");
        }
    }

    private void setupTable(com.pharmacy.model.User user) {
        colMedicine.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMedicine().getName()));
        colPharmacist.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPharmacistUsername()));
        colPriority.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPriority()));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus()));
        colMessage.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMessage()));

        if ("ADMIN".equals(user.getRole().toUpperCase())) {
            tableAlerts.setItems(dataStore.getStockAlerts());
        } else {
            // Pharmacist only sees their own requests
            javafx.collections.transformation.FilteredList<StockAlert> myRequests = 
                new javafx.collections.transformation.FilteredList<>(dataStore.getStockAlerts(), 
                a -> a.getPharmacistUsername().equals(user.getUsername()));
            tableAlerts.setItems(myRequests);
        }
    }

    @FXML
    public void handleRestock(ActionEvent event) {
        StockAlert selected = tableAlerts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an alert to restock.").show();
            return;
        }

        Medicine med = selected.getMedicine();
        TextInputDialog dialog = new TextInputDialog(String.valueOf(med.getQuantity()));
        dialog.setTitle("Restock Medicine");
        dialog.setHeaderText("Add stock for " + med.getName());
        dialog.setContentText("Enter new total quantity:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int newQty = Integer.parseInt(result.get());
                med.setQuantity(newQty);
                tableAlerts.refresh();
                new Alert(Alert.AlertType.INFORMATION, med.getName() + " restocked to " + newQty).show();
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid quantity entered.").show();
            }
        }
    }

    @FXML
    public void handleResolve(ActionEvent event) {
        StockAlert selected = tableAlerts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an alert to resolve.").show();
            return;
        }

        selected.setStatus("Resolved");
        tableAlerts.refresh();
        new Alert(Alert.AlertType.INFORMATION, "Request marked as Resolved.").show();
    }
}
