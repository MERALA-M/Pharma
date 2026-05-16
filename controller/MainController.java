package com.pharmacy.controller;

import com.pharmacy.App;
import com.pharmacy.model.User;
import com.pharmacy.service.SessionManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private StackPane contentArea;
    @FXML private VBox navMenu;
    @FXML private Label lblUserName;
    @FXML private Label lblRole;

    private List<Button> navButtons = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("[DEBUG] MAIN CONTROLLER INITIALIZED");
        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            lblUserName.setText(user.getUsername());
            lblRole.setText(user.getRole().toUpperCase());
        }
        buildNavigationMenu();
    }

    private void buildNavigationMenu() {
        navMenu.getChildren().clear();
        navButtons.clear();

        User user = SessionManager.getInstance().getCurrentUser();
        String role = user != null ? user.getRole().toUpperCase() : "GUEST";

        // Common to all
        navMenu.getChildren().add(createNavButton("Dashboard", "HOME", this::showDashboard));

        if ("CUSTOMER".equals(role)) {
            navMenu.getChildren().add(createNavButton("Storefront", "SHOPPING_BAG", this::showStorefront));
            navMenu.getChildren().add(createNavButton("Cart / Checkout", "SHOPPING_CART", this::openCart));
            navMenu.getChildren().add(createNavButton("Favorites", "HEART", this::showFavorites));
            navMenu.getChildren().add(createNavButton("Orders History", "LIST_ALT", this::showOrders));
            navMenu.getChildren().add(createNavButton("Ask Pharmacist", "COMMENT", this::showPrescriptions));
            navMenu.getChildren().add(createNavButton("Profile", "USER", this::showProfile));
        } else if ("PHARMACIST".equals(role)) {
            navMenu.getChildren().add(createNavButton("Sell Point / POS", "DESKTOP", this::openPOS));
            navMenu.getChildren().add(createNavButton("Stock Update", "MEDKIT", this::showStockUpdate));
            navMenu.getChildren().add(createNavButton("Requests / Prescriptions", "FILE_TEXT", this::showPrescriptions));
            navMenu.getChildren().add(createNavButton("Inventory Warnings / My Requests", "BELL", this::showAlerts));
            navMenu.getChildren().add(createNavButton("Profile", "USER", this::showProfile));
        } else if ("ADMIN".equals(role)) {
            navMenu.getChildren().add(createNavButton("Medicine Management", "MEDKIT", this::showMedicine));
            navMenu.getChildren().add(createNavButton("Alerts", "BELL", this::showAlerts));
            navMenu.getChildren().add(createNavButton("Users & Suppliers", "COGS", this::showSettings));
            navMenu.getChildren().add(createNavButton("Reports", "BAR_CHART", this::showReports));
            navMenu.getChildren().add(createNavButton("Settings", "GEARS", this::showSettings));
            navMenu.getChildren().add(createNavButton("Profile", "USER", this::showProfile));
        }

        // Auto-select first screen
        if (!navButtons.isEmpty()) {
            navButtons.get(0).fire();
        }
    }

    private Button createNavButton(String text, String iconName,
                                   javafx.event.EventHandler<ActionEvent> action) {
        System.out.println("[DEBUG] CREATING NAV BUTTON: " + text);
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("sidebar-btn");

        try {
            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setGlyphName(iconName);
            icon.setSize("18");
            icon.getStyleClass().add("sidebar-icon");
            btn.setGraphic(icon);
        } catch (Exception e) {
            // Silently ignore icon errors
        }

        btn.setOnAction(e -> {
            System.out.println("[DEBUG] NAV BUTTON CLICKED: " + text);
            setActiveButton(btn);
            try {
                action.handle(e);
            } catch (Exception ex) {
                System.err.println("[MainController] Routing error for: " + text);
                ex.printStackTrace();
            }
        });

        navButtons.add(btn);
        return btn;
    }

    private void setActiveButton(Button activeButton) {
        for (Button btn : navButtons) {
            btn.getStyleClass().remove("active");
        }
        activeButton.getStyleClass().add("active");
    }

    // ─── Navigation Methods ─────────────────────────────────────────────────────

    private void showDashboard(ActionEvent event) {
        System.out.println("[DEBUG] Method: showDashboard -> FXML: Dashboard.fxml"); System.out.flush();
        loadScreen("Dashboard.fxml");
    }

    private void showMedicine(ActionEvent event) {
        System.out.println("[DEBUG] Method: showMedicine -> FXML: MedicineManagement.fxml"); System.out.flush();
        loadScreen("MedicineManagement.fxml");
    }

    private void showStorefront(ActionEvent event) {
        System.out.println("[DEBUG] Method: showStorefront -> FXML: Storefront.fxml"); System.out.flush();
        loadScreen("Storefront.fxml");
    }

    @FXML
    public void openCart(ActionEvent event) {
        System.out.println("[DEBUG] openCart -> Loading FXML view");
        loadScreen("Cart.fxml");
    }

    @FXML
    public void openPOS(ActionEvent event) {
        System.out.println("[DEBUG] openPOS -> Loading FXML view (Consolidated)");
        loadScreen("POS.fxml");
    }

    private void showStockUpdate(ActionEvent event) {
        System.out.println("[DEBUG] Method: showStockUpdate -> FXML: StockManagement.fxml"); System.out.flush();
        loadScreen("StockManagement.fxml");
    }

    private void showFavorites(ActionEvent event) {
        System.out.println("[DEBUG] Method: showFavorites -> FXML: Favorites.fxml"); System.out.flush();
        loadScreen("Favorites.fxml");
    }

    private void showOrders(ActionEvent event) {
        System.out.println("[DEBUG] Method: showOrders -> FXML: OrdersHistory.fxml"); System.out.flush();
        loadScreen("OrdersHistory.fxml");
    }

    private void showSettings(ActionEvent event) {
        System.out.println("[DEBUG] Method: showSettings -> FXML: AdminSettings.fxml"); System.out.flush();
        loadScreen("AdminSettings.fxml");
    }

    private void showPrescriptions(ActionEvent event) {
        System.out.println("[DEBUG] Method: showPrescriptions -> FXML: Requests.fxml"); System.out.flush();
        loadScreen("Requests.fxml");
    }

    private void showReports(ActionEvent event) {
        System.out.println("[DEBUG] Method: showReports -> FXML: Reports.fxml"); System.out.flush();
        loadScreen("Reports.fxml");
    }

    private void showAlerts(ActionEvent event) {
        System.out.println("[DEBUG] Method: showAlerts -> FXML: StockAlerts.fxml");
        loadScreen("StockAlerts.fxml");
    }

    @FXML
    public void showProfile(ActionEvent event) {
        System.out.println("[DEBUG] Method: showProfile -> FXML: Profile.fxml"); System.out.flush();
        loadScreen("Profile.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        SessionManager.getInstance().logout();
        App.setRoot("fxml/Login.fxml", "💊 Pharma - Secure Login");
    }

    // ─── Core FXML Loader ───────────────────────────────────────────────────────

    private void loadScreen(String fxmlFile) {
        String resourcePath = "/fxml/" + fxmlFile;
        try {
            URL resourceUrl = getClass().getResource(resourcePath);
            if (resourceUrl == null) {
                System.err.println("[loadScreen] FXML not found: " + resourcePath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Node node = loader.load();

            if (node == null) {
                System.err.println("[loadScreen] Loaded node is null for: " + fxmlFile);
                return;
            }

            node.setManaged(true);
            node.setVisible(true);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(node);
            
            System.out.println("[NAV] " + fxmlFile + " loaded successfully");


        } catch (IOException e) {
            System.out.println("[loadScreen] IOException loading " + fxmlFile);
            e.printStackTrace(System.out); 
            new Alert(Alert.AlertType.ERROR, "FXML Load Error: " + e.getMessage()).show();
        } catch (Exception e) {
            System.out.println("[loadScreen] General Exception loading " + fxmlFile);
            e.printStackTrace(System.out);
            new Alert(Alert.AlertType.ERROR, "System Error: " + e.getMessage()).show();
        }
    }
}
