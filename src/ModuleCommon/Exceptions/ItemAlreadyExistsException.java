package ModuleCommon.Exceptions;

public class ItemAlreadyExistsException extends Exception {
    public ItemAlreadyExistsException() {
        super("-- This item already exists! --");
    }

}
