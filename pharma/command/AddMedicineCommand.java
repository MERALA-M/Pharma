package com.pharmacy.patterns.command;

import com.pharmacy.model.Medicine;
import com.pharmacy.service.DataStore;

// Command Pattern - Concrete Command
public class AddMedicineCommand implements MedicineCommand {
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
