package ModuleCommon.valueobjects;

/**
 * Enumeration of different types of webshop users.
 *
 * @author Chachulski, Mathea
 */
public enum UserType {
    EMPLOYEE("employee"),
    CUSTOMER("customer");

    private final String description;

    UserType(String description){

        this.description = description;
    }

    @Override
    public String toString(){
        return this.description;
    }
}
