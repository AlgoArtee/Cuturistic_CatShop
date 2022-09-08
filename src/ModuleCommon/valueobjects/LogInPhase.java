package ModuleCommon.valueobjects;

/**
 * Enumeration of the different user login stages
 *
 * @author Chachulski, Mathea
 */
public enum LogInPhase {
    LOGGED_IN("Logged In"),
    LOGGED_OUT("Logged Out");

    private final String description;

    LogInPhase(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return this.description;
    }
}
