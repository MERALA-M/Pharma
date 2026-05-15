package com.pharmacy.patterns.command;

// Command Pattern - Command interface
public interface MedicineCommand {
    void execute();
    void undo();
}
