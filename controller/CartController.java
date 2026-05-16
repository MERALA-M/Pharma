package com.pharmacy.controller;

import com.pharmacy.model.Customer;
import com.pharmacy.model.Invoice;
import com.pharmacy.model.InvoiceItem;
import com.pharmacy.model.User;
import com.pharmacy.patterns.*;
import com.pharmacy.patterns.decorator.*;
import com.pharmacy.service.DataStore;
import com.pharmacy.service.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;
import java.net.URL;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    @FXML private VBox cartItemsContainer;
    @FXML private Label lblSubtotal;
    @FXML private Label lblTax;
    @FXML private Label lblDelivery;
    @FXML private Label lblDiscount;
    @FXML private Label lblTotal;
    @FXML private ComboBox<String> cmbPaymentMethod;
    @FXML private HBox discountRow;

    private ObservableList<InvoiceItem> cartItems;
    private PharmacyFacade pharmacyFacade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("[CLEAN-CART] Initializing Premium Cart Controller");
        
        pharmacyFacade = new PharmacyFacade();
        cartItems = SessionManager.getInstance().getCart();
        
        cmbPaymentMethod.setItems(FXCollections.observableArrayList("Cash on Delivery", "Card / Visa", "Insurance"));
        cmbPaymentMethod.getSelectionModel().selectFirst();
        
        refreshCartUI();
    }

    private void refreshCartUI() {
        cartItemsContainer.getChildren().clear();
        if (cartItems.isEmpty()) {
            Label emptyLbl = new Label("Your cart is empty.");
            emptyLbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-style: italic;");
            cartItemsContainer.getChildren().add(emptyLbl);
        } else {
            for (InvoiceItem item : cartItems) {
                cartItemsContainer.getChildren().add(createCartItemCard(item));
            }
        }
        updateSummary();
    }

    private HBox createCartItemCard(InvoiceItem item) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #e2e8f0; -fx-border-radius: 10;");
        
        VBox details = new VBox(5);
        Label nameLbl = new Label(item.getMedicineName());
        nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #1e293b;");
        Label priceLbl = new Label(String.format("$%.2f per unit", item.getUnitPrice()));
        priceLbl.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        details.getChildren().addAll(nameLbl, priceLbl);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        HBox qtyControls = new HBox(10);
        qtyControls.setAlignment(Pos.CENTER);
        Button btnMinus = new Button("-");
        btnMinus.setOnAction(e -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                refreshCartUI();
            }
        });
        Label qtyLbl = new Label(String.valueOf(item.getQuantity()));
        qtyLbl.setStyle("-fx-font-weight: bold; -fx-min-width: 20; -fx-alignment: center;");
        Button btnPlus = new Button("+");
        btnPlus.setOnAction(e -> {
            if (item.getQuantity() < item.getMedicine().getQuantity()) {
                item.setQuantity(item.getQuantity() + 1);
                refreshCartUI();
            }
        });
        qtyControls.getChildren().addAll(btnMinus, qtyLbl, btnPlus);
        
        Label totalLbl = new Label(String.format("$%.2f", item.getTotal()));
        totalLbl.setStyle("-fx-font-weight: 800; -fx-text-fill: #2563eb; -fx-min-width: 80; -fx-alignment: center-right;");
        
        Button btnRemove = new Button("✕");
        btnRemove.setStyle("-fx-background-color: transparent; -fx-text-fill: #ef4444; -fx-font-weight: bold; -fx-cursor: hand;");
        btnRemove.setOnAction(e -> {
            cartItems.remove(item);
            refreshCartUI();
        });
        
        card.getChildren().addAll(details, spacer, qtyControls, totalLbl, btnRemove);
        return card;
    }

    private void updateSummary() {
        double subtotal = cartItems.stream().mapToDouble(InvoiceItem::getTotal).sum();
        
        // Decorator Pattern
        InvoicePricing pricing = new BaseInvoicePricing(subtotal);
        pricing = new TaxDecorator(pricing, 0.05);
        if (subtotal > 0) pricing = new DeliveryFeeDecorator(pricing, 10.0);
        if (subtotal > 100) pricing = new DiscountDecorator(pricing, 0.1);
        
        double total = pricing.calculateTotal();

        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblTax.setText(String.format("$%.2f", subtotal * 0.05));
        lblDelivery.setText(subtotal > 0 ? "$10.00" : "$0.00");
        
        if (subtotal > 100) {
            discountRow.setVisible(true);
            discountRow.setManaged(true);
            lblDiscount.setText(String.format("-$%.2f", subtotal * 0.1));
        } else {
            discountRow.setVisible(false);
            discountRow.setManaged(false);
        }
        
        lblTotal.setText(String.format("$%.2f", total));
    }

    @FXML
    public void handleCheckout(ActionEvent event) {
        System.out.println("[CHECKOUT] Place Order Now clicked");
        if (cartItems.isEmpty()) {
            showAlert("Cart Empty", "Please add items before checking out.");
            return;
        }

        String selectedMethod = cmbPaymentMethod.getValue();
        PaymentStrategy strategy;
        if ("Insurance".equals(selectedMethod)) {
            strategy = new InsuranceAdapter(new ExternalInsuranceAPI(), "POL-12345");
        } else if ("Card / Visa".equals(selectedMethod)) {
            strategy = new CardPayment("1234-5678-9012-3456", "12/25");
        } else {
            strategy = new CashPayment();
        }

        User currentUser = SessionManager.getInstance().getCurrentUser();
        Customer currentCustomer = DataStore.getInstance().getCustomers().stream()
                .filter(c -> c.getEmail().toLowerCase().contains(currentUser.getUsername().toLowerCase()))
                .findFirst()
                .orElse(null);

        if (currentCustomer == null) {
            currentCustomer = new Customer(currentUser.getId(), currentUser.getUsername(), "N/A", currentUser.getUsername() + "@example.com");
        }

        double subtotal = cartItems.stream().mapToDouble(InvoiceItem::getTotal).sum();
        InvoicePricing pricing = new BaseInvoicePricing(subtotal);
        pricing = new TaxDecorator(pricing, 0.05);
        if (subtotal > 0) pricing = new DeliveryFeeDecorator(pricing, 10.0);
        if (subtotal > 100) pricing = new DiscountDecorator(pricing, 0.1);
        double total = pricing.calculateTotal();

        InvoiceBuilder builder = new InvoiceBuilder()
                .setId("INV-" + System.currentTimeMillis())
                .setCustomer(currentCustomer)
                .setItems(new java.util.ArrayList<>(cartItems))
                .setPaymentMethod(selectedMethod)
                .setTax(subtotal * 0.05)
                .setDiscount(subtotal > 100 ? subtotal * 0.1 : 0)
                .setTotal(total);

        Invoice result = pharmacyFacade.processCheckout(builder, strategy);

        if (result != null) {
            System.out.println("[CHECKOUT] Order saved for customer = " + currentCustomer.getName());
            
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Order placed successfully");
            successAlert.setHeaderText(null);
            successAlert.showAndWait();
            
            cartItems.clear();
            refreshCartUI();
        }
    }

    @FXML
    public void handleClearCart(ActionEvent event) {
        cartItems.clear();
        refreshCartUI();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
