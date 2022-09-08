package ModuleCommon.Exceptions;

public class BulkSizeException extends Exception {
    public BulkSizeException() {
        super("-- The amount has to be a multiple of the unit size! --");
    }
}
