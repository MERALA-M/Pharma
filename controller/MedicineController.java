package com.pharmacy.controller;

import com.pharmacy.model.Medicine;
import com.pharmacy.patterns.ScreenAccessProxy;
import com.pharmacy.patterns.command.AddMedicineCommand;
import com.pharmacy.patterns.command.CommandInvoker;
import com.pharmacy.patterns.command.DeleteMedicineCommand;
import com.pharmacy.patterns.command.MedicineCommand;
import com.pharmacy.service.DataStore;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Callback;
import java.io.IOException;
import java.util.Optional;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MedicineController implements Initializable {

    @FXML private TextField txtSearch;
    @FXML private TableView<Medicine> tableMedicines;
    @FXML private TableColumn<Medicine, String> colId;
    @FXML private TableColumn<Medicine, String> colName;
    @FXML private TableColumn<Medicine, String> colCategory;
    @FXML private TableColumn<Medicine, Double> colPrice;
    @FXML private TableColumn<Medicine, Integer> colQuantity;
    @FXML private TableColumn<Medicine, String> colExpiry;
    @FXML private TableColumn<Medicine, String> colStatus;

    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnUpdateStock;
    @FXML private Button btnDelete;
    @FXML private Button btnClone;
    @FXML private Button btnHierarchy;
    @FXML private Button btnUndo;
    @FXML private HBox actionBox;

    private DataStore dataStore;
    private FilteredList<Medicine> filteredData;
    private CommandInvoker commandInvoker = new CommandInvoker();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataStore = DataStore.getInstance();
        setupTable();
        setupSearch();
        applyRoleBasedAccess();
    }
    
    private void applyRoleBasedAccess() {
        boolean canAddEdit = ScreenAccessProxy.canAddEditMedicine();
        boolean canDelete = ScreenAccessProxy.canDeleteMedicine();
        boolean canUpdateStock = com.pharmacy.service.SessionManager.getInstance().getCurrentUser() != null && 
                                ("PHARMACIST".equals(com.pharmacy.service.SessionManager.getInstance().getCurrentUser().getRole().toUpperCase()) ||
                                 "ADMIN".equals(com.pharmacy.service.SessionManager.getInstance().getCurrentUser().getRole().toUpperCase()));
        
        btnAdd.setVisible(canAddEdit);
        btnAdd.setManaged(canAddEdit);
        
        btnEdit.setVisible(canAddEdit);
        btnEdit.setManaged(canAddEdit);

        btnUpdateStock.setVisible(canUpdateStock);
        btnUpdateStock.setManaged(canUpdateStock);
        
        btnClone.setVisible(canAddEdit);
        btnClone.setManaged(canAddEdit);

        btnHierarchy.setVisible(canAddEdit);
        btnHierarchy.setManaged(canAddEdit);
        
        btnDelete.setVisible(canDelete);
        btnDelete.setManaged(canDelete);
        
        if (btnUndo != null) {
            btnUndo.setVisible(canAddEdit || canDelete);
            btnUndo.setManaged(canAddEdit || canDelete);
            btnUndo.setDisable(!commandInvoker.canUndo());
        }
        
        if (!canAddEdit && !canDelete && !canUpdateStock) {
            actionBox.setVisible(false);
            actionBox.setManaged(false);
        }
    }

    private void setupTable() {
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colCategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        colPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        colQuantity.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        colExpiry.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getExpiryDate();
            return new SimpleStringProperty(date != null ? date.toString() : "");
        });
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        // Custom styling for Status column
        colStatus.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Medicine, String> call(TableColumn<Medicine, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item);
                            getStyleClass().removeAll("badge-available", "badge-low-stock", "badge-expired", "badge-out-of-stock");
                            if (item.equals("Available")) getStyleClass().add("badge-available");
                            else if (item.equals("Low Stock")) getStyleClass().add("badge-low-stock");
                            else if (item.equals("Expired")) getStyleClass().add("badge-expired");
                            else if (item.equals("Out of Stock")) getStyleClass().add("badge-out-of-stock");
                        }
                    }
                };
            }
        });

        filteredData = new FilteredList<>(dataStore.getMedicines(), p -> true);
        SortedList<Medicine> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableMedicines.comparatorProperty());
        tableMedicines.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
    public void handleAddMedicine(ActionEvent event) {
        if (!ScreenAccessProxy.canAddEditMedicine()) {
            showAlert("Access Denied", "Only Admins can add medicine.", Alert.AlertType.ERROR);
            return;
        }
        
        Medicine newMed = showMedicineDialog(null);
        if (newMed != null) {
            MedicineCommand addCommand = new AddMedicineCommand(newMed);
            commandInvoker.executeCommand(addCommand);
            
            if (btnUndo != null) btnUndo.setDisable(!commandInvoker.canUndo());
        }
    }

    @FXML
    public void handleEditMedicine(ActionEvent event) {
        Medicine selected = tableMedicines.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showMedicineDialog(selected);
            tableMedicines.refresh();
            com.pharmacy.patterns.InventorySubject.getInstance().checkMedicine(selected); // Check stock alerts
        } else {
            showAlert("No Selection", "Please select a medicine to edit.", Alert.AlertType.WARNING);
        }
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
                    com.pharmacy.patterns.InventorySubject.getInstance().checkMedicine(selected);
                    showAlert("Success", "Stock updated successfully.", Alert.AlertType.INFORMATION);
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Input", "Please enter a valid positive number.", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("No Selection", "Please select a medicine to update stock.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handleDeleteMedicine(ActionEvent event) {
        if (!ScreenAccessProxy.canDeleteMedicine()) {
            showAlert("Access Denied", "Only Admins can delete medicine.", Alert.AlertType.ERROR);
            return;
        }

        Medicine selected = tableMedicines.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Delete Medicine");
            confirm.setContentText("Are you sure you want to delete " + selected.getName() + "?");
            
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                MedicineCommand delCommand = new DeleteMedicineCommand(selected);
                commandInvoker.executeCommand(delCommand);
                if (btnUndo != null) btnUndo.setDisable(!commandInvoker.canUndo());
            }
        } else {
            showAlert("No Selection", "Please select a medicine to delete.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handleCloneMedicine(ActionEvent event) {
        Medicine selected = tableMedicines.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Prototype Pattern
            Medicine cloned = selected.clone();
            Medicine editedClone = showMedicineDialog(cloned);
            if (editedClone != null) {
                MedicineCommand addCommand = new AddMedicineCommand(editedClone);
                commandInvoker.executeCommand(addCommand);
                if (btnUndo != null) btnUndo.setDisable(!commandInvoker.canUndo());
            }
        } else {
            showAlert("No Selection", "Please select a medicine to clone (Prototype Pattern).", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handleShowComposite(ActionEvent event) {
        // Composite Pattern - Open Hierarchy Dialog
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

            // Also print to console for pattern proof
            System.out.println("--- Medicine Composite Hierarchy ---");
            com.pharmacy.patterns.composite.MedicineCategory rootCategory = new com.pharmacy.patterns.composite.MedicineCategory("Pharmacy Catalog");
            java.util.Map<String, com.pharmacy.patterns.composite.MedicineCategory> categories = new java.util.HashMap<>();
            for (Medicine m : dataStore.getMedicines()) {
                categories.computeIfAbsent(m.getCategory(), cat -> {
                    com.pharmacy.patterns.composite.MedicineCategory sub = new com.pharmacy.patterns.composite.MedicineCategory(cat);
                    rootCategory.add(sub);
                    return sub;
                }).add(m);
            }
            rootCategory.displayHierarchy("");

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the hierarchy dialog.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleUndo(ActionEvent event) {
        if (commandInvoker.canUndo()) {
            commandInvoker.undoLastCommand();
            if (btnUndo != null) btnUndo.setDisable(!commandInvoker.canUndo());
        }
    }

    private Medicine showMedicineDialog(Medicine medicine) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MedicineDialog.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(medicine == null ? "Add Medicine" : "Edit Medicine");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            // Needs to be assigned owner in a real app, but we can just use the scene
            if (tableMedicines.getScene() != null) {
                dialogStage.initOwner(tableMedicines.getScene().getWindow());
            }
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            MedicineDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMedicine(medicine);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                return controller.getMedicine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the medicine dialog.", Alert.AlertType.ERROR);
        }
        return null;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
