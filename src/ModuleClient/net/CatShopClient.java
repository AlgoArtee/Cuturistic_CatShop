package ModuleClient.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import ModuleCommon.Exceptions.BulkSizeException;
import ModuleCommon.Exceptions.ItemAlreadyExistsException;
import ModuleCommon.Exceptions.ItemNotExistendException;
import ModuleCommon.Exceptions.ItemNotInCartException;
import ModuleCommon.Exceptions.NameNotExistendException;
import ModuleCommon.Exceptions.NotEnoughItemInStockException;
import ModuleCommon.Exceptions.PasswordNotExistendException;
import ModuleCommon.Exceptions.UserAlreadyExistsException;
import ModuleCommon.valueobjects.Address;
import ModuleCommon.valueobjects.Item;
import ModuleCommon.valueobjects.Receipt;
import ModuleCommon.valueobjects.UserType;
import ModuleCommon.*;

import java.io.*;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateRevokedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * @author Chachulsky, Mathea
 */
    public class CatShopClient implements ModuleCommon.IShop{
        private Socket socket;
        private BufferedReader socketIn;
        private PrintStream socketOut; // Vorteil: flushed automatisch
        final String separator = ";";

        public CatShopClient() throws IOException {
            // Verbindung zum Server aufbauen
            socket = new Socket("127.0.0.1", 1399);
            // Siehe Doku:
            // With this option set to a positive timeout value, a read() call on the InputStream associated with
            // this Socket will block for only this amount of time.
            socket.setSoTimeout(10000); // Jegliche Antworten vom Server werden innerhalb einer Sekunde erwartet (1000)

            // Streams vom Socket holen
            InputStream inputStream = socket.getInputStream();
            socketIn = new BufferedReader(new InputStreamReader(inputStream));
            socketOut = new PrintStream(socket.getOutputStream());
        }

        private String[] readResponse() {
            String[] parts = null;
            try {
                // Auf Antwort warten. Es wird maximal 1000ms gewartet
                String receivedData = socketIn.readLine();
                parts = receivedData.split(separator);

                //System.err.println("received answer: " + receivedData);
            } catch(SocketTimeoutException e) {
                System.out.println("Server didn't answer.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return parts;
        }

        public Map<Integer, Item> getInventory() throws IOException, ClassNotFoundException{
            String cmd = Commands.CMD_GET_INVENTORY.name();
            socketOut.println(cmd);

            //deserialize Map inventory
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            HashMap<Integer, Item> inventory = (HashMap<Integer, Item>) ois.readObject();
            return inventory;

            /*
            //get and process response
            String[] data = readResponse();

            if(Commands.valueOf(data[0]) != Commands.CMD_GET_INVENTORY_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            return createInventoryFromData(data);
            */
        }

        private Map<Integer, Item> createInventoryFromData(String[] data)
        {
            Map<Integer, Item> itemList = new HashMap<>();

            for(int i = 1; i < data.length; i += 7){
                int itemNumber = Integer.parseInt(data[i]);
                String itemName = data[i + 1];
                String pic = data[i + 2];
                String description = data[i +3 ];
                double price=Double.parseDouble(data[i + 4]);
                boolean isAvailable=Boolean.parseBoolean(data[i + 5]);
                int inStock=Integer.parseInt(data[i + 6]);

                itemList.put(itemNumber,new Item(itemName,itemNumber,pic,isAvailable,price,description,inStock));
            }

            return itemList;
        }

        @Override
        public int getItemNumber() {
            String cmd = Commands.CMD_GET_ITEMNUMBER.name();
            socketOut.println(cmd);

            String[] data = readResponse();

            if(Commands.valueOf(data[0]) != Commands.CMD_GET_ITEMNUMBER_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }

            return Integer.parseInt(data[1]);
        }

        @Override
        public void logIn(String name, String password) throws PasswordNotExistendException, NameNotExistendException, ClassNotFoundException, IOException {
            String cmd = Commands.CMD_LOGIN.name() + separator + name + separator + password;
            socketOut.println(cmd);

            // auf antwort warten (blockierend)
            // socketIn.readLine(); SUCCESS, EXCEPTION_Name
            // throw NameNotExistendException;
            String[] data = readResponse();

            if(Commands.valueOf(data[0]) != Commands.CMD_LOGIN_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            if (!data[1].equals("Success")) {
                switch (data[1]) {
                    case "PasswordNotExistendException":
                        throw new PasswordNotExistendException();
                    case "NameNotExistendException":
                        throw new NameNotExistendException();
                    case "ClassNotFoundException":
                        throw new ClassNotFoundException();
                    default:
                        throw new IOException();
                }
            }
        }

        @Override
        public List<String> displayInfo(String itemName) {
            String cmd = Commands.CMD_DISPLAY_INFO.name() + separator + itemName;
            socketOut.println(cmd);

            String[] data = readResponse();

            if(Commands.valueOf(data[0]) != Commands.CMD_DISPLAY_INFO_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }

            return createDisplayInfoFromData(data);
        }

        private List<String> createDisplayInfoFromData(String[] data){
            List<String> displayInfo = new ArrayList<>();

            for(int i = 1; i < data.length; i += 5){
                String itemName = data[i];
                String description = data[i + 1];
                String price = data[i + 2];
                String isAvailable = (data[i + 3]);
                String itemNumber = (data[i + 4]);

                displayInfo.add(0, itemName);
                displayInfo.add(1, description);
                displayInfo.add(2, price);
                displayInfo.add(3, isAvailable);
                displayInfo.add(4, itemNumber);

            } return displayInfo;
        }

        @Override
        public void signUpCustomer(String name, String password, String streetAndNumber, int postalCode, String placeOfResidence) throws IOException, ClassNotFoundException {
            String cmd = Commands.CMD_SIGNUP_CUSTOMER.name() + separator + name + separator + password + separator + streetAndNumber + separator + postalCode + separator + placeOfResidence;
            socketOut.println(cmd);

            String[] data = readResponse();

            if(Commands.valueOf(data[0]) != Commands.CMD_SIGNUP_CUSTOMER_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            if (!data[1].equals("Success")) {
                switch (data[1]) {
                    case "ClassNotFoundException":
                        throw new ClassNotFoundException();
                    default:
                        throw new IOException();
                }
            }
        }

        @Override
        public void signUpStaff(String name, String password) throws IOException, ClassNotFoundException, UserAlreadyExistsException {
            String cmd = Commands.CMD_SIGNUP_STAFF.name() + separator + name + separator + password;
            socketOut.println(cmd);

            String[] data = readResponse();

            if(Commands.valueOf(data[0]) != Commands.CMD_SIGNUP_STAFF_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            if (!data[1].equals("Success")) {
                switch (data[1]) {
                    case "ClassNotFoundException":
                        throw new ClassNotFoundException();
                    case "userExistent":
                        throw new UserAlreadyExistsException("");
                    default:
                        throw new IOException();
                }
            }
        }

        @Override
        public List<Map.Entry<Integer, Item>> listItemsByNumber() {
            String cmd = Commands.CMD_LIST_ITEMS_BY_NUMBER.name();
            socketOut.println(cmd);

            /* klappt nicht - vermutlich, weil Map.Entry nicht serialisierbar ist:
            //deserialize Map itemsSortedByNumber
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                List<Map.Entry<Integer, Item>> itemsSortedByNumber = (List<Map.Entry<Integer, Item>>) ois.readObject();
                return itemsSortedByNumber;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }*/

            String[] data = readResponse();

            if(Commands.valueOf(data[0]) != Commands.CMD_LIST_ITEMS_BY_NUMBER_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }

            List<Map.Entry<Integer, Item>> itemsSortedByNumber = createSortedListFromData(data);
            return itemsSortedByNumber;
        }

        @Override
        public List<Map.Entry<Integer, Item>> listItemsByName() {
            String cmd = Commands.CMD_LIST_ITEMS_BY_NAME.name();
            socketOut.println(cmd);

            /* klappt nicht - vermutlich, weil Map.Entry nicht serialisierbar ist:
            //deserialize Map itemsSortedByName
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                List<Map.Entry<Integer, Item>> itemsSortedByName = (List<Map.Entry<Integer, Item>>) ois.readObject();
                return itemsSortedByName;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }*/

            String[] data = readResponse();

            if(Commands.valueOf(data[0]) != Commands.CMD_LIST_ITEMS_BY_NAME_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }

            List<Map.Entry<Integer, Item>> itemsSortedByName = createSortedListFromData(data);

            return itemsSortedByName;

        }

        private List<Map.Entry<Integer, Item>> createSortedListFromData(String[] data){
            List<Map.Entry<Integer,Item>> entryList= new ArrayList<>();

            for (int i = 1; i < data.length; i+= 7){
                String name = data[i];
                int number = Integer.parseInt(data[i + 1]);
                String pic = data[i + 2];
                boolean isAvailable = Boolean.parseBoolean(data[i + 3]);
                double price = Double.parseDouble(data[i + 4]);
                String description = data[i + 5];
                int inStock = Integer.parseInt(data[i + 6]);

                entryList.add(new AbstractMap.SimpleEntry<>(number, new Item(name, number, pic, isAvailable, price, description, inStock)));
            }
            return entryList;
        }

        @Override
        public Map<Integer, Item> searchItemByName(String itemName) {
            String cmd = Commands.CMD_SEARCH_ITEM_BY_NAME.name() + separator + itemName;

            socketOut.println(cmd);

            /*
            //deserialize Map itemsSortedByName
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Map<Integer, Item> itemsSortedByName = (Map<Integer, Item>) ois.readObject();
                return itemsSortedByName;
            } catch (SocketTimeoutException ste){
                Map<Integer, Item> emptyMap = new HashMap<>();
                return emptyMap;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }*/

            String[] data = readResponse();
            if(Commands.valueOf(data[0]) != Commands.CMD_SEARCH_ITEM_BY_NAME_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            if (!(data.length == 1)) {
                Map<Integer, Item> result = createInventoryFromData(data);
                return result;
            }
            Map<Integer, Item> emptyMap = new HashMap<>();
            return emptyMap;
        }

        @Override
        public boolean isLoggedInAs(UserType userType) {
            String cmd = Commands.CMD_IS_LOGGEDIN_AS.name() + separator + userType;

            socketOut.println(cmd);

            String[] data = readResponse();

            // data[1] ist true oder false
            return data[1].equals("true");
        }

        @Override
        public void addItem(String itemName, int number, String pic, boolean isAvailable, double price, String description, int inStock) throws Exception {
            String cmd = Commands.CMD_ADD_ITEM.name()+ separator + itemName + separator + number + separator + pic + separator + isAvailable + separator + price + separator + description + separator + inStock;

            socketOut.println(cmd);

            String[] data = readResponse();

            if(Commands.valueOf(data[0]) != Commands.CMD_ADD_ITEM_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            if(!data[1].equals("success")){
               throw new Exception();
            }
        }

        public void increaseStock(int itemNumber, int increaseBy) throws IOException {
            String cmd = Commands.CMD_INCREASE_STOCK.name() + separator + itemNumber + separator + increaseBy;

            socketOut.println(cmd);

            String[] data = readResponse();
            if(Commands.valueOf(data[0]) != Commands.CMD_INCREASE_STOCK_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            if(!data[1].equals("success")){
                throw new IOException();
            }
        }

        @Override
        public String viewCart() throws IOException, ClassNotFoundException {
            String cmd = Commands.CMD_VIEW_CART.name();
            socketOut.println(cmd);

            System.out.println("Client cmd = " + cmd);

            String[] data = readResponse();
            System.out.println("Client data = " + Arrays.toString(data));

            if(Commands.valueOf(data[0]) != Commands.CMD_VIEW_CART_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }


            System.out.println("Client cmd resp = " + Commands.valueOf(data[0]));
            if (data.length == 1) {
                data[1] = "";
            }

            List<String> stringToList = Arrays.asList(data[1].split("_"));

            String viewCartList="";

            for (int i = 0; i < stringToList.size(); i++) {

                viewCartList += stringToList.get(i) + "\n";
                System.out.println();
            }

            return viewCartList;
        }




        @Override
        public Receipt placeOrder() throws IOException, ClassNotFoundException, NotEnoughItemInStockException {
            String cmd = Commands.CMD_PLACE_ORDER.name();
            socketOut.println(cmd);

            /*String[] data = readResponse();
            System.out.println("Client data = " + Arrays.toString(data));

            if(Commands.valueOf(data[0]) != Commands.CMD_PLACE_ORDER_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            if(data[1].equals("ModuleCommon.NotEnoughItemInStockException: -- There's not enough item in Stock! --")){
                throw new NotEnoughItemInStockException();
            } else {
                int customerNumber = Integer.parseInt(data[1]);

                String cartItemsAsString = data[2];

                String StreetAndNumber = (data[3]);
                int postalCode = Integer.parseInt(data[4]);
                String placeOfResidence = (data[5]);

                Address address = new Address(StreetAndNumber, postalCode, placeOfResidence);

                Receipt receipt = new Receipt(customerNumber, cartItemsAsString, address);
                return receipt;*/


            //deserialze Receipt

            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                Receipt receipt = (Receipt) ois.readObject();

                String receivedVersion = receipt.getCartItemsAsString();

                List<String> stringToList = Arrays.asList(receivedVersion.split("_"));

                String correctedVersion = "\n";

                for (int i = 0; i < stringToList.size(); i++) {

                    correctedVersion += stringToList.get(i) + "\n";
                    System.out.println();
                }

                receipt.setCartItemsAsString(correctedVersion);
                return receipt;
            } catch (Exception e){
                String[] data = readResponse();
                throw new NotEnoughItemInStockException();
            }
            }


        @Override
        public void logOut() {
            String cmd = Commands.CMD_LOGOUT.name();
            socketOut.println(cmd);
        }

        @Override
        public void clearCart() {
            String cmd = Commands.CMD_CLEAR_CART.name();
            socketOut.println(cmd);
        }

        @Override
        public void saveCurrentItemList() throws IOException {
            String cmd = Commands.CMD_SAVE_CURRENT_ITEMLIST.name();
            socketOut.println(cmd);
        }

        @Override
        public void deleteItem(int itemNumber) throws IOException, ClassNotFoundException {
            String cmd = Commands.CMD_DELETE_ITEM.name() + separator + itemNumber;
            socketOut.println(cmd);
        }

        @Override
        public boolean isBulkItem(int itemNumber) throws IOException, ClassNotFoundException, ItemNotExistendException {
            String cmd = Commands.CMD_IS_BULKITEM.name() + separator + itemNumber;
            socketOut.println(cmd);

            String[] data = readResponse();
            if(Commands.valueOf(data[0]) != Commands.CMD_IS_BULKITEM_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            if(!data[1].equals("true")){
                return false;
            }
            return true;
        }

        @Override
        public int getBulkSize(int itemToAdd) throws IOException, ClassNotFoundException {
            String cmd = Commands.CMD_GET_BULKSIZE.name() + separator + itemToAdd;
            socketOut.println(cmd);

            String[] data = readResponse();
            if(Commands.valueOf(data[0]) != Commands.CMD_GET_BULKSIZE_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            return Integer.parseInt(data[1]);
        }

        @Override
        public List<String> returnLogList() {
            String cmd = Commands.CMD_RETURN_LOGLIST.name();
            socketOut.println(cmd);

            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                return (List<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            /*
            String[] data = readResponse();

            if(Commands.valueOf(data[0]) != Commands.CMD_RETURN_LOGLIST_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            return createLoglistFromData(data);
            */
        }

        /* replaced by Serialization

        private List<String> createLoglistFromData(String[] data){
            List<String> logList = new ArrayList<>();
            for (int i = 1; i < data.length; i ++){
                logList.add(data[i]);
            }return logList;
        } */

        @Override
        public List<String> returnLogListItemByDate(String itemName) {
            String cmd = Commands.CMD_RETURN_LOGLISTITEM_BY_DATE.name() + separator + itemName;
            socketOut.println(cmd);

            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                return (List<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void addToCart(int itemToAdd, int itemAmount) throws IOException, ClassNotFoundException, BulkSizeException, ItemNotExistendException {
            String cmd = Commands.CMD_ADD_TO_CART.name() + separator + itemToAdd + separator + itemAmount ;
            socketOut.println(cmd);

            String[] data = readResponse();
            if(Commands.valueOf(data[0]) != Commands.CMD_ADD_TO_CART_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            if (!data[1].equals("Success")) {
                switch (data[1]) {
                    case "BulkSizeException":
                        throw new BulkSizeException();
                    case "ItemNotExistendException":
                        throw new ItemNotExistendException();
                    case "ClassNotFoundException":
                        throw new ClassNotFoundException();
                    default:
                        throw new IOException();
                }
            }
        }

        @Override
        public void increaseAmountInCart(int itemNumber, int increaseBy) throws ModuleCommon.Exceptions.ItemNotInCartException, IOException {
            String cmd = Commands.CMD_INCREASE_AMOUNT_IN_CART.name() + separator + itemNumber + separator + increaseBy ;
            socketOut.println(cmd);

            String[] data = readResponse();
            if(Commands.valueOf(data[0]) != Commands.CMD_INCREASE_AMOUNT_IN_CART_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            if (!data[1].equals("Success")) {
                switch (data[1]) {
                    case "ItemNotInCartException":
                        throw new ItemNotInCartException();
                    default:
                        throw new IOException();
                }
            }
        }

        @Override
        public String getItemsInCartToPrintThem() throws IOException, ClassNotFoundException {
            String cmd = Commands.CMD_GET_ITEMS_IN_CART_TO_PRINT_THEM.name();
            socketOut.println(cmd);

            String[] data = readResponse();
            if(Commands.valueOf(data[0]) != Commands.CMD_GET_ITEMS_IN_CART_TO_PRINT_THEM_RESP){
                throw new RuntimeException("Received invalid response to request!");
            }
            return data[1];
        }
}
