package com.pharmacy.service;

import com.pharmacy.model.Customer;
import com.pharmacy.model.Invoice;
import com.pharmacy.model.Medicine;
import com.pharmacy.model.Supplier;
import com.pharmacy.model.User;
import com.pharmacy.patterns.UserFactory;
import com.pharmacy.model.CustomerRequest;
import com.pharmacy.model.StockAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// Singleton Pattern
public class DataStore {
    private static DataStore instance;

    // In-memory collections
    private Map<String, User> users;
    private ObservableList<Medicine> medicines;
    private ObservableList<Customer> customers;
    private ObservableList<Supplier> suppliers;
    private ObservableList<CustomerRequest> requests;
    private ObservableList<Invoice> invoices;
    private ObservableList<StockAlert> stockAlerts;

    private DataStore() {
        users = new HashMap<>();
        medicines = FXCollections.observableArrayList();
        customers = FXCollections.observableArrayList();
        suppliers = FXCollections.observableArrayList();
        requests = FXCollections.observableArrayList();
        invoices = FXCollections.observableArrayList();
        stockAlerts = FXCollections.observableArrayList();
        
        loadSampleData();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void loadSampleData() {
        // Sample Users (Factory pattern used here)
        users.put("admin", UserFactory.createUser("ADMIN", "U01", "admin", "admin123"));
        users.put("pharmacist", UserFactory.createUser("PHARMACIST", "U02", "pharmacist", "pharm123"));
        users.put("customer", UserFactory.createUser("CUSTOMER", "U03", "customer", "cust123"));

        // Sample Suppliers
        suppliers.add(new Supplier("S01", "MediCorp Inc.", "John Doe", "555-0101", "123 Health Ave"));
        suppliers.add(new Supplier("S02", "PharmaGlobal", "Jane Smith", "555-0202", "456 Wellness Blvd"));

        // Sample Medicines
        medicines.add(new Medicine("M01", "Paracetamol 500mg", "Painkillers", 5.50, 150, LocalDate.now().plusYears(1), "S01", "Relieves pain and fever"));
        medicines.add(new Medicine("M02", "Amoxicillin 250mg", "Antibiotics", 12.00, 50, LocalDate.now().plusMonths(6), "S02", "Treats bacterial infections"));
        medicines.add(new Medicine("M03", "Vitamin C 1000mg", "Vitamins", 8.75, 5, LocalDate.now().plusYears(2), "S01", "Boosts immune system")); // Low stock
        medicines.add(new Medicine("M04", "Ibuprofen 400mg", "Painkillers", 6.20, 200, LocalDate.now().minusDays(5), "S02", "Anti-inflammatory pain relief")); // Expired
        medicines.add(new Medicine("M05", "Cough Syrup", "Cold & Flu", 15.00, 0, LocalDate.now().plusMonths(8), "S01", "Relieves dry cough")); // Out of stock

        // Sample Customers
        customers.add(new Customer("C01", "Walk-in Customer", "-", "-"));
        customers.add(new Customer("C02", "Alice Johnson", "555-1111", "alice@example.com"));
        customers.add(new Customer("C03", "Bob Williams", "555-2222", "bob@example.com"));
        customers.add(new Customer("C04", "Demo Customer", "555-3333", "customer@example.com"));
    }

    public Map<String, User> getUsers() { return users; }
    public ObservableList<Medicine> getMedicines() { return medicines; }
    public ObservableList<Customer> getCustomers() { return customers; }
    public ObservableList<Supplier> getSuppliers() { return suppliers; }
    public ObservableList<CustomerRequest> getRequests() { return requests; }
    public ObservableList<Invoice> getInvoices() { return invoices; }
    public ObservableList<StockAlert> getStockAlerts() { return stockAlerts; }
}
