package ModuleServer.persistence;

import ModuleCommon.valueobjects.Item;
import ModuleCommon.valueobjects.User;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FilePersistenceManager implements PersistenceManager, Serializable{

    private BufferedReader reader = null;
    private PrintWriter writer = null;

    public void openForReading(String file) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(file));
    }

    public void openForWriting(String file) throws IOException {
        writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
    }

    public boolean close() throws IOException {
        if (writer != null)
            writer.close();

        if (reader != null) {
            try {
                reader.close();
            }catch (IOException e) {
                return false;
            }
        }
        return true;
    }


    /**
     *
     * Method for reading Item data from an external data source
     * The availability attribute is coded in the data source (here file)
     * as "t" (true) or "f" (false)
     *
     * @return Item Object, if reading was successful

    public Item loadItem() throws IOException {

        // read Item Name
        String itemName = readData();
        if (itemName == null) {
            // no items in shop
            return null;
        }

        // read Item Number
        String itemNumber = readData();
        // ... convert to int
        int number = Integer.parseInt(itemNumber);

        // Is the Item available?
        String isAvailableCode = readData();
        // ... convert to boolean
        boolean isAvailable = isAvailableCode.equals("t") ? true : false;

        // read Item Price
        String itemPrice = readData();
        // ... convert to int
        int price = Integer.parseInt(itemPrice);


        // read Item Description
        String description = readData();

        String itemInStock = readData();
        // ... convert to int
        int inStock = Integer.parseInt(itemInStock);


        // return data as an Item object
        return new Item(itemName, number, isAvailable, price, description, inStock);
    } */

    /**
     * Method for reading Item data from an external data source
     * The availability attribute is coded as "t" (true) or "f" (false)
     *
     * @param item Item Object to save
     * @return true, if writing was successful, else false
     * */

    public boolean saveItem(Item item) throws IOException {
        // Titel, Nummer und Verfügbarkeit schreiben
        writeData(item.getItemName());

        writeData(item.getItemNumber() + "");

        if (item.getIsAvailable())
            writeData("t");
        else
            writeData("f");

        writeData(item.getItemPrice() + "");

        return true;
    }

    // --- START Log File methods ---
    public boolean writeLogEntries(List<String> entries) throws IOException {
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Log.txt"));
            oos.writeObject(entries);
            System.out.println("Log has been updated!");
            return true;
        }catch (IOException ioe){
            throw new IOException();
        }
    }

    public List<String> readLogList() throws IOException, ClassNotFoundException{
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Log.txt"));
            List<String> currentLogList = (List<String>) ois.readObject();

            // Test if items readable from file
            //List<Map.Entry<Integer, Item>> l_test = new ArrayList<>(currentItemList.entrySet());

            //for (var e : l_test) {
            //    System.out.println(e.getKey() + ":\t" + e.getValue().getItemName() + ":\t" + e.getValue().getDescription());
            //    System.out.println("Price: " + e.getValue().getItemPrice());
            //    System.out.println("Item ID (Needed for Adding the Item to Cart!) : " + e.getValue().getItemNumber());
            //    System.out.println();
            //}
            // End Test items readable from file

            return currentLogList;

        }catch (IOException e){
            throw new IOException();
        }catch (ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }
    }


    // --- START Item file methods ---
    public boolean writeItemList(Map<Integer, Item> itemList) throws IOException {
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Items.bin"));
            oos.writeObject(itemList);
            System.out.println("Item List updated!");
            return true;
        }catch (IOException ioe){
            throw new IOException();
        }
    }

    public Map<Integer, Item> readItemList() throws IOException, ClassNotFoundException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Items.bin"));
            Map<Integer, Item> currentItemList = (HashMap<Integer, Item>) ois.readObject();

            // Test if items readable from file
            //List<Map.Entry<Integer, Item>> l_test = new ArrayList<>(currentItemList.entrySet());

            //for (var e : l_test) {
            //    System.out.println(e.getKey() + ":\t" + e.getValue().getItemName() + ":\t" + e.getValue().getDescription());
            //    System.out.println("Price: " + e.getValue().getItemPrice());
            //    System.out.println("Item ID (Needed for Adding the Item to Cart!) : " + e.getValue().getItemNumber());
            //    System.out.println();
            //}
            // End Test items readable from file

            return currentItemList;

        }catch (IOException e){
            throw new IOException();
        }catch (ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }
    }
    // // --- END Item file methods ---


    public boolean writeUserList(Map<String, User> userList) throws IOException {
       try{
           ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Users.bin"));
           oos.writeObject(userList);
           return true;
       }catch (IOException ioe){
           throw new IOException();
       }
    }

    public Map<String, User> readUserList() throws IOException, ClassNotFoundException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Users.bin"));
            Map<String, User> currentUserList = (HashMap<String, User>) ois.readObject();
            return currentUserList;
        }catch (IOException e){
            throw new IOException();
        }catch (ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }
    }

    private String readData() throws IOException {
        if (reader != null)
            return reader.readLine();
        else
            return "";
    }

    private void writeData(String daten) {
        if (writer != null)
            writer.println(daten);
    }

}
