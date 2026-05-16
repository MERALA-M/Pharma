package com.pharmacy.model;

import com.pharmacy.patterns.state.PendingRequestState;
import com.pharmacy.patterns.state.RequestState;

public class CustomerRequest {
    private String id;
    private Customer customer;
    private String message;
    private String reply;
    private RequestState state;

    public CustomerRequest() {
        this.state = new PendingRequestState(); // Initial state
        this.state.handleState(this);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }

    public RequestState getState() { return state; }
    public void setState(RequestState state) { 
        this.state = state; 
        this.state.handleState(this);
    }
    
    public String getStatus() {
        return state != null ? state.getStatusString() : "Unknown";
    }
}
