package com.pharmacy.controller;

import com.pharmacy.patterns.InventoryReport;
import com.pharmacy.patterns.ReportTemplate;
import com.pharmacy.patterns.RevenueReport;
import com.pharmacy.patterns.SalesReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ReportsController {

    @FXML private TextArea txtReportOutput;

    @FXML
    public void handleInventoryReport(ActionEvent event) {
        ReportTemplate report = new InventoryReport();
        txtReportOutput.setText(report.generateReport());
    }

    @FXML
    public void handleSalesReport(ActionEvent event) {
        ReportTemplate report = new SalesReport();
        txtReportOutput.setText(report.generateReport());
    }

    @FXML
    public void handleRevenueReport(ActionEvent event) {
        ReportTemplate report = new RevenueReport();
        txtReportOutput.setText(report.generateReport());
    }
}
