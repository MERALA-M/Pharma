package com.pharmacy.controller;

import com.pharmacy.model.Medicine;
import com.pharmacy.service.DataStore;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HierarchyDialogController implements Initializable {

    @FXML private TreeView<String> treeHierarchy;
    
    private Stage dialogStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateTree();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void populateTree() {
        DataStore dataStore = DataStore.getInstance();
        TreeItem<String> rootItem = new TreeItem<>("Medicines Catalog");
        rootItem.setExpanded(true);

        Map<String, TreeItem<String>> categoryNodes = new HashMap<>();

        for (Medicine m : dataStore.getMedicines()) {
            TreeItem<String> catNode = categoryNodes.computeIfAbsent(m.getCategory(), cat -> {
                TreeItem<String> node = new TreeItem<>(cat);
                node.setExpanded(true);
                rootItem.getChildren().add(node);
                return node;
            });
            
            String statusIcon = m.getQuantity() < 10 ? " ⚠️" : "";
            catNode.getChildren().add(new TreeItem<>(m.getName() + " - " + m.getStatus() + statusIcon));
        }

        // Add counts to category names
        for (TreeItem<String> catNode : rootItem.getChildren()) {
            int count = catNode.getChildren().size();
            catNode.setValue(catNode.getValue() + " (" + count + ")");
        }

        treeHierarchy.setRoot(rootItem);
    }

    @FXML
    private void handleClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}
