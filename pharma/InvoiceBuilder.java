package com.pharmacy.patterns;

import com.pharmacy.model.Invoice;
import com.pharmacy.model.InvoiceItem;
import com.pharmacy.model.Customer;
import com.pharmacy.model.User;
import java.util.List;

// Builder Pattern
public class InvoiceBuilder {
    private Invoice invoice;

    public InvoiceBuilder() {
        this.invoice = new Invoice();
    }

    public InvoiceBuilder setId(String id) {
        invoice.setId(id);
        return this;
    }

    public InvoiceBuilder setCustomer(Customer customer) {
        invoice.setCustomer(customer);
        return this;
    }

    public InvoiceBuilder setCashier(User cashier) {
        invoice.setCashier(cashier);
        return this;
    }

    public InvoiceBuilder setItems(List<InvoiceItem> items) {
        invoice.setItems(items);
        double subtotal = items.stream().mapToDouble(InvoiceItem::getTotal).sum();
        invoice.setSubtotal(subtotal);
        return this;
    }

    public InvoiceBuilder setPaymentMethod(String paymentMethod) {
        invoice.setPaymentMethod(paymentMethod);
        return this;
    }

    // Decorators will handle tax and discount
    public InvoiceBuilder applyDiscount(double discountPercent) {
        invoice.setDiscount(invoice.getSubtotal() * (discountPercent / 100));
        return this;
    }

    public InvoiceBuilder applyTax(double taxPercent) {
        double amountAfterTax = (invoice.getSubtotal() - invoice.getDiscount());
        invoice.setTax(amountAfterTax * (taxPercent / 100));
        return this;
    }
    
    public InvoiceBuilder setDiscount(double discount) {
        invoice.setDiscount(discount);
        return this;
    }

    public InvoiceBuilder setTax(double tax) {
        invoice.setTax(tax);
        return this;
    }
    
    public InvoiceBuilder setTotal(double total) {
        invoice.setTotal(total);
        return this;
    }

    public InvoiceBuilder calculateTotal() {
        invoice.setTotal(invoice.getSubtotal() - invoice.getDiscount() + invoice.getTax());
        return this;
    }

    public Invoice build() {
        return invoice;
    }
}
