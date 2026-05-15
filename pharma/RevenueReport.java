package com.pharmacy.patterns;

import com.pharmacy.model.InvoiceItem;
import com.pharmacy.service.DataStore;

public class RevenueReport extends ReportTemplate {
    @Override
    protected String generateHeader() {
        return "=== REVENUE REPORT ===\n";
    }

    @Override
    protected String generateBody() {
        StringBuilder sb = new StringBuilder();
        double totalRevenue = 0;
        int totalItemsSold = 0;
        
        DataStore ds = DataStore.getInstance();
        if (ds.getInvoices() != null) {
            for (var inv : ds.getInvoices()) {
                totalRevenue += inv.getTotal();
                for (InvoiceItem item : inv.getItems()) {
                    totalItemsSold += item.getQuantity();
                }
            }
        }
        
        sb.append("Total Revenue: $").append(String.format("%.2f", totalRevenue)).append("\n");
        sb.append("Total Items Sold: ").append(totalItemsSold).append("\n");
        return sb.toString();
    }
}
