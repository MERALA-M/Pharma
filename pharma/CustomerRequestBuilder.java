package com.pharmacy.patterns;

import com.pharmacy.model.Customer;
import com.pharmacy.model.CustomerRequest;

// Builder Pattern
public class CustomerRequestBuilder {
    private CustomerRequest request;

    public CustomerRequestBuilder() {
        this.request = new CustomerRequest();
    }

    public CustomerRequestBuilder setId(String id) {
        request.setId(id);
        return this;
    }

    public CustomerRequestBuilder setCustomer(Customer customer) {
        request.setCustomer(customer);
        return this;
    }

    public CustomerRequestBuilder setMessage(String message) {
        request.setMessage(message);
        return this;
    }

    public CustomerRequest build() {
        return request;
    }
}
