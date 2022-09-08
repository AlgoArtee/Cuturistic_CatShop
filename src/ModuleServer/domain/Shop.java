package ModuleServer.domain;

import ModuleCommon.Exceptions.ItemAlreadyExistsException;
import ModuleCommon.Exceptions.ItemNotInCartException;
import ModuleCommon.valueobjects.Item;
import ModuleCommon.valueobjects.LogInPhase;
import ModuleCommon.valueobjects.Receipt;
import ModuleCommon.valueobjects.UserType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class for the management of a simpel e-shop.
 * Offers methods for returning all items in stock,
 * for item search, for adding new items
 * and for saving the inventory.
 *
 * @author Chachulski, Mathea
 */

public class Shop implements ModuleCommon.IShop {
    private LogInManager logInManager = new LogInManager();
    private ItemManager itemManager = new ItemManager();
    private CustomerManager customerManager = new CustomerManager();
    private StaffManager staffManager = new StaffManager();
    private ModuleCommon.valueobjects.Cart cart;
    public LogEntry entry;
    public Log log = new Log(entry);
    //public volatile Set<Map.Entry<String, String>> itemNameQuantity;
    public Set<Map.Entry<String, String>> itemNameQuantity;

    //Ist das nicht besser im ItemManager aufgehoben?
    //private List<Item> inventory = new ArrayList<>();

    public Shop(String shopFile) throws IOException, ClassNotFoundException {
        this.logInManager.setLogInPhase(LogInPhase.LOGGED_OUT);
        //this.log = new Log(this.entry);
    }

    /**
     * Method returns a copy of the inventory.
     * The original inventory and its copy refer to the same item-objects.
     *
     * @return List of all items in stock (copy)
     */


    // Dependency problems: circular dependencies or restructuring the code???
    // Example in Cart!


    // --- End get itemManager and logInManager

    @Override
    public Map<Integer,Item> getInventory() throws IOException, ClassNotFoundException {
        try {
            Map<Integer, Item> inventory = this.itemManager.getItemList();
            return inventory;
        }catch (IOException ioe) {
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }

    @Override
    public synchronized int getItemNumber(){
        return itemManager.createNextNumber();
    }

    @Override
    public synchronized void logIn(String name, String password) throws ModuleCommon.Exceptions.PasswordNotExistendException, ModuleCommon.Exceptions.NameNotExistendException, ClassNotFoundException, IOException {
        try {
            logInManager.processLoginInfo(name, password);
        } catch (ModuleCommon.Exceptions.PasswordNotExistendException wrongPassword){
            throw new ModuleCommon.Exceptions.PasswordNotExistendException();
        } catch (ModuleCommon.Exceptions.NameNotExistendException wrongName){
            throw new ModuleCommon.Exceptions.NameNotExistendException();
        } catch (IOException e) {
            throw new IOException();
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException();
        }
    }

    @Override
    public List<String> displayInfo(String itemName){
        return itemManager.displayInfo(itemName);
    }

    @Override
    public synchronized void signUpCustomer(String name, String password, String streetAndNumber, int postalCode, String placeOfResidence) throws IOException, ClassNotFoundException {
        try {
            customerManager.processCustomerSignupInfo(name, password, streetAndNumber, postalCode, placeOfResidence);
        } catch (ModuleCommon.Exceptions.UserAlreadyExistsException userExists) {
            throw new IOException();
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException();
        }
    }

    @Override
    public synchronized void signUpStaff(String name, String password) throws IOException, ClassNotFoundException {
        try {
            staffManager.addEmployee(name, password);
            logData("employeeAdded", "", name);
        } catch (ModuleCommon.Exceptions.UserAlreadyExistsException userExists) {
            throw new IOException();
        } catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }

    @Override
    public List<Map.Entry<Integer,Item>> listItemsByNumber(){
        return this.itemManager.listItemsByNumber();
    }

    @Override
    public List<Map.Entry<Integer,Item>> listItemsByName(){
        return this.itemManager.listItemsByName();
    }


    @Override
    public Map<Integer ,Item> searchItemByName(String itemName) {return this.itemManager.searchItemByName(itemName);}


    //public List<String> searchItemByNumber(int itemNumber) {return this.itemManager.searchItemByNumber(itemNumber);}

    @Override
    public synchronized boolean isLoggedInAs(UserType userType){
        return (getLoginPhase() == LogInPhase.LOGGED_IN && getUserType() == userType);
    }

    private synchronized LogInPhase getLoginPhase(){
        return this.logInManager.getLogInPhase();
    }

    private synchronized UserType getUserType(){
        return this.logInManager.getUserType();
    }

    @Override
    public synchronized void addItem(String itemName, int number, String pic, boolean isAvailable, double price, String description, int inStock) throws ModuleCommon.Exceptions.ItemAlreadyExistsException {
        Item newItem = new Item(itemName, number, pic, isAvailable, price, description, inStock);

        try {
            this.itemManager.insertItem(number, newItem);
        } catch (IOException | ItemAlreadyExistsException e) {
            throw new RuntimeException(e);
        }

        logData("addItem", Integer.toString(newItem.getInStock()), newItem.getItemName());

    }

    @Override
    public List<String> returnLogList(){return this.log.returnLogList();}

    // Display Item Log by Date
    @Override
    public List<String> returnLogListItemByDate(String itemName){

        List<String> logList = this.log.returnLogList();

        List<String> dateList = new ArrayList<>();

        Map<String, String> mapForSorting = new HashMap<>();


        for(var l : logList) {
            //System.out.println(l + "\n");
            String[] tokens = l.split("\t");

            for(int i = 0; i < tokens.length; i++){
                if (i == 3 && tokens[i].equals(itemName)){

                    mapForSorting.put(tokens[1].split("\s")[1]+tokens[1].split("\s")[2]+tokens[1].split("\s")[3],l);

                    //System.out.println(tokens[i]);
                    //System.out.println(tokens[1].split("\s")[1]);
                    //System.out.println(tokens[1].split("\s")[2]);
                    //System.out.println(tokens[1].split("\s")[3]);
                    //dateList.add(l);
                }


            }
        }

        // Do the sorting
        List<Map.Entry<String, String>> dateListSorting = new ArrayList<>(mapForSorting.entrySet());
        dateListSorting.sort(Map.Entry.comparingByKey());

        // add to list
        dateListSorting.forEach(i-> {dateList.add(i.getValue());});

        //System.out.println(dateList);
        return dateList;


    }
    // End Display Item Log by Date

    @Override
    public synchronized void addToCart(int itemToAdd, int itemAmount) throws ModuleCommon.Exceptions.ItemNotExistendException, IOException, ClassNotFoundException, ModuleCommon.Exceptions.BulkSizeException {
        try {
            if (this.itemManager.getItemList().containsKey(itemToAdd)) {
                if (isBulkItem(itemToAdd)){
                    if (isBulkSizeCorrect(itemToAdd, itemAmount)){
                        this.customerManager.addToCart(itemToAdd, itemAmount);
                    }else {
                        throw new ModuleCommon.Exceptions.BulkSizeException();
                    }
                }else {
                    this.customerManager.addToCart(itemToAdd, itemAmount);
                }
            } else throw new ModuleCommon.Exceptions.ItemNotExistendException();
        }catch (IOException ioe) {
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        } catch (ModuleCommon.Exceptions.BulkSizeException bse) {
            throw new ModuleCommon.Exceptions.BulkSizeException();
        }
    }

    private boolean isBulkSizeCorrect(int itemToAdd, int itemAmount) throws IOException, ClassNotFoundException {
        try {
            if (this.itemManager.isBulkSizeCorrect(itemToAdd, itemAmount)){
                return true;
            }else {
                return false;
            }
        }catch (IOException ioe) {
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }

    @Override
    public synchronized void increaseAmountInCart(int itemNumber, int increaseBy) throws ItemNotInCartException {
       this.customerManager.increaseAmountInCart(itemNumber, increaseBy);
    }

    @Override
    public synchronized String getItemsInCartToPrintThem() throws IOException, ClassNotFoundException {
        try {
            return this.customerManager.getItemsInCartToPrintThem();
        }catch (IOException ioe) {
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }

    @Override
    public synchronized void increaseStock(int itemNumber, int increaseBy) throws IOException, ClassNotFoundException {
        this.itemManager.increaseStock(itemNumber, increaseBy);

        // logging increased Stock for an item
        Item loggedItem = itemManager.getItemList().get(itemNumber);
        logData("increaseItemSupply", Integer.toString(loggedItem.getInStock()), loggedItem.getItemName());

        // end logging

        // Update Item List on File with new data
        this.itemManager.persistenceManager.writeItemList(itemManager.getItemList());
    }

    /**
     * This Method filters the inventory for the purchased items (drawn from the CartList of the current user's cart).
     * The amount of purchased items is then read from the cartList. The method uses the items own decreaseInStock()
     * function to subtract that number from the amount of items of this particular type in stock.
     *
     */
    private synchronized void decreaseStockAtPurchase() throws ModuleCommon.Exceptions.NotEnoughItemInStockException, IOException, ClassNotFoundException {

        Map<Integer, Integer> purchasedItems = getCurrentCart().getCartList();
        try {
            for (int itemNumber : purchasedItems.keySet()) {
                Item purchasedItem = getInventory().get(itemNumber);
                for (Item item : getInventory().values()) {
                    if (item.equals(purchasedItem)) {
                        item.decreaseStock(purchasedItems.get(itemNumber));
                    }
                }
            }
        }catch (ModuleCommon.Exceptions.NotEnoughItemInStockException notEnough){
            throw new ModuleCommon.Exceptions.NotEnoughItemInStockException();
        }catch (IOException ioe) {
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }

    @Override
    public synchronized String viewCart() throws IOException, ClassNotFoundException {
        try {
            return this.customerManager.viewCart();
        }catch (IOException ioe) {
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }

    @Override
    public synchronized Receipt placeOrder() throws ModuleCommon.Exceptions.NotEnoughItemInStockException, IOException, ClassNotFoundException {
        try {
            decreaseStockAtPurchase();
            Receipt receipt = getCurrentCart().buyNow();

            //logData Entry
            itemNameQuantity = getCurrentCart().getItemNameQuantity().entrySet();

            for (Map.Entry<String, String> inq: itemNameQuantity) {
                logData("boughtItem", inq.getKey(), inq.getValue());
            }
            //logData Entry End


            // Write decrease to PERSITANCE file
            saveCurrentItemList();
            // End Write decrease to PERSITANCE file

            return receipt;
        }catch (ModuleCommon.Exceptions.NotEnoughItemInStockException notEnough){
            throw new ModuleCommon.Exceptions.NotEnoughItemInStockException();
        }catch (IOException ioe) {
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }

    @Override
    public void logOut(){
        this.logInManager.logOut();
    }

    private synchronized ModuleCommon.valueobjects.Cart getCurrentCart(){
        ModuleCommon.valueobjects.Customer currentCustomer = (ModuleCommon.valueobjects.Customer) SessionState.currentUser;
        return currentCustomer.getCurrentCart();
    }

    @Override
    public synchronized void clearCart(){
        this.customerManager.clearCart();
    }

    @Override
    public synchronized void saveCurrentItemList() throws IOException {
        this.itemManager.saveCurrentItemList();
    }

    @Override
    public synchronized void deleteItem(int itemNumber) throws IOException, ClassNotFoundException {

        String name = this.itemManager.getItemList().get(itemNumber).getItemName();
        itemManager.deleteItem(itemNumber);


        logData("itemDelete", "-1", name);

        saveCurrentItemList();
    }

    private synchronized void logData(String type, String stockAmount, String name)
    {
        // ---- Log Data ----
        Map<String, String> itemAdded = new HashMap<>();
        Map<String, String> employeeAdded = new HashMap<>();
        Map<String, String> itemDeleted = new HashMap<>();

        // Date variable
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        LocalDateTime now = LocalDateTime.now();
        String dateTimeString = now.getDayOfWeek() + " " + now.format(formatter);


        if (stockAmount.isBlank()){
            // Employee is Added
            employeeAdded.put("Welcome ", name);

        } else if (stockAmount.equals("-1")) {
            // Item is Deleted
            itemAdded.put("Out of Stock: ", name);
        } else {
            // Item is Added
            itemAdded.put(stockAmount, name);
        }

        // Log Entry
        if(type.equals("addItem"))
        {
            this.entry = new LogEntry(EventType.ADD_NEW_TO_STORAGE.toString(), dateTimeString, itemAdded, SessionState.currentUser.name);
        }
        else if (type.equals("boughtItem")) {

            this.entry = new LogEntry(EventType.PURCHASE.toString(), dateTimeString, itemAdded, SessionState.currentUser.name);
        }
        else if (type.equals("increaseItemSupply")) {
            this.entry = new LogEntry(EventType.INCREASE_ITEM_SUPPLY.toString(), dateTimeString, itemAdded, SessionState.currentUser.name);
        }
        else if (type.equals("employeeAdded")) {
            this.entry = new LogEntry(EventType.NEW_EMPLOYEE_ADDED.toString(), dateTimeString, employeeAdded, SessionState.currentUser.name);
        }
        else if (type.equals("itemDelete")) {
            this.entry = new LogEntry(EventType.ITEM_DELETED.toString(), dateTimeString, itemAdded, SessionState.currentUser.name);
        }

        this.log = new Log(this.entry);
        log.writeLogEntries();

        //Log updateLog = new Log(this.entry);
        //updateLog.writeLogEntries();
    }

    @Override
    public boolean isBulkItem(int itemNumber) throws IOException, ClassNotFoundException, ModuleCommon.Exceptions.ItemNotExistendException {
        if (this.itemManager.getItemList().get(itemNumber) != null) {
            try {
                return this.itemManager.isBulkItem(itemNumber);
            } catch (IOException ioe) {
                throw new IOException();
            } catch (ClassNotFoundException cnfe) {
                throw new ClassNotFoundException();
            }
        }else throw new ModuleCommon.Exceptions.ItemNotExistendException();
    }

    @Override
    public int getBulkSize(int itemToAdd) throws IOException, ClassNotFoundException {
        try {
            return this.itemManager.getBulkSize(itemToAdd);
        }catch (IOException ioe) {
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }
}
