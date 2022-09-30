package ModuleCommon.valueobjects;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;


/**
 * Class represents a single webshop customer.
 * Extends User.
 * It inherits following methods from its parent class:
 * getNumber(), addToCart(), getCurrentCart
 *
 * @author Chachulski, Mathea, Malla Mirza,
 */
public class Customer extends User implements Serializable {
    private Address address = null;
    private Cart myCart = null;

    public Customer(String customerName, int customerNumber, String password, Address address) throws IOException, ClassNotFoundException {

        super(customerName, customerNumber, password);
        this.address = address;
        this.myCart = new Cart(customerNumber);
        this.userType = UserType.CUSTOMER;
    }

    public void addToCart(int addThis, int itemAmount) {
        this.myCart.getCartList().put(addThis, itemAmount);
        //nur zum testen:
        for (Map.Entry entry : this.myCart.getCartList().entrySet()) {
            System.out.println("(Only for testing purposes) item and amount: " + entry);
        }
    }

    public String viewCart() throws IOException, ClassNotFoundException {

        /*
        String cartString="";

        if (this.myCart.getCartList().isEmpty())
        {
            cartString = "Your Cart is Empty! :(";

        }
        else
        {
            for (Map.Entry entry : this.myCart.getCartList().entrySet()) {
                System.out.println("(Only for testing purposes) item and amount: " + entry);
                cartString = cartString + entry.toString() + "_";
            }

        }

        return cartString; */

        return this.myCart.getCartItemsAsString();
    }


    public void clearCart(){
        this.myCart.clearCart();
    }



    public Cart getCurrentCart(){
        return this.myCart;
    }
    public Address getAddress(){
        return this.address;
    }
}
