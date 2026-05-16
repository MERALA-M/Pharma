package com.pharmacy.controller;

import com.pharmacy.model.Medicine;
import com.pharmacy.model.StockAlert;
import com.pharmacy.service.DataStore;
import com.pharmacy.service.SessionManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    @FXML private TextField txtSearch;
    @FXML private TableView<Medicine> tableMedicines;
    @FXML private TableColumn<Medicine, String> colName;
    @FXML private TableColumn<Medicine, String> colCategory;
    @FXML private TableColumn<Medicine, Double> colPrice;
    @FXML private TableColumn<Medicine, Integer> colQuantity;
    @FXML private TableColumn<Medicine, String> colStatus;

    @FXML private ComboBox<String> cmbPriority;
    @FXML private TextField txtRequestMsg;

    private DataStore dataStore;
    private FilteredList<Medicine> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataStore = DataStore.getInstance();
        setupTable();
        setupSearch();
        
        if (cmbPriority != null) {
            cmbPriority.setItems(FXCollections.observableArrayList("Low", "Medium", "High"));
            cmbPriority.getSelectionModel().select(1); // Medium default
        }
    }

    private void setupTable() {
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colCategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        colPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        colQuantity.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        filteredData = new FilteredList<>(dataStore.getMedicines(), p -> true);
        SortedList<Medicine> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableMedicines.comparatorProperty());
        tableMedicines.setItems(sortedData);
    }

    private void setupSearch() {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(medicine -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return medicine.getName().toLowerCase().contains(lowerCaseFilter) ||
                       medicine.getCategory().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    @FXML
    public void handleUpdateStock(ActionEvent event) {
        Medicine selected = tableMedicines.getSelectionModel().getSelectedItem();
        if (selected != null) {
            TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getQuantity()));
            dialog.setTitle("Update Stock");
            dialog.setHeaderText("Update Quantity for " + selected.getName());
            dialog.setContentText("Please enter new quantity:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                try {
                    int newQty = Integer.parseInt(result.get());
                    if (newQty < 0) throw new NumberFormatException();
                    
                    selected.setQuantity(newQty);
                    tableMedicines.refresh();
                    
                    // Trigger observer pattern if low stock
                    com.pharmacy.patterns.InventorySubject.getInstance().checkMedicine(selected);
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR, "Please enter a valid positive number.").show();
                }
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a medicine from the table.").show();
        }
    }

    @FXML
    public void handleSendRequest(ActionEvent event) {
        Medicine selected = tableMedicines.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a medicine first.").show();
            return;
        }
        
        String msg = txtRequestMsg.getText();
        if (msg == null || msg.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a message for the Admin.").show();
            return;
        }
        
        String priority = cmbPriority.getValue();
        String pharmacist = SessionManager.getInstance().getCurrentUser().getUsername();
        
        StockAlert alert = new StockAlert(selected, pharmacist, priority, msg);
        dataStore.getStockAlerts().add(alert);
        
        new Alert(Alert.AlertType.INFORMATION, "Stock request sent to Admin successfully!").show();
        txtRequestMsg.clear();
    }

    @FXML
    public void handleShowHierarchy(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HierarchyDialog.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Medicine Category Hierarchy");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            if (tableMedicines.getScene() != null) {
                dialogStage.initOwner(tableMedicines.getScene().getWindow());
            }
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            HierarchyDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
