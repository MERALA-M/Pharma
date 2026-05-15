package com.pharmacy.patterns.command;

import com.pharmacy.model.Medicine;
import com.pharmacy.service.DataStore;

// Command Pattern - Concrete Command
public class DeleteMedicineCommand implements MedicineCommand {
    private Medicine medicine;
    private DataStore dataStore;

    public DeleteMedicineCommand(Medicine medicine) {
        this.medicine = medicine;
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public void execute() {
        dataStore.getMedicines().remove(medicine);
    }

    @Override
    public void undo() {
        dataStore.getMedicines().add(medicine);
    }
}
