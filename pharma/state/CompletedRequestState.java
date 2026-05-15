package com.pharmacy.patterns.state;

import com.pharmacy.model.CustomerRequest;

public class CompletedRequestState implements RequestState {
    @Override
    public void handleState(CustomerRequest request) {
        // Logic for entering completed state
    }

    @Override
    public String getStatusString() {
        return "Completed";
    }
}
