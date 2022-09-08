package ModuleCommon.valueobjects;

import ModuleCommon.Exceptions.NotEnoughItemInStockException;

import java.io.Serializable;

/**
 * Blueprint for a webshop item.
 *
 * @author Chachulski, Mathea
 */

public class Item implements Serializable {
    private String itemName = "";
    private int itemNumber = 0;
    private String pic = "";
    private boolean isAvailable = false;
    private double price = 0.00;
    private String description = "";
    private volatile int amountInStock = 0;
    protected int bulkSize = 0;


    public Item(String itemName, int itemNumber, String pic, boolean isAvailable, double price, String description, int inStock) {
        this.itemName = itemName;
        this.itemNumber = itemNumber;
        this.pic = pic;
        this.isAvailable = isAvailable;
        this.price = price;
        this.description = description;
        this.amountInStock = inStock;
    }

    public String getItemName(){
        return this.itemName;
    }

    public int getItemNumber() {
        return this.itemNumber;
    }

    public String getPic(){return this.pic;}

    public int getAmountInStock() {
        return this.amountInStock;
    }

    public boolean getIsAvailable(){
        return this.isAvailable;
    }

    public String getDescription() {
        return this.description;
    }

    public double getItemPrice(){
        return this.price;
    }


    public int getInStock() {
        return this.amountInStock;
    }

    public void increaseStock(int increaseBy){
        this.amountInStock += increaseBy;
    }

    public void decreaseStock(int decreaseBy) throws NotEnoughItemInStockException {
        if (this.amountInStock >= decreaseBy) {
            this.amountInStock -= decreaseBy;
            if (this.amountInStock == 0) {
                this.isAvailable = false;
            }
        } else throw new NotEnoughItemInStockException();
    }

    public int getBulkSize(){
        return this.bulkSize;
    }

   @Override
    public String toString(){
        return this.itemName + " - price per item: " + this.price;
    }
}