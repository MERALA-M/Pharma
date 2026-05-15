public interface MedicineState { String getStatusString(); boolean canAddToCart(); }
public class OutOfStockState implements MedicineState {
    public String getStatusString() { return "Out of Stock"; }
    public boolean canAddToCart() { return false; }
}
