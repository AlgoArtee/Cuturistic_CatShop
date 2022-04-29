package valueobjects;

/**
 * @author La Cha
 *
 *
 */

public class Item {

    private String ItemName = "";
    private int ItemNumber;


    public Item(String ItemName, int ItemNumber)
    {
        this.ItemName=ItemName;
        this.ItemNumber=ItemNumber;


    }

    public String getItemName() {
        return ItemName;
    }

    public int getItemNumber() {
        return ItemNumber;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public void setItemNumber(int itemNumber) {
        ItemNumber = itemNumber;
    }
}
