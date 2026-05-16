package com.pharmacy.controller;

import com.pharmacy.model.Supplier;
import com.pharmacy.model.User;
import com.pharmacy.patterns.UserFactory;
import com.pharmacy.service.DataStore;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminSettingsController implements Initializable {

    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colRole;

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbRole;

    @FXML private TableView<Supplier> tableSuppliers;
    @FXML private TableColumn<Supplier, String> colSupplierName;
    @FXML private TableColumn<Supplier, String> colSupplierContact;

    @FXML private TextField txtSupplierName;
    @FXML private TextField txtSupplierContact;

    private DataStore dataStore;
    private ObservableList<User> userList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataStore = DataStore.getInstance();
        userList = FXCollections.observableArrayList();
        
        cmbRole.getItems().addAll("ADMIN", "PHARMACIST", "CUSTOMER");
        cmbRole.getSelectionModel().select("PHARMACIST");

        setupUserTable();
        setupSupplierTable();
        refreshUserList();
    }

    private void setupUserTable() {
        colUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        colRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        tableUsers.setItems(userList);
    }

    private void setupSupplierTable() {
        colSupplierName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colSupplierContact.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        tableSuppliers.setItems(dataStore.getSuppliers());
    }

    private void refreshUserList() {
        userList.clear();
        for (Map.Entry<String, User> entry : dataStore.getUsers().entrySet()) {
            userList.add(entry.getValue());
        }
    }

    @FXML
    public void handleAddUser(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String role = cmbRole.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all user fields.");
            return;
        }

        if (dataStore.getUsers().containsKey(username)) {
            showAlert("Error", "Username already exists.");
            return;
        }

        // Factory Pattern
        User newUser = UserFactory.createUser(role, "U" + System.currentTimeMillis(), username, password);
        dataStore.getUsers().put(username, newUser);

        refreshUserList();
        txtUsername.clear();
        txtPassword.clear();
        showAlert("Success", "User created successfully.");
    }

    @FXML
    public void handleDeleteUser(ActionEvent event) {
        User selected = tableUsers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a user to delete.");
            return;
        }
        if ("admin".equals(selected.getUsername())) {
            showAlert("Error", "Cannot delete the master admin.");
            return;
        }
        dataStore.getUsers().remove(selected.getUsername());
        refreshUserList();
    }

    @FXML
    public void handleAddSupplier(ActionEvent event) {
        String name = txtSupplierName.getText();
        String contact = txtSupplierContact.getText();

        if (name.isEmpty() || contact.isEmpty()) {
            showAlert("Error", "Please fill in all supplier fields.");
            return;
        }

        Supplier supplier = new Supplier("SUP-" + System.currentTimeMillis(), name, "N/A", contact, "N/A");
        dataStore.getSuppliers().add(supplier);

        txtSupplierName.clear();
        txtSupplierContact.clear();
        showAlert("Success", "Supplier added.");
    }

    @FXML
    public void handleDeleteSupplier(ActionEvent event) {
        Supplier selected = tableSuppliers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a supplier to delete.");
            return;
        }
        dataStore.getSuppliers().remove(selected);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
