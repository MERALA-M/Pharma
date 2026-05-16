package com.pharmacy.controller;

import com.pharmacy.App;
import com.pharmacy.model.User;
import com.pharmacy.service.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    
    @FXML private Circle avatarCircle;
    @FXML private FontAwesomeIconView avatarIcon;
    @FXML private Label lblName;
    @FXML private Label lblUsername;
    @FXML private Label lblRole;
    @FXML private Label lblEmail;
    @FXML private Label lblPhone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            lblName.setText(user.getUsername()); // Or full name if stored
            lblUsername.setText("@" + user.getUsername());
            lblRole.setText(user.getRole().toUpperCase());
            
            // Set distinct colors based on role
            if ("ADMIN".equals(user.getRole().toUpperCase())) {
                avatarCircle.setFill(Color.web("#8B5CF6")); // Purple
                avatarIcon.setGlyphName("SHIELD");
                lblRole.setStyle("-fx-background-color: #EDE9FE; -fx-text-fill: #8B5CF6; -fx-background-radius: 12; -fx-padding: 5 15;");
            } else if ("PHARMACIST".equals(user.getRole().toUpperCase())) {
                avatarCircle.setFill(Color.web("#06B6D4")); // Cyan/Teal
                avatarIcon.setGlyphName("USER_MD");
                lblRole.setStyle("-fx-background-color: #CFFAFE; -fx-text-fill: #0891B2; -fx-background-radius: 12; -fx-padding: 5 15;");
            } else {
                avatarCircle.setFill(Color.web("#10B981")); // Emerald
                avatarIcon.setGlyphName("USER");
                lblRole.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #059669; -fx-background-radius: 12; -fx-padding: 5 15;");
            }
            
            // Populate mock info based on user type if specific info missing
            lblEmail.setText(user.getUsername().toLowerCase() + "@example.com");
            lblPhone.setText("+1 (555) 123-4567");
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        SessionManager.getInstance().logout();
        App.setRoot("fxml/Login.fxml", "PharmaLux - Secure Login");
    }
}
