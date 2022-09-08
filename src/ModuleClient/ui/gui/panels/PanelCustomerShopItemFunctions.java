package ModuleClient.ui.gui.panels;

import ModuleCommon.*;
import ModuleClient.ui.gui.ShopClientGUI;
import ModuleCommon.Exceptions.BulkSizeException;
import ModuleCommon.Exceptions.ItemNotExistendException;
import ModuleCommon.Exceptions.ItemNotInCartException;
import ModuleCommon.Exceptions.NotEnoughItemInStockException;
import ModuleCommon.valueobjects.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PanelCustomerShopItemFunctions extends JPanel {

    // adding Listener interface
    public interface CustomerItemFunctionsListener {

        void onSortBy(List<Map.Entry<Integer, Item>> sortedResultList);
        void onViewCart(ShopClientGUI shopClientGUI);
    }

    private IShop shop = null;
    private CustomerItemFunctionsListener listener = null;

    // Components

    // TODO: Review potential of combobox
    private JTextField itemSelectedTextField = new JTextField();
    private JTextField amountTextField = new JTextField();
    private JTextField itemToIncreaseTextField = new JTextField();
    private JTextField increaseByTextField = new JTextField();
    private JButton sortByNameButton = new JButton("Sort by Name");
    private JButton sortByNumberButton = new JButton("Sort by Number");
    private JButton addToCartButton = new JButton("Add to Cart");
    private JButton viewCartButton = new JButton("View Cart");
    private JButton increaseAmountButton = new JButton("Increase amount in cart");
    private JButton clearCartButton = new JButton("Clear cart");
    private JButton orderButton = new JButton("Buy now!");

    public PanelCustomerShopItemFunctions(IShop shop, CustomerItemFunctionsListener listener){

        super(new GridBagLayout());
        this.shop = shop;
        this.listener = listener;

        // Set general Dimension of Components
        Dimension componentSize = new Dimension(140,30);

        // Orient components north-wise
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;

        // Sort by Name
        sortByNameButton.setPreferredSize(componentSize);
        c.gridx = 0;
        c.gridy = 0;
        add(sortByNameButton, c);
        sortByNameButton.addActionListener(e -> sortByName(e));

        // Sort by Item Number
        sortByNumberButton.setPreferredSize(componentSize);
        //c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 1;
        add(sortByNumberButton, c);
        sortByNumberButton.addActionListener(e -> sortByNumber(e));

        // Add Label Item Number
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 2;
        add(new JLabel("Enter Item Number:"),c);

        // Enter Item Number
        itemSelectedTextField.setPreferredSize(componentSize);
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 3;
        add(itemSelectedTextField,c);

        // Add Label Item Amount
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 4;
        add(new JLabel("Enter Wanted Amount:"),c);

        // Enter Amount
        amountTextField.setPreferredSize(componentSize);
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 5;
        add(amountTextField,c);

        // Add Item
        addToCartButton.setPreferredSize(componentSize);
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 6;
        add(addToCartButton, c);

            addToCartButton.addActionListener(e -> {
                try {
                    addToCart(e);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Fields cannot be empty and must contain integers only!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

        // View Cart
        viewCartButton.setPreferredSize(componentSize);
        //c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 7;
        add(viewCartButton, c);
        viewCartButton.addActionListener(e -> {
            try {
                viewCart(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Add Label Item to increase
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 8;
        add(new JLabel("increase (item#):"),c);

        // Enter Amount
        itemToIncreaseTextField.setPreferredSize(componentSize);
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 9;
        add(itemToIncreaseTextField,c);

        // Add Label Amount to increase by
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 10;
        add(new JLabel("amount to increase by:"),c);

        // Enter Amount
        increaseByTextField.setPreferredSize(componentSize);
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 11;
        add(increaseByTextField,c);

        // Increase Item Amount in Cart
        increaseAmountButton.setPreferredSize(componentSize);
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 12;
        add(increaseAmountButton, c);
        increaseAmountButton.addActionListener(e -> {
            try {
                increaseAmount(e);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Fields cannot be empty and must contain integers only!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });

        // Clear Cart
        clearCartButton.setPreferredSize(componentSize);
        //c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 13;
        add(clearCartButton, c);
        clearCartButton.addActionListener(e -> {
                clearCart(e);
        });

        // Place Order
        orderButton.setPreferredSize(componentSize);
        //c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 14;
        add(orderButton, c);
        orderButton.addActionListener(e -> {
            try {
                placeOrder(e);
            } catch (NotEnoughItemInStockException ignored){
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Define Border
        this.setBorder(BorderFactory.createTitledBorder("Menu"));
    }

    private void addToCart(ActionEvent e) {

        if(!e.getSource().equals(addToCartButton))
            return;

            int itemID = Integer.parseInt(itemSelectedTextField.getText());
            int amount = Integer.parseInt(amountTextField.getText());
            try {
                /*if (shop.isBulkItem(itemID)) {
                    int bulkSize = shop.getBulkSize(itemID);
                    if (amount % bulkSize != 0) {
                        JOptionPane.showMessageDialog(null, "This item is sold in units of " + bulkSize + ". \n How many items would you like to add to your cart?", "There's a catch...", JOptionPane.ERROR_MESSAGE);
                        amountTextField.setText("");
                    }else {*/
                        try {
                            shop.addToCart(itemID, amount);
                            JOptionPane.showMessageDialog(null, "The goods are now waiting for you in your cart!", "Added", JOptionPane.INFORMATION_MESSAGE);
                            clearTextFields();
                        } catch (BulkSizeException bse){
                            int bulkSize = shop.getBulkSize(itemID);
                            JOptionPane.showMessageDialog(null, "This item is sold in units of " + bulkSize + ". \n How many items would you like to add to your cart?", "There's a catch...", JOptionPane.ERROR_MESSAGE);
                            //}
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "-- Failed to process Data correctly --", "Error", JOptionPane.ERROR_MESSAGE);
                            clearTextFields();
                        } /*else {
                    try {
                        shop.addToCart(itemID, amount);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "-- Failed to process Data correctly --", "Error", JOptionPane.ERROR_MESSAGE);
                        clearTextFields();
                    }
                    JOptionPane.showMessageDialog(null, "The goods are now waiting for you in your cart!", "Added", JOptionPane.INFORMATION_MESSAGE);
                    clearTextFields(); */
                //}
            } catch (IOException | ClassNotFoundException ioe) {
                JOptionPane.showMessageDialog(null, "-- Failed to process Data correctly --", "Error", JOptionPane.ERROR_MESSAGE);
                clearTextFields();
            } /* catch (ItemNotExistendException ex) {
                JOptionPane.showMessageDialog(null, "-- This Item doesn't exist! --", "Error", JOptionPane.ERROR_MESSAGE);
                clearTextFields();
            } */
    }

    private void viewCart(ActionEvent e) throws IOException, ClassNotFoundException {
        if(!e.getSource().equals(viewCartButton))
            return;

        String cartContent = shop.viewCart();

        if (!cartContent.equals("\n Cart is empty") && !cartContent.equals(" ")){

        String contentMessage = "Your Cart:" + "\n" + cartContent;
        JOptionPane.showMessageDialog(null, contentMessage, "Information", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(null, "Your cart is empty :(", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void increaseAmount(ActionEvent e) {
        int itemNumber = Integer.parseInt(itemToIncreaseTextField.getText());
        int amountToIncreaseBy = Integer.parseInt(increaseByTextField.getText());

        try {
            if (shop.isBulkItem(itemNumber)) {
                int bulkSize = shop.getBulkSize(itemNumber);
                JOptionPane.showMessageDialog(null, "\"This item is sold in units of \" + bulkSize + \". \\n \" +\n" +
                        "                        \"By how many would you like to increase the amount of this item in your cart? \\n \" +\n" +
                        "                        \"(Must be a multiple of \" + bulkSize + \" .)\"", "There's a catch...", JOptionPane.ERROR_MESSAGE);
            } else {
                clearTextFields();
            }
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "-- Failed to process Data correctly --", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ItemNotExistendException ex) {
            JOptionPane.showMessageDialog(null, "-- This item doesn't exist! --", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            shop.increaseAmountInCart(itemNumber, amountToIncreaseBy);
            JOptionPane.showMessageDialog(null, "added " + amountToIncreaseBy + " more - way to go!", "Yeay", JOptionPane.INFORMATION_MESSAGE);
        }catch (ItemNotInCartException itemNotInCartException){
            JOptionPane.showMessageDialog(null, "You don't have this item in your cart!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Something went wrong, sorry!", "Error", JOptionPane.ERROR_MESSAGE);

        }
        clearTextFields();
    }

    private void sortByName(ActionEvent e){
        if(!e.getSource().equals(sortByNameButton))
            return;

        try {
            List<Map.Entry<Integer, Item>> sortedList = shop.listItemsByName();
            listener.onSortBy(sortedList);
        } catch (Exception ex) {
            System.out.println("Something went wrong!");
        }
    }

    private void sortByNumber(ActionEvent e){
        if(!e.getSource().equals(sortByNumberButton))
            return;

        try {
            List<Map.Entry<Integer, Item>> sortedList = shop.listItemsByNumber();
            listener.onSortBy(sortedList);
        } catch (Exception ex) {
            System.out.println("Something went wrong!");
        }
    }

    private void clearCart(ActionEvent e){
        shop.clearCart();
        JOptionPane.showMessageDialog(null, "All gone... time for a fresh start!", "Yikes...", JOptionPane.INFORMATION_MESSAGE);
    }

    private void placeOrder(ActionEvent e) throws IOException, ClassNotFoundException, NotEnoughItemInStockException {
        try {
            String receipt = shop.placeOrder().getCartItemsAsString();
            JOptionPane.showMessageDialog(null, "Your Receipt: " + receipt, "Information", JOptionPane.INFORMATION_MESSAGE);
        }catch (NotEnoughItemInStockException notEnoughItemInStockException){
            JOptionPane.showMessageDialog(null, "-- Bohooo, there's not enough item in Stock :( --", "Error", JOptionPane.ERROR_MESSAGE);
        }/*catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }*/
    }

    private void clearTextFields(){
        itemSelectedTextField.setText("");
        amountTextField.setText("");
        itemToIncreaseTextField.setText("");
        increaseByTextField.setText("");
    }
}
