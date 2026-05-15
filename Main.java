1. PATTERN: Singleton
CATEGORY: Creational
FILES:

src/main/java/com/pharmacy/service/DataStore.java
CODE:

java
public class DataStore {
    private static DataStore instance;
    private DataStore() { /* Load sample data */ }
    public static synchronized DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }
}
TRIGGER:

Initialized at startup and accessed via DataStore.getInstance() in every controller.
UML RELATIONSHIPS:
DataStore has a static self-reference (Self-Association).
2. PATTERN: Factory Method
CATEGORY: Creational
FILES:

src/main/java/com/pharmacy/patterns/UserFactory.java
src/main/java/com/pharmacy/model/User.java
CODE:

java
public class UserFactory {
    public static User createUser(String role, String username, String email, String password) {
        if (role.equalsIgnoreCase("ADMIN")) return new AdminUser(username, email, password);
        if (role.equalsIgnoreCase("PHARMACIST")) return new PharmacistUser(username, email, password);
        if (role.equalsIgnoreCase("CUSTOMER")) return new CustomerUser(username, email, password);
        return null;
    }
}
TRIGGER:

DataStore.loadSampleData() uses the factory to create role-specific user objects.
UML RELATIONSHIPS:
AdminUser, PharmacistUser, CustomerUser implement User.
UserFactory creates User (Dependency).
3. PATTERN: Abstract Factory
CATEGORY: Creational
FILES:

src/main/java/com/pharmacy/patterns/ui/UIFactory.java
src/main/java/com/pharmacy/patterns/ui/LuxuryUIFactory.java
CODE:

java
public interface UIFactory {
    Button createPrimaryButton(String text);
    Label createLabel(String text, String styleClass);
}
public class LuxuryUIFactory implements UIFactory {
    @Override
    public Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }
}
TRIGGER:

DashboardController.initialize() creates luxury-themed UI components.
UML RELATIONSHIPS:
LuxuryUIFactory implements UIFactory.
DashboardController uses UIFactory.