package com.pharmacy.controller;

import com.pharmacy.model.Invoice;
import com.pharmacy.model.InvoiceItem;
import com.pharmacy.service.DataStore;
import com.pharmacy.service.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class OrdersHistoryController implements Initializable {

    @FXML private TableView<Invoice> tableOrders;
    @FXML private TableColumn<Invoice, String> colId;
    @FXML private TableColumn<Invoice, String> colDate;
    @FXML private TableColumn<Invoice, String> colItems;
    @FXML private TableColumn<Invoice, String> colPayment;
    @FXML private TableColumn<Invoice, String> colTotal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
    }

    private void setupTable() {
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getId()));
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDateTime().toString()));
        colPayment.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPaymentMethod()));
        colTotal.setCellValueFactory(cell -> new SimpleStringProperty(String.format("$%.2f", cell.getValue().getTotal())));
        
        colItems.setCellValueFactory(cell -> {
            StringBuilder itemsStr = new StringBuilder();
            for (InvoiceItem item : cell.getValue().getItems()) {
                itemsStr.append(item.getQuantity()).append("x ").append(item.getMedicineName()).append(", ");
            }
            return new SimpleStringProperty(itemsStr.toString().replaceAll(", $", ""));
        });

        String currentUsername = SessionManager.getInstance().getCurrentUser().getUsername();
        System.out.println("[HISTORY] current customer = " + currentUsername);
        
        FilteredList<Invoice> myOrders = new FilteredList<>(DataStore.getInstance().getInvoices(), inv -> 
            inv.getCustomer() != null && inv.getCustomer().getEmail().toLowerCase().startsWith(currentUsername.toLowerCase())
        );

        System.out.println("[HISTORY] loaded orders count = " + myOrders.size());
        tableOrders.setItems(myOrders);
    }
}
