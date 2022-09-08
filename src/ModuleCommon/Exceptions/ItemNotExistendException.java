package ModuleCommon.Exceptions;

public class ItemNotExistendException extends Exception {
    public ItemNotExistendException(){
        super("-- This item doesn't exist! --");
    }
}
