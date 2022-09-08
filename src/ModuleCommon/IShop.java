package ModuleCommon;

import ModuleCommon.valueobjects.Item;
import ModuleCommon.valueobjects.Receipt;
import ModuleCommon.valueobjects.UserType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IShop {


    Map<Integer, Item> getInventory() throws IOException, ClassNotFoundException;

    int getItemNumber();

    void logIn(String name, String password) throws ModuleCommon.Exceptions.PasswordNotExistendException, ModuleCommon.Exceptions.NameNotExistendException, ClassNotFoundException, IOException;

    List<String> displayInfo(String itemName);

    void signUpCustomer(String name, String password, String streetAndNumber, int postalCode, String placeOfResidence) throws IOException, ClassNotFoundException;

    void signUpStaff(String name, String password) throws IOException, ClassNotFoundException, ModuleCommon.Exceptions.UserAlreadyExistsException;

    List<Map.Entry<Integer,Item>> listItemsByNumber();

    List<Map.Entry<Integer,Item>> listItemsByName();

    Map<Integer ,Item> searchItemByName(String itemName);

    boolean isLoggedInAs(UserType userType);

    void addItem(String itemName, int number, String pic, boolean isAvailable, double price, String description, int inStock) throws Exception;

    List<String> returnLogList();

    // Display Item Log by Date
    List<String> returnLogListItemByDate(String itemName);

    void addToCart(int itemToAdd, int itemAmount) throws ModuleCommon.Exceptions.ItemNotExistendException, IOException, ClassNotFoundException, ModuleCommon.Exceptions.BulkSizeException;

    void increaseAmountInCart(int itemNumber, int increaseBy) throws ModuleCommon.Exceptions.ItemNotInCartException, IOException;

    String getItemsInCartToPrintThem() throws IOException, ClassNotFoundException;

    void increaseStock(int itemNumber, int increaseBy) throws IOException, ClassNotFoundException;

    String viewCart() throws IOException, ClassNotFoundException;

    Receipt placeOrder() throws ModuleCommon.Exceptions.NotEnoughItemInStockException, IOException, ClassNotFoundException;

    void logOut();

    void clearCart();

    void saveCurrentItemList() throws IOException;

    void deleteItem(int itemNumber) throws IOException, ClassNotFoundException;

    boolean isBulkItem(int itemNumber) throws IOException, ClassNotFoundException, ModuleCommon.Exceptions.ItemNotExistendException;

    int getBulkSize(int itemToAdd) throws IOException, ClassNotFoundException;
}
