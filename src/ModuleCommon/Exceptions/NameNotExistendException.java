package ModuleCommon.Exceptions;

public class NameNotExistendException extends Exception {
    public NameNotExistendException(){
        super("-- No User found with this name! --");
    }
}
