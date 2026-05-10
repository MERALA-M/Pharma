package com.pharmacy.patterns;

public class PharmacyFacade {

    public Invoice processCheckout(InvoiceBuilder builder, PaymentStrategy strategy) {

        Invoice invoice = builder.build();

        strategy.pay(invoice.getTotal());

        updateInventory(invoice.getItems());

        DataStore.getInstance().getInvoices().add(invoice);

        return invoice;
    }

    private void updateInventory(Object items) {

    }
}