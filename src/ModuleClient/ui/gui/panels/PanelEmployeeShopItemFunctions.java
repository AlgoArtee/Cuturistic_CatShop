package ModuleClient.ui.gui.panels;

import ModuleClient.ui.gui.ShopClientGUI;
import ModuleCommon.IShop;
import ModuleClient.ui.gui.LogTableModel;
import ModuleCommon.Exceptions.ItemNotInCartException;
import ModuleCommon.valueobjects.Item;
import ModuleServer.domain.Shop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PanelEmployeeShopItemFunctions extends JPanel {

    // adding Listener interface
    public interface EmployeeItemFunctionsListener {

        void onSortBy(List<Map.Entry<Integer, Item>> sortedResultList);
    }
    private ShopClientGUI shopClientGUI = null;
    private IShop shop = null;
    private EmployeeItemFunctionsListener listener = null;

    // Components

    // TODO: Review potential of combobox
    private JButton sortByNameButton = new JButton("Sort by Name");
    private JButton sortByNumberButton = new JButton("Sort by Number");
    private JTextField itemSelectedTextField = new JTextField();
    private JTextField amountTextField = new JTextField();

    private JTextField nameFilterLog = new JTextField();

    private JButton increaseStock = new JButton("Increase Stock");
    private JButton addNewItemButton = new JButton("Add New Item");
    private JButton increaseAmountButton = new JButton("Increase Stock");
    private JButton addStaffButton = new JButton("Add Staff");
    private JButton displayLogButton = new JButton("Display Log");
    private JButton displayLogIDButton = new JButton("Item Log");

    private JButton displayItemStockButton = new JButton("Display Item Stock");

    protected LogTableModel logTableModel;
    private JTable logTable;

    public PanelEmployeeShopItemFunctions(IShop shop, EmployeeItemFunctionsListener listener){

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
        c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 1;
        add(sortByNumberButton, c);
        sortByNumberButton.addActionListener(e -> sortByNumber(e));

        // Add Item
        addNewItemButton.setPreferredSize(componentSize);
        c.gridx = 0;
        c.gridy = 2;
        add(addNewItemButton, c);
        addNewItemButton.addActionListener(e -> addNewItem(e));

        // Add Label Increase Stock
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 3;
        add(new JLabel("Increase Stock:"),c);

        // Add Label Item Number
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 4;
        add(new JLabel("Item Number"),c);

        // Enter Item Number
        itemSelectedTextField.setPreferredSize(componentSize);
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 5;
        add(itemSelectedTextField,c);

        // Add Label Item Amount
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 6;
        add(new JLabel("Amount to increase by"),c);

        // Enter Amount
        amountTextField.setPreferredSize(componentSize);
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 7;
        add(amountTextField,c);

        // Increase Item Amount in Cart
        increaseAmountButton.setPreferredSize(componentSize);
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 8;
        add(increaseAmountButton, c);
        increaseAmountButton.addActionListener(e -> {
                    try {
                        increaseAmount(e);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "Fields cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    }catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }
                });

        // Add Staff
        addStaffButton.setPreferredSize(componentSize);
        c.gridx = 0;
        c.gridy = 9;
        add(addStaffButton, c);
        addStaffButton.addActionListener(e -> addStaff(e));

        // Display Log
        displayLogButton.setPreferredSize(componentSize);
        c.gridx = 0;
        c.gridy = 10;
        add(displayLogButton, c);
        displayLogButton.addActionListener(e -> displayLog(e));

        // Display the Log sorted by Item by date
        // --- Enter Item Name
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 11;
        add(new JLabel("Search Log by Item: "),c);

        // --- Enter Item Name
        nameFilterLog.setPreferredSize(componentSize);
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 12;
        add(nameFilterLog,c);

        // --- Button for Displaying Log for Item
        displayLogIDButton.setPreferredSize(componentSize);
        //c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 13;
        add(displayLogIDButton, c);
        displayLogIDButton.addActionListener(e -> displayLogID(e));



        // Display Item Stock
        displayItemStockButton.setPreferredSize(componentSize);
        c.gridx = 0;
        c.gridy = 14;
        add(displayItemStockButton, c);
        displayItemStockButton.addActionListener(e -> {
            try {
                displayItemStock(e);
            } /*catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(null, "Please fill out every field and use only integers!", "Error", JOptionPane.ERROR_MESSAGE);

            }*/ catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });


        // Define Border
        this.setBorder(BorderFactory.createTitledBorder("Menu"));




    }

    private void displayLogID(ActionEvent e) {

        if (!e.getSource().equals(displayLogIDButton))
            return;

        try {

            String itemName = nameFilterLog.getText();

            if (itemName.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter the correct name of the item you are looking for!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {

                LogDetails logDetails = new LogDetails(shop.returnLogListItemByDate(itemName));

                System.out.println("Displaying Log for Item by Date ...");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Please enter the correct name of the item you are looking for!", "Error", JOptionPane.ERROR_MESSAGE);
        }  catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void displayItemStock(ActionEvent e) throws IOException, ClassNotFoundException {
        if (!e.getSource().equals(displayItemStockButton))
            return;
        try {

            int itemNumber = Integer.parseInt(itemSelectedTextField.getText());

            int amountInStock = shop.getInventory().get(itemNumber).getInStock();

            JOptionPane.showMessageDialog(null, "In Stock: " + amountInStock, "Items In Stock", JOptionPane.INFORMATION_MESSAGE);

        }catch (NumberFormatException nfe){
            JOptionPane.showMessageDialog(null, "Please enter the number of the item you are looking for!!", "Error", JOptionPane.ERROR_MESSAGE);
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void displayLog(ActionEvent e) {

        if(!e.getSource().equals(displayLogButton))
            return;

        try {

            LogDetails logDetails = new LogDetails(shop.returnLogList());

            System.out.println("Displaying Log ...");

        } catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addNewItem(ActionEvent e) {

        if(!e.getSource().equals(addNewItemButton))
            return;
        // Formular in neuem Fenster anzeigen
        AddItem addItems = new AddItem(shop, shopClientGUI);
    }

    private void addStaff(ActionEvent e){

        if(!e.getSource().equals(addStaffButton))
            return;
        // Formular in neuem Fenster anzeigen
        AddStaff addstaff = new AddStaff(shop, shopClientGUI);
    }

    private void sortByName(ActionEvent e){

        if(!e.getSource().equals(sortByNameButton))
            return;

        try {
            List<Map.Entry<Integer, Item>> sortedList = shop.listItemsByName();
            listener.onSortBy(sortedList);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sortByNumber(ActionEvent e){

        if(!e.getSource().equals(sortByNumberButton))
            return;

        try {
            List<Map.Entry<Integer, Item>> sortedList = shop.listItemsByNumber();
            listener.onSortBy(sortedList);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void increaseAmount(ActionEvent e) {
        int itemNumber = Integer.parseInt(itemSelectedTextField.getText());
        int amountToIncreaseBy = Integer.parseInt(amountTextField.getText());
        try {
            shop.increaseStock(itemNumber, amountToIncreaseBy);
        } catch (Exception exception){
            JOptionPane.showMessageDialog(null, "Something's wrong. Does this item exist?", "Error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, "Stock is updated", "Well Done!", JOptionPane.INFORMATION_MESSAGE);
        clearTextFields();
    }

    private void clearTextFields(){
        itemSelectedTextField.setText("");
        amountTextField.setText("");
        itemSelectedTextField.setText("");
        amountTextField.setText("");
    }

}
