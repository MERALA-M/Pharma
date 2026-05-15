package com.pharmacy.patterns;

import com.pharmacy.model.Medicine;
import com.pharmacy.service.DataStore;

// Command Pattern Implementation
public class AddMedicineCommand implements Command {
    private Medicine medicine;
    private DataStore dataStore;

    public AddMedicineCommand(Medicine medicine) {
        this.medicine = medicine;
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public void execute() {
        dataStore.getMedicines().add(medicine);
    }

    @Override
    public void undo() {
        dataStore.getMedicines().remove(medicine);
    }
}
