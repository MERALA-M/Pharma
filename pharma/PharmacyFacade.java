package com.pharmacy.patterns;

import com.pharmacy.model.Invoice;
import com.pharmacy.model.InvoiceItem;
import com.pharmacy.model.Medicine;
import com.pharmacy.service.DataStore;

import java.util.List;

// Facade Pattern
public class PharmacyFacade {
    private InventorySubject inventorySubject;
    private DataStore dataStore;

    public PharmacyFacade() {
        this.inventorySubject = InventorySubject.getInstance();
        this.dataStore = DataStore.getInstance();
    }

    public Invoice processCheckout(InvoiceBuilder builder, PaymentStrategy paymentStrategy) {
        Invoice invoice = builder.build();
        System.out.println("[FACADE] Processing checkout for Invoice: " + invoice.getId());

        // 1. Process payment
        boolean paymentSuccess = paymentStrategy.pay(invoice.getTotal());
        if (!paymentSuccess) {
            System.err.println("[FACADE] Payment failed for amount: " + invoice.getTotal());
            return null;
        }

        // 2. Update inventory
        updateInventory(invoice.getItems());
        System.out.println("[FACADE] Inventory updated for " + invoice.getItems().size() + " items");

        // 3. Save invoice (In memory)
        if (dataStore.getInvoices() == null) {
            System.err.println("[FACADE] CRITICAL: DataStore invoices list is null!");
            return null;
        }
        
        dataStore.getInvoices().add(invoice);
        System.out.println("[FACADE] Invoice " + invoice.getId() + " saved successfully. Total invoices: " + dataStore.getInvoices().size());
        
        return invoice;
    }

    private void updateInventory(List<InvoiceItem> items) {
        for (InvoiceItem item : items) {
            Medicine med = item.getMedicine();
            int oldQty = med.getQuantity();
            int newQty = oldQty - item.getQuantity();
            med.setQuantity(newQty);
            System.out.println("[FACADE] Stock reduced for " + med.getName() + ": " + oldQty + " -> " + newQty);
            
            // Trigger observer pattern if low stock
            inventorySubject.checkMedicine(med);
        }
    }
}
