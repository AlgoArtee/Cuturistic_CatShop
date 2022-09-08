package ModuleClient.ui.gui.panels;

import ModuleClient.ui.gui.ShopClientGUI;
import ModuleCommon.IShop;
import ModuleCommon.Exceptions.ItemAlreadyExistsException;
import ModuleServer.domain.Shop;

import javax.swing.*;
import java.awt.*;

public class PanelAddItem extends JPanel{
    private JButton addItemButton;
    private JButton clearAllButton;
    private JTextField nameTextField = new JTextField();
    private JTextField picturePathTextField = new JTextField();
    private JTextField priceTextField = new JTextField();
    private JTextField descriptionTextField = new JTextField();
    private JTextField inStockTextField = new JTextField();

    private ShopClientGUI shopClientGUI = null;
    private IShop shop = null;

    public PanelAddItem(IShop shop, ShopClientGUI shopClientGUI) {
        super(new BorderLayout());

        this.shop = shop;

        JPanel panelAddItemForm = new JPanel();
        panelAddItemForm.setLayout(new BoxLayout(panelAddItemForm, BoxLayout.Y_AXIS));
        panelAddItemForm.setMaximumSize(new Dimension(50, 25));
        this.add(panelAddItemForm, BorderLayout.CENTER);
        //frame.setVisible(true);

        // sign-up form
        panelAddItemForm.add(new JLabel("Name:"));
        panelAddItemForm.add(nameTextField);
        panelAddItemForm.add(new JLabel("Path to the item picture:"));
        panelAddItemForm.add(picturePathTextField);
        panelAddItemForm.add(new JLabel("Attractive item price following this format '39.99' :"));
        panelAddItemForm.add(priceTextField);
        panelAddItemForm.add(new JLabel("Vivid description of the new item:"));
        panelAddItemForm.add(descriptionTextField);
        panelAddItemForm.add(new JLabel("Amount of items you wish to put in stock:"));
        panelAddItemForm.add(inStockTextField);

        clearAllButton = new JButton("Clear All Fields");
        panelAddItemForm.add(clearAllButton);

        addItemButton = new JButton("Add Item To Stock");
        panelAddItemForm.add(addItemButton);

        addItemButton.addActionListener(e -> {
            try {
                String itemName = nameTextField.getText();
                String path = picturePathTextField.getText();
                double price = Double.parseDouble(priceTextField.getText());
                String description = descriptionTextField.getText();
                int inStock = Integer.parseInt(inStockTextField.getText());
                boolean isAvailable = true;
                int number = this.shop.getItemNumber();

                shop.addItem(itemName, number,  path, isAvailable, price, description, inStock);

                clearTextFields();

                JOptionPane.showMessageDialog(null,  itemName + " sucessfully added!", "Well Done", JOptionPane.INFORMATION_MESSAGE);

            } catch (ItemAlreadyExistsException ex) {
                JOptionPane.showMessageDialog(null, "-- This Item already exists! --", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "-- please fill out every field and use only integers! --", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        clearAllButton.addActionListener(e -> {
            clearTextFields();
        });

    }
    private void clearTextFields(){
        nameTextField.setText("");
        picturePathTextField.setText("");
        priceTextField.setText("");
        descriptionTextField.setText("");
        inStockTextField.setText("");
    }
}
