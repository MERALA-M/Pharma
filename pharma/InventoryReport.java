package com.pharmacy.patterns;

import com.pharmacy.model.Medicine;
import com.pharmacy.service.DataStore;

public class InventoryReport extends ReportTemplate {
    @Override
    protected String generateHeader() {
        return super.generateHeader() + "\nINVENTORY STATUS REPORT";
    }

    @Override
    protected String generateBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s | %-10s | %-15s\n", "Medicine Name", "Quantity", "Status"));
        sb.append("--------------------------------------------------\n");
        for (Medicine m : DataStore.getInstance().getMedicines()) {
            sb.append(String.format("%-20s | %-10d | %-15s\n", m.getName(), m.getQuantity(), m.getStatus()));
        }
        return sb.toString();
    }
}
