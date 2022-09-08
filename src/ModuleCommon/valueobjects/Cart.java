package ModuleCommon.valueobjects;

import ModuleCommon.Exceptions.ItemNotInCartException;
import ModuleServer.domain.ItemManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class represents a cart which handels webshop items inside a CartList.
 * It belongs to a specific customer and is invoked at customer login.
 * Customers can add items they want to buy, increase the amount of items, buy them and clear their cart.
 *
 * @author Chachulski, Mathea
 */

public class Cart implements Serializable {
    // cartList: key = itemNumber, value = Amount
    private Map<Integer, Integer> cartList = new HashMap<>();
    private ItemManager itemManager;
    private int customerNumber = 0;
    public Map<String, String> itemNameQuantity = new HashMap<>();
    //private IShop shop;

    public Cart(int customerNumber) throws IOException, ClassNotFoundException {
        this.customerNumber = customerNumber;
        this.itemManager = new ItemManager();
    }

    public Map<Integer, Integer> getCartList() {
        return this.cartList;
    }

    public void increaseItemAmount(int itemNumber, int increaseBy) throws ItemNotInCartException {
        if (this.cartList.containsKey(itemNumber)) {
            int newAmount = this.cartList.get(itemNumber) + increaseBy;
            this.cartList.put(itemNumber, newAmount);
        }else throw new ItemNotInCartException();
    }

     private String calculateTotal() throws IOException, ClassNotFoundException {
        double total = 0;
        String totalAsString = "";
        try {
            java.text.DecimalFormat format = new java.text.DecimalFormat("#.00");
            for (int itemNumberInCart : this.cartList.keySet()) {
                for (int itemNumberInStock : this.itemManager.getItemList().keySet()) {
                    if (itemNumberInCart == itemNumberInStock) {
                        total += this.cartList.get(itemNumberInCart) * this.itemManager.getItemList().get(itemNumberInStock).getItemPrice();
                    }
                }
            }
            return totalAsString = format.format(total);
        } catch (IOException ioe) {
            throw new IOException();
        } catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }

    /**
     * Returns the quantity of a particular item inside the cart, the items name
     * and price per item.
     */
    public String getCartItemsAsString() throws IOException, ClassNotFoundException {

        this.itemManager = new ItemManager();

        //StringBuilder itemsInCart = new StringBuilder();
        String itemsInCart = "";

        if (this.cartList.isEmpty()) {
            return "Cart is empty. :(";
        }

        try {
            for (int itemNumberInCart : this.cartList.keySet()) {
                for (Map.Entry mapEntry : this.itemManager.getItemList().entrySet()) {
                    int itemNumberInStock = (int) mapEntry.getKey();
                    if (itemNumberInCart == itemNumberInStock) {
                        if (itemNameQuantity.containsKey(itemNumberInCart)){
                        }else{
                            itemNameQuantity.put(Integer.toString(this.cartList.get(itemNumberInCart)),this.itemManager.getItemList().get(itemNumberInStock).getItemName());
                        }

                        /*
                        itemsInCart.append(this.itemManager.getItemList().get(itemNumberInStock).toString())
                                .append(" - quantity: ").append(cartList.get(itemNumberInCart))
                                .append("\n"); */

                        itemsInCart += this.itemManager.getItemList().get(itemNumberInStock).toString()
                                + (" - quantity: ")+ (cartList.get(itemNumberInCart) + "_");
                    }
                }
            }
            //return itemsInCart.append(" \n").append("Price in total: ").append(calculateTotal()).append("\n").toString();
            itemsInCart += " Price in total: "+ calculateTotal();

            System.out.println("In method return: " + itemsInCart);
            return itemsInCart;

        } catch (IOException ioe) {
            throw new IOException();
        } catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }

    }

    public void clearCart() {
        cartList.clear();
    }

    public Receipt buyNow() throws IOException, ClassNotFoundException {
        try {
            Receipt receipt = new Receipt(customerNumber, getCartItemsAsString(), getCustomerAddress());
            clearCart();
            return receipt;
        }catch(IOException ioe){
            throw new IOException();
        }catch(ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }
    }

    /**
     * This method casts the current user back into a customer object in order to access its address.
     *
     */
    public Address getCustomerAddress(){
        Customer currentCustomer = (Customer) ModuleServer.domain.SessionState.currentUser;
        return currentCustomer.getAddress();
    }

    public Map<String, String> getItemNameQuantity(){
        return itemNameQuantity;
    }
}
