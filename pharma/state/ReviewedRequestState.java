package com.pharmacy.patterns.state;

import com.pharmacy.model.CustomerRequest;

public class ReviewedRequestState implements RequestState {
    @Override
    public void handleState(CustomerRequest request) {
        // Logic for entering reviewed state
    }

    @Override
    public String getStatusString() {
        return "Reviewed";
    }
}
