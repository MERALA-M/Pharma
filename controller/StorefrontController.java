package com.pharmacy.controller;

import com.pharmacy.model.CustomerUser;
import com.pharmacy.model.InvoiceItem;
import com.pharmacy.model.Medicine;
import com.pharmacy.service.DataStore;
import com.pharmacy.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.net.URL;
import java.util.ResourceBundle;

public class StorefrontController implements Initializable {

    @FXML private TextField txtSearchMed;
    @FXML private FlowPane productsFlowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProductCards("");
        txtSearchMed.textProperty().addListener((obs, oldV, newV) -> loadProductCards(newV));
    }

    private void loadProductCards(String filter) {
        productsFlowPane.getChildren().clear();
        for (Medicine med : DataStore.getInstance().getMedicines()) {
            if (filter == null || filter.isEmpty() || med.getName().toLowerCase().contains(filter.toLowerCase()) || med.getCategory().toLowerCase().contains(filter.toLowerCase())) {
                productsFlowPane.getChildren().add(createProductCard(med));
            }
        }
    }

    private VBox createProductCard(Medicine medicine) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 15, 0, 0, 5);");
        card.setPrefWidth(220);
        card.setMaxWidth(220);

        HBox topRow = new HBox();
        Label iconLbl = new Label("💊");
        iconLbl.setStyle("-fx-font-size: 24px;");
        
        Button btnFav = new Button();
        btnFav.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        FontAwesomeIconView favIcon = new FontAwesomeIconView();
        favIcon.setGlyphName("HEART");
        
        CustomerUser customer = null;
        if (SessionManager.getInstance().getCurrentUser() instanceof CustomerUser) {
            customer = (CustomerUser) SessionManager.getInstance().getCurrentUser();
            if (customer.getFavoriteMedicines().contains(medicine.getId())) {
                favIcon.setFill(javafx.scene.paint.Color.web("#EF4444")); // Red heart
            } else {
                favIcon.setFill(javafx.scene.paint.Color.web("#CBD5E1")); // Gray heart
            }
        }
        btnFav.setGraphic(favIcon);
        
        final CustomerUser finalCustomer = customer;
        btnFav.setOnAction(e -> {
            if (finalCustomer != null) {
                if (finalCustomer.getFavoriteMedicines().contains(medicine.getId())) {
                    finalCustomer.removeFavorite(medicine.getId());
                    favIcon.setFill(javafx.scene.paint.Color.web("#CBD5E1"));
                } else {
                    finalCustomer.addFavorite(medicine.getId());
                    favIcon.setFill(javafx.scene.paint.Color.web("#EF4444"));
                }
            }
        });
        
        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        topRow.getChildren().addAll(iconLbl, spacer, btnFav);

        Label nameLbl = new Label(medicine.getName());
        nameLbl.setStyle("-fx-font-weight: 800; -fx-font-size: 16px; -fx-text-fill: #1E293B;");
        nameLbl.setWrapText(true);

        Label catLbl = new Label(medicine.getCategory());
        catLbl.setStyle("-fx-background-color: #E0E7FF; -fx-text-fill: #4F46E5; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 10;");

        Label priceLbl = new Label(String.format("$%.2f", medicine.getPrice()));
        priceLbl.setStyle("-fx-text-fill: #0F172A; -fx-font-weight: 900; -fx-font-size: 20px;");
        
        Label descLbl = new Label(medicine.getDescription() != null ? medicine.getDescription() : "High quality healthcare product.");
        descLbl.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11px;");
        descLbl.setWrapText(true);
        descLbl.setPrefHeight(35);

        Label statusLbl = new Label("Status: " + medicine.getStatus());
        statusLbl.setStyle(medicine.canAddToCart() ? "-fx-text-fill: #10B981; -fx-font-size: 12px; -fx-font-weight: bold;" : "-fx-text-fill: #EF4444; -fx-font-size: 12px; -fx-font-weight: bold;");

        Button btnAdd = new Button("Add to Cart");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 6;");
        btnAdd.setOnAction(e -> handleAddToCart(medicine));
        
        if (!medicine.canAddToCart() || medicine.getQuantity() <= 0) {
            btnAdd.setDisable(true);
            btnAdd.setText("Out of Stock");
        }

        card.getChildren().addAll(topRow, catLbl, nameLbl, priceLbl, descLbl, statusLbl, btnAdd);
        return card;
    }

    private void handleAddToCart(Medicine selected) {
        boolean found = false;
        for (InvoiceItem item : SessionManager.getInstance().getCart()) {
            if (item.getMedicine().getId().equals(selected.getId())) {
                if (item.getQuantity() < selected.getQuantity()) {
                    item.setQuantity(item.getQuantity() + 1);
                } else {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Not enough stock!");
                    a.show();
                    return;
                }
                found = true;
                break;
            }
        }
        if (!found) {
            SessionManager.getInstance().getCart().add(new InvoiceItem(selected, 1, selected.getPrice()));
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Added to cart successfully");
        alert.setHeaderText(null);
        alert.show();
    }
}
