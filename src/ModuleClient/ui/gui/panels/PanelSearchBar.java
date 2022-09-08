package ModuleClient.ui.gui.panels;

import ModuleCommon.IShop;
import ModuleClient.ui.gui.ItemTableModel;
import ModuleClient.ui.gui.ShopClientGUI;
import ModuleCommon.valueobjects.Item;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

/**
 * This class implements a search bar as a JPanel with FlowLayout
 */
public class PanelSearchBar extends JPanel {

    // PanelSearchBar uses this interface (implemented as an inner class) to let the registered Listeners know that
    // a search has been processed. The interface is implemented by the ShopClientGUI.
    public interface SearchListener {
        void onSearchedFor(Map<Integer, Item> searchResultList);
    }
    private IShop shop = null;
    private ShopClientGUI shopClientGUI = null;
    private SearchListener listener = null;
    private JTextField searchTextField = new JTextField();
    public JButton searchNameButton = new JButton("Search");
    private JButton searchNumberButton = new JButton("Search");
    private JButton refreshButton = new JButton("Refresh");
    private ItemTableModel itemTableModel;

    public PanelSearchBar(IShop shop, SearchListener listener, ShopClientGUI shopClientGUI){
        super(new FlowLayout());
        this.shop = shop;
        this.listener = listener;
        this.shopClientGUI = shopClientGUI;
        add(new JLabel("Item Name:"));
        searchTextField.setPreferredSize(new Dimension(200, 30));
        add(searchTextField);
        add(searchNameButton);
        add(refreshButton);

        // the event is defined right here through a lambda notiation
        searchNameButton.addActionListener(e -> {
            String itemName = searchTextField.getText();

            // TODO: potentially check if integer and switch functions
            Map<Integer, Item> resultList = shop.searchItemByName(itemName);

            if(resultList.isEmpty()){
                try {
                    resultList = shop.getInventory();
                    JOptionPane.showMessageDialog(null, "There were no results for your query! :( \n Try Again. :)", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }

            listener.onSearchedFor(resultList);

            //this.itemTableModel = new ItemTableModel(resultList);
            //clearTextFields();
            //shopClientGUI.getCardLayout().show(shopClientGUI.getCardPanel(), "1");
            //shopClientGUI.getCardPanel().validate();
        });

        refreshButton.addActionListener(e -> {
            Map<Integer, Item> resultList= null;
            try {
                resultList = shop.getInventory();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            listener.onSearchedFor(resultList);
        });






        /*
        add(new JLabel("item number"));
        searchTextField.setPreferredSize(new Dimension(200, 30));
        add(searchTextField);
        add(searchNumberButton);

        searchNumberButton.addActionListener(e -> {
            int itemNumber = Integer.parseInt(searchTextField.getText());
            java.util.List<Item> resultList = shop.searchItemByNumber(itemNumber);

            if(resultList.isEmpty()){
                resultList = shop.getInventory();
            }
            listener.onSearchedFor(resultList);
        }); */

        setBorder(BorderFactory.createTitledBorder("Search"));
    }



    private void clearTextFields(){
        searchTextField.setText("");
    }
}
