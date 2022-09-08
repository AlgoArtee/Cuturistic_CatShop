package ModuleClient.ui.cui;

import ModuleClient.net.CatShopClient;
import ModuleCommon.*;
import ModuleCommon.Exceptions.BulkSizeException;
import ModuleCommon.Exceptions.ItemAlreadyExistsException;
import ModuleCommon.Exceptions.ItemNotExistendException;
import ModuleCommon.Exceptions.ItemNotInCartException;
import ModuleCommon.Exceptions.NameNotExistendException;
import ModuleCommon.Exceptions.NotEnoughItemInStockException;
import ModuleCommon.Exceptions.PasswordNotExistendException;
import ModuleCommon.valueobjects.UserType;
import ModuleCommon.valueobjects.Item;
import ModuleCommon.valueobjects.Receipt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is a simple user interface for the shop.
 * It processes input and output via the command line.
 *
 * @author Chachulski, Mathea
 */
public class ShopClientCUI {
    private IShop shop;
    private BufferedReader in;

    public ShopClientCUI(String shopFile) throws IOException, ClassNotFoundException {
        //the shop class handels everything besides i/o
        shop = new CatShopClient();

        // make Stream-Object for text input in console (command line)
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    /* (non-Javadoc)
     *
     * Interne (private) Methode zur Ausgabe des Menüs.
     */
    private void listActions() {
        emptyRow();
        System.out.print("Welcome to Hello Shop - the Shop experience of the future! \n");
        System.out.print("Menu: \n  List Shop Items:  'shop'");
        System.out.print("         \n  Sign Up As Customer: 'su'");
        System.out.print("         \n  Sign In: 'si'");
        System.out.print("         \n  ---------------------");
        System.out.println("         \n  Sign Out:        'q'");
        System.out.print("> "); // Prompt
        System.out.flush(); // without New Line
    }

    private void listActionsCustomer() {
        emptyRow();
        System.out.print("Menu:    \n  List Shop Items:  'shop'");
        System.out.print("         \n  List Items by Name: 'ln'");
        System.out.print("         \n  List Items by ID: 'l#'");
        System.out.print("         \n  Display Item Information: 'info'");
        System.out.print("         \n  Add item(s) to cart: 'select'");
        System.out.print("         \n  View cart: 'cart'");
        System.out.print("         \n  Increase the amount of an item in your cart: 'more'");
        System.out.print("         \n  Clear your cart; 'clear'");
        System.out.print("         \n  Name 3 Moods and let our AI suggest a product: 'ai'");
        System.out.print("         \n  Search Item:  'f'");
        System.out.print("         \n  Place Order:  'order'");
        System.out.print("         \n  ---------------------");
        System.out.println("         \n  Sign Out:        'q'");
        System.out.print("> "); // Prompt
        System.out.flush(); // without New Line
    }

    private void listActionsStaff() {
        emptyRow();
        System.out.print("Menu:    \n  List Shop Items:  'shop'");
        System.out.print("         \n  List Items by Name: 'ln'");
        System.out.print("         \n  List Items by item number: 'l#'");
        System.out.print("         \n  Display Item Information: 'info'");
        System.out.print("         \n  Add Staff Member: 'addStaff'");
        //System.out.print("         \n  Edit Item: 'edit <item name>'");
        System.out.print("         \n  Add item to inventory: 'new'");
        System.out.print("         \n  increase the stock of an item: 'inc'");
        System.out.print("         \n  Search Item:  'f'");
        System.out.print("         \n  List Log File:  'lf'");
        System.out.print("         \n  ---------------------");
        System.out.println("       \n  Sign Out:        'q'");
        System.out.print("> "); // Prompt
        System.out.flush(); // without New Line
    }

    /* (non-Javadoc)
     *
     * Private Method For reading User Input
     */
    private String readInput() throws IOException {
        // read from console
        return in.readLine();
    }

    private void spellcheck(){
        System.err.println("please check your spelling");
        runMenu();
    }

    private void emptyRow(){
        System.out.println(" ");
    }

    /* (non-Javadoc)
     *
     * Private method for processing Input and Output
     */
    private void processMenuInput(String line) throws IOException, ClassNotFoundException {
        String itemName;
        List<String> itemInfo;

        switch (line) {
            case "shop":
                shopWelcome();
                break;

            //Sign Up
            case "su":
                runCustomerSignup();
                break;

            // Sign In
            case "si":
                runLogin();
                break;

            case "info":
                itemInfoDisplay();
                break;

            case "select":
                runAddToCart();
                break;

            case "cart":
                emptyRow();
                System.out.println("Your Cart:" +"\n" + this.shop.viewCart());
                runMenu();
                break;

            case "more":
                runIncreaseAmountInCart();
                break;

            case "clear":
                shop.clearCart();
                emptyRow();
                System.out.println("Ready for a fresh start!");
                runMenu();
                break;

            case "edit <item name>":
                System.out.print("Implement edit item.");
                runMenu();
                break;

            case "new":
                runAddNewItem();
                break;

            case "inc":
                runIncreaseStock();
                break;

            case "f":
                findItem();

                break;

            case "ai":
                System.out.print("This function will maaaaaaybe be available in the future.");
                runMenu();
                break;

            case "order":
                runPlaceOrder();
                break;

            case "addStaff":
                runStaffSignup();
                break;

            case "ln":
                itemsByName();
               break;

            case "l#":
                itemsByNumber();
                break;

            case "lf":
                displayLog();
                break;

            case "lfi":
                displayLogIBT();
                break;

            case "q":
                runLogOut();

            default:
                spellcheck();
        }
    }

    private void askFor(String thingToAskFor){
        switch (thingToAskFor) {
            case "name" -> System.out.println("Please enter your first and last name:");
            case "employee name" -> System.out.println("Please enter your colleagues first and last name:");
            case "password" -> System.out.println("Please enter your password:");
            case "new password" -> System.out.println("Please enter a password:");
            case "street and number" -> System.out.println("Please enter your street name and house number:");
            case "postal code" -> System.out.println("Please enter your postal code:");
            case "place of residence" -> System.out.println("Please enter your place of residence:");
            case "item name" -> System.out.println("Please enter a catchy name for the new item");
            case "price" -> System.out.println("Please enter an attractive item price following this format '39.99' :");
            case "description" -> System.out.println("Please enter a vivid description of the new item:");
            case "inStock" -> System.out.println("Please enter the amount of items you wish to put in stock:");
            case "# of item" -> System.out.println("please enter the item number:");
            case "increase by" -> System.out.println("Please enter the amount you wish to increase the stock by:");
            case "itemToAdd" -> System.out.println("Please enter the number of the item you want to add to the cart:");
            case "amount" -> System.out.println("Please enter the amount:");
            case "order validation" -> System.out.println("Do you want to order the following items? Type 'yes' or 'no'.");
            case "picture path" -> System.out.println("Please enter the path to the item picture: ");
            case "item name log entries by date" -> System.out.println("Please enter the item name to filter the log data by: ");
        }
    }

    private void shopWelcome(){
        try {

            List<Map.Entry<Integer, Item>> shopList = new ArrayList<>(this.shop.getInventory().entrySet());
            //List<Map.Entry<Integer,Item>> shopList = this.shop.getInventory();

            System.out.println("Welcome to Hello Shop!");
            System.out.println("Browse through our puuuursome variety of products:");
            System.out.println();

            for (var e : shopList) {
                System.out.println(e.getKey() + ":\t" + e.getValue().getItemName() + ":\t" + e.getValue().getDescription());
                System.out.println("Price: " + e.getValue().getItemPrice());
                System.out.println("Item ID (Needed for Adding the Item to Cart!) : " + e.getValue().getItemNumber());
                System.out.println();
            }

            System.out.println();
            System.out.println("Remember to live in the MEOW - but look towards the FUTURE with Hello Shop!");
            System.out.println();

            runMenu();


        }catch (IOException ioe) {
            System.err.println("-- Failed to process Data correctly --");
            runMenu();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void findItem(){
        try {
            String itemName;
            System.out.print("Item Name  > ");
            itemName = readInput();
            Map<Integer, Item> foundItems = shop.searchItemByName(itemName);
            if (foundItems.isEmpty()) {
                System.out.println("No products fit your description. Send us an Email with your awesome CAT - ideas! \n");
            } else {
                System.out.println("The following products were found: \n");
                System.out.println(foundItems);
                System.out.println("\n");
                System.out.println("To find more details about a product enter 'info' and on request a name from this list. \n");
            }
        }catch (IOException e){
            System.err.println("Please enter a product or check the correct spelling of your input.");
        } finally{
            runMenu();
        }

        runMenu();
    }

    public void itemsByName(){
        List<Map.Entry<Integer,Item>> sortedListName = this.shop.listItemsByName();
        emptyRow();
        sortedListName.forEach(i -> {System.out.println(i.getKey() + "\t" + i.getValue().getItemName() + "\t" + "|| Item ID:" + i.getValue().getItemNumber() +":\t Amount:"  + i.getValue().getAmountInStock());});
        runMenu();
    }

    public void itemsByNumber(){
        List<Map.Entry<Integer,Item>> sortedList = this.shop.listItemsByNumber();
        emptyRow();
        sortedList.forEach(i -> {System.out.println(i.getKey() + "\t" + i.getValue().getItemName() + "\t" + "|| Item ID:" + i.getValue().getItemNumber() +":\t Amount:"  + i.getValue().getAmountInStock());});
        runMenu();
    }

    public void itemInfoDisplay() throws IOException {
        String itemName;
        List<String> itemInfo;

        System.out.print("Item Name  > ");

        itemName = readInput();
        itemInfo = this.shop.displayInfo(itemName);

        if(itemInfo.isEmpty())
            {System.out.println("If this wasn't a misspelling: We don't sell this product currently. Send us a request!");}
        else {
            emptyRow();
            System.out.print("Item Name:\t");
            System.out.println(itemInfo.get(0));

            System.out.print("Item Description:\t");
            System.out.println(itemInfo.get(1));

            System.out.print("Item Price:\t");
            System.out.println(itemInfo.get(2));

            System.out.print("Availability:\t");
            if(itemInfo.get(3) == "true")
            {System.out.println("This item is still available!");}
            else {System.out.println("We currently don't have this product. Try again in a few days or send us a request!");}
            System.out.print("Product ID (Important for adding to the Cart!) :\t");
            System.out.println(itemInfo.get(4));

            System.out.println();
        }

        runMenu();
    }

    private void runAddNewItem(){
        try {
            askFor("item name");
            String itemName = readInput();
            int number = this.shop.getItemNumber();
            askFor("picture path");
            String pic = readInput();
            boolean isAvailable = true;
            askFor("price");
            double price = Double.parseDouble(readInput());
            askFor("description");
            String description = readInput();
            askFor("inStock");
            int inStock = Integer.parseInt(readInput());
            this.shop.addItem(itemName, number, pic, isAvailable, price, description, inStock);
            System.out.println("Well done! There are now " + inStock  + " " + itemName +"s available.");
        } catch (IOException e){
            System.err.println("Please check the correct spelling of your input");
        } catch (ItemAlreadyExistsException alreadyExists) {
            System.err.println(alreadyExists.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally{
            runMenu();
        }
    }

    private void runAddToCart() throws ClassNotFoundException {
        try {
            askFor("itemToAdd");
            int itemToAdd = Integer.parseInt(readInput());
            //if (this.itemManager.getItemList().get(itemNumber) != 0) {
                try {
                    if (shop.isBulkItem(itemToAdd)) {
                        int bulkSize = shop.getBulkSize(itemToAdd);
                        System.out.println("This item is sold in units of " + bulkSize + ". \n How many items would you like to add to your cart?");
                    } else {
                        askFor("amount");
                    }
                } catch (IOException ioe) {
                    System.err.println("-- Failed to process Data correctly --");
                    runAddToCart();
                } catch (ClassNotFoundException cnfe) {
                    System.err.println("-- Failed to process Data correctly --");
                    runAddToCart();
                }
            //} else throw new ItemNotExistendException();
            int amountToAdd = Integer.parseInt(readInput());
            this.shop.addToCart(itemToAdd, amountToAdd);
            System.out.println("Item # " + itemToAdd + " is now waiting for you in your cart.");
            runMenu();
        }catch(IOException e){
            spellcheck();
        }catch (NumberFormatException nfe){
            System.err.println("-- Only numbers allowed! --");
            runAddToCart();
        } catch (ItemNotExistendException notExistend) {
            System.err.println(notExistend.getMessage());
            runMenu();
        } catch (BulkSizeException bse) {
            System.err.println(bse.getMessage());
            runAddToCart();
        }
    }

    private void printReceipt(Receipt receipt){
        System.out.println("\n" +
                "Your Receipt" + "\n" +
                "____________" + "\n" +
                "Customer: " + receipt.getCustomerName() + "\n" +
                "Address: " + receipt.getCustomerAddress() + "\n" +
                "Customer No.: " + receipt.getCustomerNumber() + "\n" +
                "Purchase Date: " + receipt.getPurchaseDate()  + "\n" +
                " " + "\n" +
                "Your Order:" + "\n" +
                receipt.getCartItemsAsString() + "\n" +
                " " + "\n" +
                "Please pay ASAP." + "\n" +
                "-------------" + "\n" +
                "Thank you for your business!" + "\n");
    }

    private void runPlaceOrder() {
        try {
            askFor("order validation");
            System.out.println(this.shop.getItemsInCartToPrintThem());
            String answer = readInput();
            if (answer.equals("yes")){
                try {
                    printReceipt(this.shop.placeOrder());
                    runMenu();
                }catch (NotEnoughItemInStockException notEnough){
                    System.err.println(notEnough.getMessage());
                    runMenu();
                }catch (IOException ioe) {
                    System.err.println("-- failed to process the data correctly --");
                    runMenu();
                }catch (ClassNotFoundException cnfe) {
                    System.err.println("-- failed to process the data correctly --");
                    runMenu();
                }
            }else if (answer.equals("no")){
                runMenu();
            }else {
                spellcheck();
            }
        } catch (IOException e){
            spellcheck();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void runIncreaseAmountInCart() {
        try {
            askFor("# of item");
            int itemNumber = Integer.parseInt(readInput());
            try {
                if (shop.isBulkItem(itemNumber)) {
                    int bulkSize = shop.getBulkSize(itemNumber);
                    System.out.println("This item is sold in units of " + bulkSize + ". \n " +
                            "By how many would you like to increase the amount of this item in your cart? \n " +
                            "(Must be a multiple of " + bulkSize + " .)");
                } else {
                    askFor("increase by");
                }
            }catch (IOException ioe) {
                System.err.println("-- Failed to process Data correctly --");
                try {
                    runAddToCart();
                } catch (ClassNotFoundException e) {
                    System.err.println("-- Failed to process Data correctly --");
                    runMenu();
                }
            }catch (ClassNotFoundException cnfe) {
                System.err.println("-- Failed to process Data correctly --");
                try {
                    runAddToCart();
                } catch (ClassNotFoundException e) {
                    System.err.println("-- Failed to process Data correctly --");
                    runMenu();
                }
            } catch (ItemNotExistendException e) {
                System.err.println("-- This item doesn't exist! --");
                runMenu();
            }
            int amount = Integer.parseInt(readInput());
            this.shop.increaseAmountInCart(itemNumber, amount);
            System.out.println("Awesome, you're getting more of the good stuff!");
            runMenu();
        }catch(NumberFormatException nfe){
            System.err.println("-- Only numbers allowed! --");
            runIncreaseAmountInCart();
        }catch(IOException e){
            spellcheck();
        } catch (ItemNotInCartException e) {
            System.out.println("-- You don't have this item in your cart! --");
            runMenu();
        }
    }

    private void runIncreaseStock(){
        try {
            askFor("# of item");
            int itemNumberToIncrease = Integer.parseInt(readInput());
            askFor("increase by");
            int increaseBy = Integer.parseInt(readInput());
            this.shop.increaseStock(itemNumberToIncrease, increaseBy);
            System.out.println("The stock of item #" + itemNumberToIncrease +" has been increased by "
                    + increaseBy + ". " + "\n Spread the word about it!");
            runMenu();
        }catch(IOException | ClassNotFoundException e){
            spellcheck();
        }
    }

    /**
     * runMenu method:
     * - Display LogIn/SignIn
     * - Read user input data
     * - Eingabe verarbeiten und Ergebnis ausgeben
     * (EVA-Prinzip: Eingabe-Verarbeitung-Ausgabe)
     */
    public void runMenu() {
        // Variable for input from the console
        String input = "";

        if (shop.isLoggedInAs(UserType.EMPLOYEE)){
            listActionsStaff();
        }else if (shop.isLoggedInAs(UserType.CUSTOMER) ){
            listActionsCustomer();
        }
        else {
            listActions();
        }
        try {
            input = readInput();
            processMenuInput(input);
        } catch (IOException e) {
            spellcheck();
        } catch (ClassNotFoundException e) {
            System.err.println("-- failed to process the data correctly --");
            runMenu();
        }
    }

    /**
     * generates access to the login process via the shop class
     */
    private void runLogin(){
        try {
            askFor("name");
            String name = readInput();
            askFor("password");
            String password = readInput();
            shop.logIn(name, password);
            System.out.println("Login successful! :)");
            runMenu();
        } catch (PasswordNotExistendException noSuchPassword) {
            System.err.println(noSuchPassword.getMessage());
            runMenu();
        } catch (NameNotExistendException noSuchUser) {
            System.err.println(noSuchUser.getMessage());
            runMenu();
        } catch (IOException e) {
            spellcheck();
        } catch (ClassNotFoundException e) {
            System.err.println("-- failed to process the data correctly --");
            runMenu();
        }
    }

    /**
     * generates access to the customer signup process via the shop class
     */
    private void runCustomerSignup(){
        try {
            askFor("name");
            String name = readInput();
            askFor("new password");
            String password = readInput();
            askFor("street and number");
            String streetAndNumber = readInput();
            askFor("postal code");
            int postalCode = Integer.parseInt(readInput());
            askFor("place of residence");
            String placeOfResidence = readInput();
            shop.signUpCustomer(name, password, streetAndNumber, postalCode, placeOfResidence);
            System.out.println("Welcome " + name + "! The best shopping experience of your life starts now :D");
            runMenu();
        } catch (IOException e) {
            System.err.println("-- A user with this name already exists --");
            runMenu();
        } catch (NumberFormatException nfe){
            System.err.println("-- only numbers allowed for postal code! --");
            runMenu();
        } catch (ClassNotFoundException e) {
            System.err.println("-- failed to process the data correctly --");
            runMenu();
        }
    }

    private void runStaffSignup() {
        try {
            askFor("employee name");
            String name = readInput();
            askFor("new password");
            String password = readInput();
            shop.signUpStaff(name, password);
            System.out.println( name + " was successfully added as a new staff member.");
            runMenu();
        } catch (IOException e) {
            System.err.println("-- A user with this name already exists! --");
            runMenu();
        } catch (ClassNotFoundException e) {
            System.err.println("-- failed to process the data correctly --");
            runMenu();
        } catch (ModuleCommon.Exceptions.UserAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    void displayLog(){
        List<String> logFile = this.shop.returnLogList();

        for(var l : logFile) {
            System.out.println(l + "\n");
        }

        runMenu();
    }

    void displayLogIBT() throws IOException {

        askFor("item name log entries by date");
        String itemName = readInput();
        List<String>logItemByDate = this.shop.returnLogListItemByDate(itemName);

        if (!logItemByDate.isEmpty()){
        for (int i = 0; i < logItemByDate.size(); i++) {
            System.out.println(logItemByDate.get(i));
        }}else{
            System.out.println("Please check your spelling!");
        }

        runMenu();
    }

    private void runLogOut(){
        this.shop.logOut();
        System.out.println(" \n You have been successfully logged out!");
        runMenu();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try{
            ShopClientCUI cui = new ShopClientCUI("Shop");
            cui.runMenu();
        }catch (IOException ioe){
            throw new IOException();
        }catch (ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }
    }
}

