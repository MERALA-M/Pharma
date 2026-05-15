package com.pharmacy.patterns;

// Template Method Pattern
public abstract class ReportTemplate {
    
    public final String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(generateHeader());
        sb.append("\n--------------------------------------------------\n");
        sb.append(generateBody());
        sb.append("\n--------------------------------------------------\n");
        sb.append(generateFooter());
        return sb.toString();
    }

    protected String generateHeader() {
        return "Pharmacy Management System - Official Report";
    }

    protected abstract String generateBody();

    protected String generateFooter() {
        return "End of Report - Generated Automatically";
    }
}
