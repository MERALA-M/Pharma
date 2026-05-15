public interface MedicineCommand { void execute(); void undo(); }
public class AddMedicineCommand implements MedicineCommand {
    private Medicine medicine;
    public void execute() { DataStore.getInstance().getMedicines().add(medicine); }
}
public class CommandInvoker {
    public void executeCommand(MedicineCommand cmd) { cmd.execute(); /* store for undo */ }
}

