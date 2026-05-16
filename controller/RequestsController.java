package com.pharmacy.controller;

import com.pharmacy.model.Customer;
import com.pharmacy.model.CustomerRequest;
import com.pharmacy.model.User;
import com.pharmacy.patterns.CustomerRequestBuilder;
import com.pharmacy.patterns.state.CompletedRequestState;
import com.pharmacy.patterns.state.PendingRequestState;
import com.pharmacy.patterns.state.ReviewedRequestState;
import com.pharmacy.service.DataStore;
import com.pharmacy.service.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestsController implements Initializable {

    @FXML private TableView<CustomerRequest> tableRequests;
    @FXML private TableColumn<CustomerRequest, String> colId;
    @FXML private TableColumn<CustomerRequest, String> colCustomer;
    @FXML private TableColumn<CustomerRequest, String> colMessage;
    @FXML private TableColumn<CustomerRequest, String> colStatus;

    @FXML private VBox customerPanel;
    @FXML private TextArea txtNewRequest;
    @FXML private Button btnSendRequest;

    @FXML private VBox pharmacistPanel;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private TextArea txtReply;
    @FXML private Button btnSendReply;
    
    @FXML private TextArea txtViewReply;

    private DataStore dataStore;
    private User currentUser;
    private FilteredList<CustomerRequest> filteredRequests;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataStore = DataStore.getInstance();
        currentUser = SessionManager.getInstance().getCurrentUser();

        setupTable();
        configureRolePanels();

        tableRequests.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                if ("PHARMACIST".equals(currentUser.getRole().toUpperCase()) || "ADMIN".equals(currentUser.getRole().toUpperCase())) {
                    cmbStatus.setValue(newSel.getStatus());
                    txtReply.setText(newSel.getReply() != null ? newSel.getReply() : "");
                }
                txtViewReply.setText(newSel.getReply() != null ? newSel.getReply() : "No reply yet.");
            }
        });
    }

    private void setupTable() {
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        colCustomer.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCustomer() != null ? cellData.getValue().getCustomer().getName() : "Unknown"));
        colMessage.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMessage()));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        // Filter based on user role
        filteredRequests = new FilteredList<>(dataStore.getRequests(), req -> {
            if ("CUSTOMER".equals(currentUser.getRole().toUpperCase())) {
                // Show only their requests
                return req.getCustomer() != null && req.getCustomer().getEmail().toLowerCase().startsWith(currentUser.getUsername().toLowerCase()); 
            }
            return true; // Admin/Pharmacist see all
        });

        tableRequests.setItems(filteredRequests);
    }

    private void configureRolePanels() {
        boolean isCustomer = "CUSTOMER".equals(currentUser.getRole().toUpperCase());
        boolean isPharmacist = "PHARMACIST".equals(currentUser.getRole().toUpperCase()) || "ADMIN".equals(currentUser.getRole().toUpperCase());

        customerPanel.setVisible(isCustomer);
        customerPanel.setManaged(isCustomer);

        pharmacistPanel.setVisible(isPharmacist);
        pharmacistPanel.setManaged(isPharmacist);

        if (isPharmacist) {
            cmbStatus.getItems().addAll("Pending", "Reviewed", "Completed");
        }
    }

    @FXML
    public void handleSendRequest(ActionEvent event) {
        if (txtNewRequest.getText().isEmpty()) {
            showAlert("Error", "Please enter a message.");
            return;
        }

        // Find current customer
        Customer currentCustomer = null;
        for (Customer c : dataStore.getCustomers()) {
            // Simplified matching for demo
            if (c.getEmail().startsWith(currentUser.getUsername())) {
                currentCustomer = c;
                break;
            }
        }
        if (currentCustomer == null) {
            // Fallback
            currentCustomer = dataStore.getCustomers().get(0);
        }

        // Builder pattern
        CustomerRequest request = new CustomerRequestBuilder()
                .setId("REQ" + System.currentTimeMillis())
                .setCustomer(currentCustomer)
                .setMessage(txtNewRequest.getText())
                .build();

        dataStore.getRequests().add(request);
        txtNewRequest.clear();
        tableRequests.refresh();
        showAlert("Success", "Request sent successfully.");
    }

    @FXML
    public void handleSendReply(ActionEvent event) {
        CustomerRequest selected = tableRequests.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a request first.");
            return;
        }

        selected.setReply(txtReply.getText());
        
        // State Pattern
        String status = cmbStatus.getValue();
        if ("Pending".equals(status)) selected.setState(new PendingRequestState());
        else if ("Reviewed".equals(status)) selected.setState(new ReviewedRequestState());
        else if ("Completed".equals(status)) selected.setState(new CompletedRequestState());

        tableRequests.refresh();
        showAlert("Success", "Reply sent and status updated.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
