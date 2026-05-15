package com.pharmacy.patterns;

import com.pharmacy.model.Customer;
import com.pharmacy.service.DataStore;

public class CustomerReport extends ReportTemplate {
    @Override
    protected String generateHeader() {
        return super.generateHeader() + "\nCUSTOMER DIRECTORY REPORT";
    }

    @Override
    protected String generateBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s | %-15s | %-20s\n", "Name", "Phone", "Email"));
        sb.append("--------------------------------------------------\n");
        for (Customer c : DataStore.getInstance().getCustomers()) {
            sb.append(String.format("%-20s | %-15s | %-20s\n", c.getName(), c.getPhone(), c.getEmail()));
        }
        return sb.toString();
    }
}
