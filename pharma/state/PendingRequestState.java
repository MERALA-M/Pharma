package com.pharmacy.patterns.state;

import com.pharmacy.model.CustomerRequest;

public class PendingRequestState implements RequestState {
    @Override
    public void handleState(CustomerRequest request) {
        // Logic for entering pending state
    }

    @Override
    public String getStatusString() {
        return "Pending";
    }
}
