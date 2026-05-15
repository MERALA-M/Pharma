package com.pharmacy.patterns.state;

import com.pharmacy.model.CustomerRequest;

public interface RequestState {
    void handleState(CustomerRequest request);
    String getStatusString();
}
