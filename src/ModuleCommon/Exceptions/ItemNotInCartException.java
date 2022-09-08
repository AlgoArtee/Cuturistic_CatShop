package ModuleCommon.Exceptions;

public class ItemNotInCartException extends Exception {
    public ItemNotInCartException(){
        super("-- You don't have this item in your cart! --");
    }
}
