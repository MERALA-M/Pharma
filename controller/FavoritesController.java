package com.pharmacy.controller;

import com.pharmacy.model.CustomerUser;
import com.pharmacy.model.InvoiceItem;
import com.pharmacy.model.Medicine;
import com.pharmacy.service.DataStore;
import com.pharmacy.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class FavoritesController implements Initializable {

    @FXML private FlowPane favoritesFlowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFavorites();
    }

    private void loadFavorites() {
        favoritesFlowPane.getChildren().clear();
        if (!(SessionManager.getInstance().getCurrentUser() instanceof CustomerUser)) return;
        
        CustomerUser customer = (CustomerUser) SessionManager.getInstance().getCurrentUser();
        Set<String> favIds = customer.getFavoriteMedicines();

        if (favIds.isEmpty()) {
            Label emptyLbl = new Label("You have no favorite medicines yet.");
            emptyLbl.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 16px;");
            favoritesFlowPane.getChildren().add(emptyLbl);
            return;
        }

        for (Medicine med : DataStore.getInstance().getMedicines()) {
            if (favIds.contains(med.getId())) {
                favoritesFlowPane.getChildren().add(createProductCard(med, customer));
            }
        }
    }

    private VBox createProductCard(Medicine medicine, CustomerUser customer) {
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
        favIcon.setGlyphName("TRASH"); // In favorites view, removing is trash or solid heart
        favIcon.setFill(javafx.scene.paint.Color.web("#EF4444"));
        btnFav.setGraphic(favIcon);
        
        btnFav.setOnAction(e -> {
            customer.removeFavorite(medicine.getId());
            loadFavorites(); // refresh
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

        Button btnAdd = new Button("Add to Cart");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 6;");
        btnAdd.setOnAction(e -> {
            boolean found = false;
            for (InvoiceItem item : SessionManager.getInstance().getCart()) {
                if (item.getMedicine().getId().equals(medicine.getId())) {
                    if (item.getQuantity() < medicine.getQuantity()) {
                        item.setQuantity(item.getQuantity() + 1);
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Not enough stock!").show();
                        return;
                    }
                    found = true;
                    break;
                }
            }
            if (!found) SessionManager.getInstance().getCart().add(new InvoiceItem(medicine, 1, medicine.getPrice()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, medicine.getName() + " added to cart!");
            alert.setHeaderText(null);
            alert.show();
        });
        
        if (!medicine.canAddToCart() || medicine.getQuantity() <= 0) {
            btnAdd.setDisable(true);
            btnAdd.setText("Out of Stock");
        }

        card.getChildren().addAll(topRow, catLbl, nameLbl, priceLbl, btnAdd);
        return card;
    }
}
