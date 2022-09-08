package ModuleServer.domain;

import ModuleCommon.Exceptions.ItemAlreadyExistsException;
import ModuleCommon.valueobjects.BulkItem;
import ModuleCommon.valueobjects.Item;
import ModuleServer.persistence.FilePersistenceManager;
import ModuleServer.persistence.PersistenceManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class for the management of items inside an ItemList Object
 *
 * @author Chachulski, Mathea
 */
public class ItemManager implements Serializable {
    //private ItemList itemListObject = new ItemList();
    //private Map<Integer,Item> itemList = itemListObject.getItems();
    PersistenceManager persistenceManager = new FilePersistenceManager();
    // itemList: key = itemNumber, value= itemObject
    private volatile Map<Integer, Item> itemList = new HashMap<>();

    public ItemManager() throws IOException, ClassNotFoundException {

        //addFirstItems(this.itemList);

        // write Item List to File
        //try {
        //    persistenceManager.writeItemList(itemList);
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}

        // initialising itemManager with itemlist from file
        try {
            getItemListFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    private synchronized void addFirstItems(Map<Integer, Item> itemList) throws IOException {
        Item item1 = new Item("CAT Scan",1,"catscan_1.png",true,384.9, "No, not the medical procedure! Our cat scanning device will let you know in seconds if your cat has unmet needs, psychological or physiological issues.", 10);
        itemList.put(item1.getItemNumber(), item1);

        Item item2 = new Item("Robo-Cat",2,"robocat_2.jpg",false,5000.99, "Your cutest electronic companion! (Might attack other electronics in your household.)", 4);
        itemList.put(item2.getItemNumber(), item2);

        Item item3 = new Item("Catablet",3,"catablet_3.jpg",true,499.99, "Our AI algorithms can read your cat's wishes by paw vibration and movement on the touch screen. Upgrade your cat-communication experience now, with our new tablet!", 30);
        itemList.put(item3.getItemNumber(), item3);

        Item item4 = new BulkItem("CatPi",4,"catpi_4.png",true,60.50, "The ultimate guide for programming cat-related sensors with Raspberry Pi", 3, 5);
        itemList.put(item4.getItemNumber(), item4);

        Item item5 = new Item("Genome Purrnalysis",5,"GenomePurrnalysis_5.jpg",true,1880.00,"Analyse your cat's genome with this awesome new device!", 112);
        itemList.put(item5.getItemNumber(), item5);

        Item item6 = new BulkItem("NetworCat",6,"networcat_6.jpg",true,250.50, "Ever struggled with getting your cat to accept new cats? Forget weeks of trying to get cats familiar with each other! This device will process each cat's odour, mix it, reproduce it, and spray it in the environment. Your new cat will be accepted within hours!", 27, 2);
        itemList.put(item6.getItemNumber(), item6);

        this.persistenceManager.writeItemList(itemList);
    }

    protected synchronized List<String> displayInfo(String itemName) {

        List<String> itemDetails = new ArrayList<>();
        Map<Integer, Item> currentItemList = itemList;

        Map<Integer, Item> filteredMap = currentItemList.entrySet()
                .stream().filter(v -> itemName.equals(v.getValue().getItemName()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        filteredMap.values().forEach(v -> itemDetails.add(v.getItemName()));
        filteredMap.values().forEach(v -> itemDetails.add(v.getDescription()));
        filteredMap.values().forEach(v -> itemDetails.add(v.getItemPrice() + ""));
        filteredMap.values().forEach(v -> itemDetails.add(v.getIsAvailable() + ""));
        filteredMap.values().forEach(v -> itemDetails.add(v.getItemNumber() + ""));

        return itemDetails;
    }


    /**
     * Method to insert an item at the end of the list
     *
     * @param oneItem - the item to be added at the end of the list
     *                //@throws ItemExistsException if item already exists
     */
    public Map<Integer, Item> insertItem(int itemNumber, Item oneItem) throws ItemAlreadyExistsException, IOException { // throws ItemExistsException {
        for (Item item : this.itemList.values()) {
            if (item.getItemName().equals(oneItem.getItemName())) {
                throw new ItemAlreadyExistsException();
            }
        }
        itemList.put(itemNumber, oneItem);
        persistenceManager.writeItemList(itemList);
        return itemList;
    }

    protected void saveCurrentItemList() throws IOException {
        boolean isItemListWritten = this.persistenceManager.writeItemList(this.itemList);
    }

    /**
     * Method to delete an item from List
     *
     * @param itemNumber - item to delete
     */
    public synchronized void deleteItem(int itemNumber) throws IOException {

        this.itemList.entrySet()
                .removeIf(
                        entry -> (itemNumber
                                == entry.getKey()));

        saveCurrentItemList();
    }

    protected synchronized void increaseStock(int itemNumber, int increaseBy) throws IOException {
        this.itemList.get(itemNumber).increaseStock(increaseBy);

        saveCurrentItemList();
    }

    /**
     * Method to search an Item by Item name.
     * The results is a list of Items who returns all Items with the exact search term
     *
     * @param itemName Name of queried item
     * @return List of books with the queried item name (potentially empty)
     */
    protected synchronized Map<Integer, Item> searchItemByName(String itemName) {
        List<String> queryResults = new ArrayList<>();
        Map<Integer, Item> currentItemList = itemList;

        // Capitalize potential item written in low case
        String itemNameC = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);

        Map<Integer, Item> filteredMap = currentItemList.entrySet()
                //.stream().filter(v-> itemName.equals(v.getValue().getItemName()))
                .stream().filter(v -> v.getValue().getItemName().startsWith(itemNameC))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        //System.out.print(currentItemList);

        //filteredMap.values().forEach(v -> queryResults.add(v.getItemName()));

        // System.out.print(queryResults);

        return filteredMap;
    }

    protected synchronized List<Map.Entry<Integer, Item>> listItemsByName() {

        List<Map.Entry<Integer, Item>> l = new ArrayList<>(itemList.entrySet());
        l.sort(Map.Entry.comparingByValue(Comparator.comparing(Item::getItemName)));

        /*
        Collections.sort(l, new Comparator<Map.Entry<Integer, Item>>() {
            @Override
            public int compare(Map.Entry<Integer, Item> o1, Map.Entry<Integer, Item> o2) {
                return o1.getValue().getItemName().compareTo(o2.getValue().getItemName());
            }
        }); */

        return l;
    }

    protected synchronized List<Map.Entry<Integer, Item>> listItemsByNumber() {
        List<Map.Entry<Integer, Item>> l1 = new ArrayList<>(itemList.entrySet());
        l1.sort(Map.Entry.comparingByKey());

        return l1;
    }

    protected synchronized int getLastNumber() {
        int highestNumber = 0;
        for (Item item : itemList.values()) {
            int itemNumber = item.getItemNumber();
            if (itemNumber > highestNumber) {
                highestNumber = itemNumber;
            }
        }
        return highestNumber;
    }

    protected int createNextNumber() {
        int lastNumber = getLastNumber();
        return lastNumber + 1;
    }

    public void getItemListFromFile() throws IOException, ClassNotFoundException {

        this.itemList = this.persistenceManager.readItemList();
    }

    protected boolean isBulkItem(int itemNumber) throws IOException, ClassNotFoundException {
        if (getItemList().get(itemNumber).getBulkSize() != 0){
            return true;
        }else {
        return false;}
    }

    public boolean isBulkSizeCorrect(int bulkItemToAdd, int itemAmount) throws IOException, ClassNotFoundException {
        try {
            if (itemAmount % getBulkSize(bulkItemToAdd) == 0) {
                return true;
            } else return false;
        }catch (IOException ioe) {
            throw new IOException();
        } catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }

    protected int getBulkSize(int itemToAdd) throws IOException, ClassNotFoundException {
        try {
            BulkItem bulkItemToAdd = (BulkItem) getItemList().get(itemToAdd);
            return bulkItemToAdd.getBulkSize();
        }catch (IOException ioe) {
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }

    public synchronized Map<Integer, Item> getItemList() throws IOException, ClassNotFoundException {
        //Map<Integer, Item> currentItemList = this.persistenceManager.readItemList();
        //return currentItemList;
        return this.itemList;
    }

}