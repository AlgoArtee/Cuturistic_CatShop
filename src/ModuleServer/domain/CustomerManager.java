package ModuleServer.domain;

import ModuleCommon.Exceptions.ItemNotInCartException;
import ModuleCommon.Exceptions.UserAlreadyExistsException;
import ModuleCommon.valueobjects.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ModuleCommon.valueobjects.LogInPhase.LOGGED_IN;

/**
 * Class for the management of webshop customers.
 * Extends UserManager
 * It inherits following methods from its parent class:
 * getUserList(), getLastNumber(), createNextNumber(), checkPassword().
 *
 * @author Chachulski, Mathea
 */
public class CustomerManager extends UserManager implements Serializable {


    public CustomerManager() throws IOException, ClassNotFoundException {
        try {
            //addFirstCustomers();
            userList = readUserList();


        }catch (IOException ioe){
            throw new IOException();
        }
    }

    private Address address1(){
        return new Address("Mainstreet 4", 8498, "Duckburg");
    }
    private Address address2(){
        return new Address("Prisonroad 90", 8351, "Duckburg");
    }
    private Address address3(){
        return new Address("Highway To Hell 6", 8199, "Duckburg");
    }

    // zu Testzwecken, solange die Persistenz-Schicht noch nicht aktiv ist
    private void addFirstCustomers() throws IOException, ClassNotFoundException {
        User Minni = new Customer("Minni Mouse", createNextNumber(), "Mickey", address1());
        this.userList.put("Minni Mouse", Minni);
        User Karlo = new Customer("Kater Karlo", createNextNumber(), "Mickey", address2());
        this.userList.put("Kater Karlo", Karlo);
        User Donald = new Customer("Donald Duck", createNextNumber(), "Mickey", address3());
        this.userList.put("Donald Duck", Donald);
        this.persistenceManager.writeUserList(this.userList);
    }

    private Map<String, User> readUserList() throws IOException, ClassNotFoundException {
        try {
            return this.persistenceManager.readUserList();
        }catch (IOException ioe){
            throw new IOException();
        }catch (ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }
    }

    protected void addCustomer(String name, String password, Address address) throws UserAlreadyExistsException, IOException, ClassNotFoundException {
        User newCustomer = new Customer(name, createNextNumber(), password, address);
        try {
            Map<String, User> currentUserList = this.persistenceManager.readUserList();
            if (!currentUserList.containsKey(name)) {
                currentUserList.put(name, newCustomer);
                this.persistenceManager.writeUserList(currentUserList);
                SessionState.onlineUsers.add(newCustomer);
            } else {
                throw new UserAlreadyExistsException(name);
            }
        }catch (IOException ioe){
            throw new IOException();
        }catch (ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }
    }

    // This is where the real signup process for customers takes place.
    protected void processCustomerSignupInfo(String newName, String password, String streetAndNumber, int postalCode, String placeOfResidence) throws UserAlreadyExistsException, IOException, ClassNotFoundException {
        Address address = new Address(streetAndNumber, postalCode, placeOfResidence);
        try {
            addCustomer(newName, password, address);
            SessionState.logInPhase = LOGGED_IN;
            SessionState.userType = UserType.CUSTOMER;

            // Investigate:
            setCurrentUser(getCurrentUser(newName));
        } catch (UserAlreadyExistsException uaee) {
            throw new UserAlreadyExistsException(newName);
        }catch (IOException ioe){
            throw new IOException();
        }catch (ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }
    }

    protected void addToCart(int itemToAdd, int itemAmount){

        getCurrentCustomer().addToCart(itemToAdd, itemAmount);

    }


    public void increaseAmountInCart(int itemNumber, int increaseBy) throws ItemNotInCartException {
        getCurrentCustomer().getCurrentCart().increaseItemAmount(itemNumber, increaseBy);
    }

    public String getItemsInCartToPrintThem() throws IOException, ClassNotFoundException {
        try {
            return getCurrentCustomer().viewCart();
        }catch (IOException ioe) {
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////
    // Investigate:

    public String viewCart() throws IOException, ClassNotFoundException {
        try {

            return getCurrentCustomer().viewCart();

        }catch (IOException ioe) {
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }
    public void clearCart(){

            getCurrentCustomer().clearCart();

    }


    private Customer getCurrentCustomer(){



        String userThread = Thread.currentThread().getName();
        System.out.println(userThread);

        int onlineUserNumber = Integer.parseInt(userThread.replaceAll("\\D+",""));
        System.out.println(onlineUserNumber);

        SessionState.currentUser = SessionState.onlineUsers.get(onlineUserNumber);
        System.out.println(SessionState.currentUser.getName());

        return (Customer) SessionState.currentUser;
    }
    protected void setCurrentUser(User user){



        SessionState.currentUser = user;
    }
    private User getCurrentUser(String name) throws IOException, ClassNotFoundException {
        return getCurrentUserList().get(name);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////



    private Map<String, User> getCurrentUserList() throws IOException, ClassNotFoundException {
        //Map<String, User> currentUserList = this.persistenceManager.readUserList();
        //return currentUserList;
        return this.userList;
    }

}
