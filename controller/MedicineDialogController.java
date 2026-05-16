package com.pharmacy.controller;

import com.pharmacy.model.Medicine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class MedicineDialogController {

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private ComboBox<String> cbCategory;
    @FXML private TextField txtPrice;
    @FXML private TextField txtQuantity;
    @FXML private DatePicker dpExpiryDate;
    @FXML private TextField txtSupplier;
    @FXML private TextArea txtDescription;
    
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    
    private Medicine medicine;
    private boolean saveClicked = false;
    private Stage dialogStage;
    private boolean isEditMode = false;

    @FXML
    private void initialize() {
        cbCategory.getItems().addAll("Painkillers", "Antibiotics", "Vitamins", "Cold & Flu", "First Aid", "Supplements", "Other");
        cbCategory.getSelectionModel().selectFirst();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
        if (medicine != null) {
            isEditMode = true;
            btnSave.setText("Update Product");
            txtId.setText(medicine.getId());
            txtId.setDisable(true);
            txtName.setText(medicine.getName());
            cbCategory.setValue(medicine.getCategory());
            txtPrice.setText(String.valueOf(medicine.getPrice()));
            txtQuantity.setText(String.valueOf(medicine.getQuantity()));
            dpExpiryDate.setValue(medicine.getExpiryDate());
            txtSupplier.setText(medicine.getSupplierId());
            txtDescription.setText(medicine.getDescription());
        } else {
            isEditMode = false;
            btnSave.setText("Add New Medicine");
            txtId.setText("M" + System.currentTimeMillis()); // Generate ID
            txtId.setDisable(true);
            dpExpiryDate.setValue(LocalDate.now().plusYears(1));
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (isInputValid()) {
            if (!isEditMode) {
                medicine = new Medicine(
                    txtId.getText(),
                    txtName.getText(),
                    cbCategory.getValue(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQuantity.getText()),
                    dpExpiryDate.getValue(),
                    txtSupplier.getText(),
                    txtDescription.getText()
                );
            } else {
                // Update existing medicine
                medicine.setName(txtName.getText());
                medicine.setCategory(cbCategory.getValue());
                medicine.setPrice(Double.parseDouble(txtPrice.getText()));
                medicine.setQuantity(Integer.parseInt(txtQuantity.getText()));
                medicine.setExpiryDate(dpExpiryDate.getValue());
                medicine.setSupplierId(txtSupplier.getText());
                medicine.setDescription(txtDescription.getText());
                medicine.updateState(); // Re-evaluate state
            }
            
            saveClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (txtName.getText() == null || txtName.getText().trim().isEmpty()) {
            errorMessage += "No valid name!\n";
        }
        if (txtPrice.getText() == null || txtPrice.getText().trim().isEmpty()) {
            errorMessage += "No valid price!\n";
        } else {
            try {
                Double.parseDouble(txtPrice.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Price must be a valid number!\n";
            }
        }
        if (txtQuantity.getText() == null || txtQuantity.getText().trim().isEmpty()) {
            errorMessage += "No valid quantity!\n";
        } else {
            try {
                Integer.parseInt(txtQuantity.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Quantity must be a valid integer!\n";
            }
        }
        if (dpExpiryDate.getValue() == null) {
            errorMessage += "No valid expiry date!\n";
        }
        if (txtSupplier.getText() == null || txtSupplier.getText().trim().isEmpty()) {
            errorMessage += "No valid supplier!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
