package ModuleCommon.Exceptions;

public class UserAlreadyExistsException extends Exception {
    private String invalidName;
    public UserAlreadyExistsException(String name){
        super("-- This user already exists! --");
        this.invalidName = name;
    }
}
