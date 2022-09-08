package ModuleServer.net;

import ModuleCommon.*;
import ModuleCommon.Exceptions.*;
import ModuleCommon.valueobjects.Item;
import ModuleCommon.valueobjects.Receipt;
import ModuleCommon.valueobjects.UserType;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ClientRequestProcessor handels incoming requests from the client by passing them on to the shop and returning
 * results to the client as a new outgoing command.
 *
 * @author Chachulsky, Mathea
 */
public class ClientRequestProcessor implements Runnable{

    private BufferedReader socketIn;
    private PrintStream socketOut;
    final String separator = ";";

    IShop shop;

    public ClientRequestProcessor(Socket socket, IShop shop) throws IOException {
        this.shop = shop;

        OutputStream outputStream = socket.getOutputStream();
        socketOut = new PrintStream(outputStream);

        InputStream inputStream = socket.getInputStream();
        socketIn = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public void run() {
        while(true) {
            try {
                String receivedData = socketIn.readLine(); // BufferedReader bietet readLine()
                handleCommandRequest(receivedData);
            } catch (SocketException e) {
                System.err.println("Client has closed connection...");
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (PasswordNotExistendException e) {
                throw new RuntimeException(e);
            } catch (ItemNotExistendException e) {
                throw new RuntimeException(e);
            } catch (BulkSizeException e) {
                throw new RuntimeException(e);
            } catch (NotEnoughItemInStockException e) {
                throw new RuntimeException(e);
            } catch (ItemAlreadyExistsException e) {
                throw new RuntimeException(e);
            } catch (ItemNotInCartException e) {
                throw new RuntimeException(e);
            } catch (NameNotExistendException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleCommandRequest(String receivedData) throws IOException, ClassNotFoundException, ModuleCommon.Exceptions.PasswordNotExistendException, ModuleCommon.Exceptions.NameNotExistendException, ModuleCommon.Exceptions.ItemAlreadyExistsException, ModuleCommon.Exceptions.ItemNotExistendException, ModuleCommon.Exceptions.BulkSizeException, ModuleCommon.Exceptions.ItemNotInCartException, ModuleCommon.Exceptions.NotEnoughItemInStockException {
        System.err.println("Data received from client: " + receivedData);
        String[] parts = receivedData.split(separator);

        switch (Commands.valueOf(parts[0])) {
            case CMD_GET_INVENTORY -> handleGetInventory();
            case CMD_GET_ITEMNUMBER -> handleGetItemNumber();
            case CMD_GET_ITEMS_IN_CART_TO_PRINT_THEM -> handleGetItemsInCart();
            case CMD_GET_BULKSIZE -> handleGetBulkSize(parts);
            case CMD_LOGIN -> handleLogIn(parts);
            case CMD_DISPLAY_INFO -> handleDisplayInfo(parts);
            case CMD_SIGNUP_CUSTOMER -> handleSignUpCustomer(parts);
            case CMD_SIGNUP_STAFF -> handleSignUpStaff(parts);
            case CMD_LIST_ITEMS_BY_NUMBER -> handleListItemsByNumber();
            case CMD_LIST_ITEMS_BY_NAME -> handleListItemsByName();
            case CMD_SEARCH_ITEM_BY_NAME -> handleSearchItemByName(parts);
            case CMD_IS_LOGGEDIN_AS -> handleIsLoggedInAs(parts);
            case CMD_ADD_ITEM -> handleAddItem(parts);
            case CMD_RETURN_LOGLIST -> handleReturnLoglist();
            case CMD_RETURN_LOGLISTITEM_BY_DATE -> handleReturnLoglistItemsByDate(parts);
            case CMD_ADD_TO_CART -> handleAddToCart(parts);
            case CMD_INCREASE_AMOUNT_IN_CART -> handleIncAmountInCart(parts);
            case CMD_INCREASE_STOCK -> handleIncreaseStock(parts);
            case CMD_VIEW_CART -> handleViewCart();
            case CMD_PLACE_ORDER -> handlePlaceOrder();
            case CMD_CLEAR_CART -> handleClearCart();
            case CMD_LOGOUT -> handleLogOut();
            case CMD_SAVE_CURRENT_ITEMLIST -> handleSaveCurrentItemList();
            case CMD_DELETE_ITEM -> handleDeleteItem(parts);
            case CMD_IS_BULKITEM -> handleIsBulkItem(parts);

            default -> System.err.println("Invalid Request received!");
        }
    }

    private void handleIsBulkItem(String[] data) throws ModuleCommon.Exceptions.ItemNotExistendException, IOException, ClassNotFoundException {

        int itemNumber = Integer.parseInt(data[1]);

        boolean isBulkItem = shop.isBulkItem(itemNumber);

        String isBulkItemStr = "false";

        if(isBulkItem){

            isBulkItemStr = "true";

        }

        String cmd = Commands.CMD_IS_BULKITEM_RESP.name();

        cmd += separator + isBulkItemStr;

        socketOut.println(cmd);

    }

    private void handleDeleteItem(String[] data) throws IOException, ClassNotFoundException {

        int itemNumber = Integer.parseInt(data[0]);

        shop.deleteItem(itemNumber);

    }

    private void handleSaveCurrentItemList() throws IOException {

        shop.saveCurrentItemList();

    }

    private void handleLogOut() {

        shop.logOut();
    }

    private void handleClearCart() {

        shop.clearCart();

        // TODO: any response?

    }

    private void handlePlaceOrder() throws ModuleCommon.Exceptions.NotEnoughItemInStockException, IOException, ClassNotFoundException {
        try {
            // return is of type Receipt -> (int customerNumber, String cartItemsAsString, Address customerAddress) + purchaseDate + customerName
            Receipt receipt = shop.placeOrder();

            //receipt is serialized for transportation via sockets
            ObjectOutputStream oos = new ObjectOutputStream(socketOut);
            oos.writeObject(receipt);
        } catch (NotEnoughItemInStockException neiise){
            socketOut.println(Commands.CMD_PLACE_ORDER_RESP.name() + separator + "fail");
        }
/*
        String cmd = Commands.CMD_PLACE_ORDER_RESP.name();

        int customerNumber = receipt.getCustomerNumber();
        String cartItemAsString = receipt.getCartItemsAsString();
        String address = receipt.getCustomerAddress().toString();

        cmd += separator + customerNumber + separator + cartItemAsString + separator + address;
        socketOut.println(cmd);*/
    }

    private void handleViewCart() throws IOException, ClassNotFoundException {

        String viewCart = shop.viewCart();

        String cmd = Commands.CMD_VIEW_CART_RESP.name() + separator + viewCart;
        System.out.println(viewCart);

        socketOut.println(cmd);

    }

    private void handleIncreaseStock(String[] data) throws IOException, ClassNotFoundException {

        // data[1...] = int itemNumber, int increaseBy

        int itemNumber = Integer.parseInt(data[1]);
        int increaseBy = Integer.parseInt(data[2]);

        String cmd = Commands.CMD_INCREASE_STOCK_RESP.name();
        try {
            shop.increaseStock(itemNumber, increaseBy);
            cmd += separator + "success";
        }catch (Exception e){
            cmd += separator + "error";
        }
        socketOut.println(cmd);
    }

    private void handleIncAmountInCart(String[] data) throws ModuleCommon.Exceptions.ItemNotInCartException {

        // data[1...] = int itemNumber, int increaseBy

        int itemNumber = Integer.parseInt(data[1]);
        int increaseBy = Integer.parseInt(data[2]);

        String cmd = Commands.CMD_INCREASE_AMOUNT_IN_CART_RESP.name();
        try {
            shop.increaseAmountInCart(itemNumber,increaseBy);
            cmd += separator + "Success";
        } catch(ItemNotInCartException nic){
            cmd += separator + "ItemNotInCartException";
        }catch (Exception e){
            cmd += separator + "Exception";
        }
        socketOut.println(cmd);

    }

    private void handleAddToCart(String[] data) throws ModuleCommon.Exceptions.ItemNotExistendException, ModuleCommon.Exceptions.BulkSizeException, IOException, ClassNotFoundException {

        // data[1...] = int itemToAdd, int itemAmount;

        int itemToAdd = Integer.parseInt(data[1]);
        int itemAmount = Integer.parseInt(data[2]);

        String cmd = Commands.CMD_ADD_TO_CART_RESP.name();

        try {
            shop.addToCart(itemToAdd,itemAmount);
            cmd += separator + "Success";
        } catch(IOException ioe){
            cmd += separator + "IOException";
        }catch (ClassNotFoundException cnfe){
            cmd += separator + "ClassNotFoundException";
        }catch (ModuleCommon.Exceptions.ItemNotExistendException inee){
            cmd += separator + "ItemNotExistendException";
        }catch (ModuleCommon.Exceptions.BulkSizeException bse){
            cmd += separator + "BulkSizeException";
        }catch (Exception e){
            cmd += separator + "Exception";
        }

        socketOut.println(cmd);
    }

    private void handleReturnLoglistItemsByDate(String[] data) {

        List<String> logList = shop.returnLogListItemByDate(data[1]);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socketOut);
            oos.writeObject(logList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // data[1] = itemName

        /*String itemName= data[1];

        List<String> logListIBD = shop.returnLogListItemByDate(itemName);

        String cmd = Commands.CMD_RETURN_LOGLISTITEM_BY_DATE_RESP.name();

        for (String logEntry : logListIBD) {
            cmd += separator + logEntry;
        }

        socketOut.println(cmd);*/

    }

    private void handleReturnLoglist() {

        List<String> logList = shop.returnLogList();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socketOut);
            oos.writeObject(logList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //logList is serialised for transport via sockets
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socketOut);
            oos.writeObject(logList);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        /*
        String cmd = Commands.CMD_RETURN_LOGLIST_RESP.name();

        for (String logEntry : logList) {
            cmd += separator + logEntry;
        }

        socketOut.println(cmd);
         */

    }

    private void handleAddItem(String[] data) throws ModuleCommon.Exceptions.ItemAlreadyExistsException {

        // data[1...] = String itemName, int number, String pic, boolean isAvailable, double price, String description, int inStock

        boolean isAvailable = false;

        String itemName = data[1];
        int number = Integer.parseInt(data[2]);
        String pic = data[3];
        if (data[4].equals("true")){
            isAvailable = true;
        }
        double price = Double.parseDouble(data[5]);
        String description = data[6];
        int inStock = Integer.parseInt(data[7]);

        String cmd = Commands.CMD_ADD_ITEM_RESP.name();
        try {
            shop.addItem(itemName, number, pic, isAvailable, price, description, inStock);
            cmd += separator + "success";
        }catch (Exception re){
            cmd += separator + "error";
        }
        socketOut.println(cmd);
    }

    private void handleIsLoggedInAs(String[] data) {

        // data[1] = userType

        UserType userType = null;

        if (data[1].equals("employee")){
            userType = UserType.EMPLOYEE;
        } else if (data[1].equals("customer")) {
            userType = UserType.CUSTOMER;
        }

        boolean result = shop.isLoggedInAs(userType);

        String cmd = Commands.CMD_IS_LOGGEDIN_AS_RESP.name();

        cmd += separator + result;

        socketOut.println(cmd);

    }

    private void handleSearchItemByName(String[] data) {
        Map<Integer, Item> searchResult;
        if (!(data.length == 1)) {
            String itemName = data[1];

            searchResult = shop.searchItemByName(itemName);
        } else {
            searchResult = new HashMap<>();
        }
            /*
            if (!searchResult.isEmpty()) {
                //searResult is serialised for transport via sockets
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(socketOut);
                    oos.writeObject(searchResult);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            try {
                Map<Integer, Item> searchResult = shop.getInventory();
                ObjectOutputStream oos = new ObjectOutputStream(socketOut);
                oos.writeObject(searchResult);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }*/

        StringBuilder cmd = new StringBuilder(Commands.CMD_SEARCH_ITEM_BY_NAME_RESP.name());

        if (!searchResult.isEmpty()) {
            for (var e : searchResult.entrySet()) {
                cmd.append(separator).append(e.getValue().getItemNumber());
                cmd.append(separator).append(e.getValue().getItemName());
                cmd.append(separator).append(e.getValue().getPic());
                cmd.append(separator).append(e.getValue().getDescription());
                cmd.append(separator).append(e.getValue().getItemPrice());
                cmd.append(separator).append(e.getValue().getIsAvailable());
                cmd.append(separator).append(e.getValue().getInStock());
            }
        }
        socketOut.println(cmd);

    }

    private void handleListItemsByName() {

        List<Map.Entry<Integer,Item>> sortedList = shop.listItemsByName();

       /* klappt nicht - vermutlich, weil Map.Entry nicht serialisierbar ist:

        //sortedList is serialised for transport via sockets
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socketOut);
            oos.writeObject(sortedList);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }*/

        String cmd = Commands.CMD_LIST_ITEMS_BY_NAME_RESP.name();

        for (var e : sortedList) {
            cmd += separator + e.getValue().getItemName();
            cmd += separator + e.getValue().getItemNumber();
            cmd += separator + e.getValue().getPic();
            cmd += separator + e.getValue().getIsAvailable();
            cmd += separator + e.getValue().getItemPrice();
            cmd += separator + e.getValue().getDescription();
            cmd += separator + e.getValue().getInStock();
        }

        socketOut.println(cmd);
    }

    private void handleListItemsByNumber() {

        List<Map.Entry<Integer,Item>> sortedList = shop.listItemsByNumber();

        /* klappt nicht - vermutlich, weil Map.Entry nicht serialisierbar ist:
        //sortedList is serialised for transport via sockets
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socketOut);
            oos.writeObject(sortedList);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }*/

        //Item item = e.getValue();
        String cmd = Commands.CMD_LIST_ITEMS_BY_NUMBER_RESP.name();
        for (var e : sortedList) {
            cmd += separator + e.getValue().getItemName();
            cmd += separator + e.getValue().getItemNumber();
            cmd += separator + e.getValue().getPic();
            cmd += separator + e.getValue().getIsAvailable();
            cmd += separator + e.getValue().getItemPrice();
            cmd += separator + e.getValue().getDescription();
            cmd += separator + e.getValue().getInStock();
        }

        socketOut.println(cmd);
    }

    private void handleSignUpStaff(String[] data) throws IOException, ClassNotFoundException {

        // data = String name, String password
        String name = data[1];
        String password = data[2];

        String cmd = Commands.CMD_SIGNUP_STAFF_RESP.name();

        try {
            shop.signUpStaff(name, password);
            cmd += separator + "Success";
        }catch (IOException ioe){
            cmd += separator + "userExistent";
        }catch (ClassNotFoundException cnfe){
            cmd += separator + "ClassNotFoundException";
        }catch (Exception e){
            cmd += separator + "Exception";
        }
        socketOut.println(cmd);
    }

    private void handleSignUpCustomer(String[] data) throws IOException, ClassNotFoundException {

        //data = String name, String password, String streetAndNumber, int postalCode, String placeOfResidence;

        String name = data[1];
        String password = data[2];
        String streetAndNumber = data[3];
        int postalCode = Integer.parseInt(data[4]);
        String placeOfResidence = data[5];

        String cmd = Commands.CMD_SIGNUP_CUSTOMER_RESP.name();

        try {
            shop.signUpCustomer(name,password,streetAndNumber,postalCode,placeOfResidence);
            cmd += separator + "Success";
        }catch (IOException ioe){
            cmd += separator + "IOException";
        }catch (ClassNotFoundException cnfe){
            cmd += separator + "ClassNotFoundException";
        }catch (Exception e){
            cmd += separator + "Exception";
        }
        socketOut.println(cmd);
    }

    private void handleDisplayInfo(String[] data) {

        String itemName = data[1];
        List<String> itemDetails = new ArrayList<>(shop.displayInfo(itemName));

        String cmd = Commands.CMD_DISPLAY_INFO_RESP.name();

        // itemDetails List: [0]-> ItemName, [1] -> Description, [2] -> Price, [3] -> Availability, [4] -> Item Number
        cmd += separator + itemDetails.get(0) + separator + itemDetails.get(1) + separator + itemDetails.get(2) + separator + itemDetails.get(3) + separator + itemDetails.get(4);

        socketOut.println(cmd);

    }

    private void handleLogIn(String[] data) throws ModuleCommon.Exceptions.PasswordNotExistendException, IOException, ClassNotFoundException, ModuleCommon.Exceptions.NameNotExistendException {

        String name = data[1];

        String password = data[2];

        String cmd = Commands.CMD_LOGIN_RESP.name();

        try {
            shop.logIn(name, password);

            cmd += separator + "Success";
        }catch (PasswordNotExistendException pne ){
            cmd += separator + "PasswordNotExistendException";
        }catch(IOException ioe){
            cmd += separator + "IOException";
        }catch (ClassNotFoundException cnfe){
            cmd += separator + "ClassNotFoundException";
        }catch (NameNotExistendException nnee){
            cmd += separator + "NameNotExistendException";
        }catch (Exception e){
            cmd += separator + "Exception";
        }

        socketOut.println(cmd);
    }

    private void handleGetBulkSize(String[] data) throws IOException, ClassNotFoundException {

        String bulkSize_str = (data.length == 1) ? "0" : data[1];

        int bulkSize = shop.getBulkSize(Integer.parseInt(bulkSize_str));

        String cmd = Commands.CMD_GET_BULKSIZE_RESP.name();

        cmd += separator + bulkSize;

        socketOut.println(cmd);
    }

    private void handleGetItemsInCart() throws IOException, ClassNotFoundException {

        String printCartItems = shop.getItemsInCartToPrintThem();

        String cmd = Commands.CMD_GET_ITEMS_IN_CART_TO_PRINT_THEM_RESP.name();

        cmd += separator + printCartItems;

        socketOut.println(cmd);
    }

    private void handleGetItemNumber() {
        // This method is used in CUI - part of another function
        // Does it need to be part of the communication protocol?

        int itemNumber = shop.getItemNumber();

        String cmd = Commands.CMD_GET_ITEMNUMBER_RESP.name();

        cmd += separator + itemNumber;

        socketOut.println(cmd);

    }


    private void handleGetInventory() throws IOException, ClassNotFoundException {

        Map<Integer, Item> listItems = shop.getInventory();

        ObjectOutputStream oos = new ObjectOutputStream(socketOut);
        oos.writeObject(listItems);

        /*
        String cmd = Commands.CMD_GET_INVENTORY_RESP.name();

        for (Item i : listItems.values()) {
            cmd += separator + i.getItemNumber();
            cmd += separator + i.getItemName();
            cmd += separator + i.getPic();
            cmd += separator + i.getDescription();
            cmd += separator + i.getItemPrice();
            cmd += separator + i.getIsAvailable();
            cmd += separator + i.getInStock();
        }

        socketOut.println(cmd);
        */
    }

}
