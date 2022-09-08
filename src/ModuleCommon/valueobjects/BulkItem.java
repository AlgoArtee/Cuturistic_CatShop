package ModuleCommon.valueobjects;

/**
 * Blueprint for bulkItems.
 *
 * @author Chachulski, Mathea
 */
public class BulkItem extends Item {

    public BulkItem(String itemName, int itemNumber, String pic,boolean isAvailable, double price,
                    String description, int inStock, int bulkSize) {
        super(itemName, itemNumber, pic, isAvailable, price, description, inStock);
        this.bulkSize = bulkSize;
    }
}
