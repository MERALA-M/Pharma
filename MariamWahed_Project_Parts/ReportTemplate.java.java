public abstract class ReportTemplate {
    public final void generateReport() {
        printHeader();
        collectData(); // abstract
        printFooter();
    }
    protected abstract void collectData();
}
public class SalesReport extends ReportTemplate {
    @Override
    protected void collectData() { /* fetch sales data */ }
}