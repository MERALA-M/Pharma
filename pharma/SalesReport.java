package com.pharmacy.patterns;

import com.pharmacy.model.Invoice;
import com.pharmacy.service.DataStore;

public class SalesReport extends ReportTemplate {
    @Override
    protected String generateHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DETAILED SALES REPORT ===\n\n");
        sb.append(String.format("%-20s %-20s %-15s %-15s\n", "Invoice ID", "Date", "Items", "Total"));
        sb.append("----------------------------------------------------------------------\n");
        return sb.toString();
    }

    @Override
    protected String generateBody() {
        StringBuilder sb = new StringBuilder();
        DataStore ds = DataStore.getInstance();
        if (ds.getInvoices() != null && !ds.getInvoices().isEmpty()) {
            for (Invoice inv : ds.getInvoices()) {
                sb.append(String.format("%-20s %-20s %-15d $%-14.2f\n", 
                        inv.getId(), 
                        inv.getDateTime().toLocalDate().toString(), 
                        inv.getItems().size(), 
                        inv.getTotal()));
            }
        } else {
            sb.append("No sales recorded yet.\n");
        }
        return sb.toString();
    }
}
