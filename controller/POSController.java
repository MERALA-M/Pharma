package com.pharmacy.controller;

import com.pharmacy.model.Customer;
import com.pharmacy.model.Invoice;
import com.pharmacy.model.InvoiceItem;
import com.pharmacy.model.Medicine;
import com.pharmacy.patterns.*;
import com.pharmacy.patterns.decorator.*;
import com.pharmacy.service.DataStore;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class POSController implements Initializable {

    @FXML private TextField txtSearchMed;
    @FXML private TableView<Medicine> tableMedicines;
    @FXML private TableColumn<Medicine, String> colMedName;
    @FXML private TableColumn<Medicine, Double> colMedPrice;
    @FXML private TableColumn<Medicine, Integer> colMedQty;
    @FXML private TextField txtAddQty;

    @FXML private TableView<InvoiceItem> tableCart;
    @FXML private TableColumn<InvoiceItem, String> colCartName;
    @FXML private TableColumn<InvoiceItem, Integer> colCartQty;
    @FXML private TableColumn<InvoiceItem, Double> colCartTotal;

    @FXML private ComboBox<Customer> cmbCustomer;
    @FXML private ComboBox<String> cmbPayment;
    @FXML private Label lblSubtotal;
    @FXML private Label lblTax;
    @FXML private Label lblTotal;

    private ObservableList<InvoiceItem> posCart;
    private DataStore dataStore;
    private FilteredList<Medicine> filteredMedicines;
    private PharmacyFacade pharmacyFacade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("[POS] Initializing Controller...");
        
        dataStore = DataStore.getInstance();
        pharmacyFacade = new PharmacyFacade();
        posCart = FXCollections.observableArrayList();
        
        setupMedicinesTable();
        setupCartTable();
        
        // Setup Customer ComboBox with Walk-in Customer support
        cmbCustomer.setItems(dataStore.getCustomers());
        cmbCustomer.setConverter(new javafx.util.StringConverter<Customer>() {
            @Override public String toString(Customer c) { return c == null ? "" : c.getName(); }
            @Override public Customer fromString(String s) { return null; }
        });
        
        // Find and select Walk-in Customer by default
        Customer walkIn = dataStore.getCustomers().stream()
                .filter(c -> c.getName().equalsIgnoreCase("Walk-in Customer"))
                .findFirst()
                .orElse(null);
        if (walkIn != null) {
            cmbCustomer.getSelectionModel().select(walkIn);
        } else if (!dataStore.getCustomers().isEmpty()) {
            cmbCustomer.getSelectionModel().selectFirst();
        }
        
        cmbPayment.setItems(FXCollections.observableArrayList("Cash", "Card", "Insurance"));
        cmbPayment.getSelectionModel().selectFirst();
        
        setupSearch();
        updateTotals();
    }

    private void setupMedicinesTable() {
        colMedName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colMedPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        colMedQty.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        
        filteredMedicines = new FilteredList<>(dataStore.getMedicines(), p -> true);
        tableMedicines.setItems(filteredMedicines);
    }

    private void setupCartTable() {
        colCartName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedicineName()));
        colCartQty.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        colCartTotal.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());
        
        tableCart.setItems(posCart);
    }

    private void setupSearch() {
        txtSearchMed.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredMedicines.setPredicate(medicine -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return medicine.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    @FXML
    public void handleAddToCart(ActionEvent event) {
        System.out.println("[POS] Add to cart clicked");
        Medicine selected = tableMedicines.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Required", "Please select a medicine from the inventory.");
            return;
        }

        int qtyToAdd = 1;
        try {
            if (!txtAddQty.getText().isEmpty()) {
                qtyToAdd = Integer.parseInt(txtAddQty.getText());
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid quantity.");
            return;
        }

        if (qtyToAdd <= 0) {
            showAlert("Input Error", "Quantity must be greater than zero.");
            return;
        }

        if (selected.getQuantity() < qtyToAdd) {
            showAlert("Insufficient stock", "Only " + selected.getQuantity() + " units available.");
            return;
        }

        boolean found = false;
        for (InvoiceItem item : posCart) {
            if (item.getMedicine().getId().equals(selected.getId())) {
                if (item.getQuantity() + qtyToAdd <= selected.getQuantity()) {
                    item.setQuantity(item.getQuantity() + qtyToAdd);
                    found = true;
                } else {
                    showAlert("Insufficient stock", "Cannot add more. Stock limit reached.");
                    return;
                }
                break;
            }
        }
        
        if (!found) {
            posCart.add(new InvoiceItem(selected, qtyToAdd, selected.getPrice()));
        }
        
        System.out.println("[POS] Added " + qtyToAdd + "x " + selected.getName() + " to cart.");
        tableCart.refresh();
        updateTotals();
        txtAddQty.setText("1");
    }

    @FXML
    public void handleIncreaseQty(ActionEvent event) {
        System.out.println("[POS] Plus clicked");
        InvoiceItem selected = tableCart.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Required", "Select an item in the cart to increase quantity.");
            return;
        }

        if (selected.getQuantity() < selected.getMedicine().getQuantity()) {
            selected.setQuantity(selected.getQuantity() + 1);
            System.out.println("[POS] Quantity increased for " + selected.getMedicineName());
            tableCart.refresh();
            updateTotals();
        } else {
            showAlert("Insufficient stock", "No more stock available for " + selected.getMedicineName());
        }
    }

    @FXML
    public void handleDecreaseQty(ActionEvent event) {
        System.out.println("[POS] Minus clicked");
        InvoiceItem selected = tableCart.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Required", "Select an item in the cart to decrease quantity.");
            return;
        }

        if (selected.getQuantity() > 1) {
            selected.setQuantity(selected.getQuantity() - 1);
            System.out.println("[POS] Quantity decreased for " + selected.getMedicineName());
        } else {
            System.out.println("[POS] Removing " + selected.getMedicineName() + " from cart.");
            posCart.remove(selected);
        }
        tableCart.refresh();
        updateTotals();
    }

    @FXML
    public void handleRemoveFromCart(ActionEvent event) {
        System.out.println("[POS] Remove clicked");
        InvoiceItem selected = tableCart.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("[POS] Removing " + selected.getMedicineName() + " from cart.");
            posCart.remove(selected);
            updateTotals();
        } else {
            showAlert("Selection Required", "Select an item in the cart to remove.");
        }
    }

    private void updateTotals() {
        double subtotal = posCart.stream().mapToDouble(InvoiceItem::getTotal).sum();
        
        InvoicePricing pricing = new BaseInvoicePricing(subtotal);
        pricing = new TaxDecorator(pricing, 0.05);
        double total = pricing.calculateTotal();

        lblSubtotal.setText(String.format("%.2f", subtotal));
        lblTax.setText(String.format("%.2f", subtotal * 0.05));
        lblTotal.setText(String.format("%.2f", total));
    }

    @FXML
    public void handleCheckout(ActionEvent event) {
        System.out.println("[POS] Process Sale clicked");
        
        if (posCart.isEmpty()) {
            showAlert("Cart is empty", "Please add medicines to the cart first.");
            return;
        }

        Customer customer = cmbCustomer.getValue();
        if (customer == null) {
            showAlert("Please select a customer", "Choose a customer or 'Walk-in Customer'.");
            return;
        }

        String paymentMethod = cmbPayment.getValue();
        if (paymentMethod == null) {
            showAlert("Please select a payment method", "Select Cash, Card, or Insurance.");
            return;
        }

        System.out.println("[POS] customer = " + customer.getName());
        System.out.println("[POS] payment method = " + paymentMethod);
        System.out.println("[POS] cart size = " + posCart.size());

        PaymentStrategy strategy;
        if ("Insurance".equals(paymentMethod)) {
            strategy = new InsuranceAdapter(new ExternalInsuranceAPI(), "POS-POL-999");
        } else if ("Card".equals(paymentMethod)) {
            strategy = new CardPayment("4000-0000-0000-0000", "01/26");
        } else {
            System.out.println("[POS] selected payment method = Cash");
            System.out.println("[POS] using CashPayment");
            strategy = new CashPayment();
        }

        double subtotal = posCart.stream().mapToDouble(InvoiceItem::getTotal).sum();
        InvoiceBuilder builder = new InvoiceBuilder()
                .setId("POS-" + System.currentTimeMillis())
                .setCustomer(customer)
                .setItems(new java.util.ArrayList<>(posCart))
                .setPaymentMethod(paymentMethod)
                .setTax(subtotal * 0.05)
                .setTotal(subtotal * 1.05);

        Invoice invoice = pharmacyFacade.processCheckout(builder, strategy);

        if (invoice != null) {
            if ("Cash".equals(paymentMethod)) {
                System.out.println("[POS] Cash payment result = true");
            }
            System.out.println("[POS] invoice created = " + invoice.getId());
            System.out.println("[POS] invoice saved count = " + DataStore.getInstance().getInvoices().size());
            System.out.println("[POS] stock updated");
            
            showAlert("Success", "Sale completed successfully");
            posCart.clear();
            tableMedicines.refresh();
            updateTotals();
        } else {
            showAlert("Payment failed", "The sale could not be processed. Check payment details.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
