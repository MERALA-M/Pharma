package com.pharmacy.patterns.command;

import java.util.Stack;

// Command Pattern - Invoker
public class CommandInvoker {
    private Stack<MedicineCommand> commandHistory = new Stack<>();

    public void executeCommand(MedicineCommand command) {
        command.execute();
        commandHistory.push(command);
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            MedicineCommand command = commandHistory.pop();
            command.undo();
        }
    }
    
    public boolean canUndo() {
        return !commandHistory.isEmpty();
    }
}
