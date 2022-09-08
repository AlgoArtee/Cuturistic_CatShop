package ModuleClient.ui.gui.panels;

import ModuleCommon.IShop;
import ModuleClient.ui.gui.ShopClientGUI;
import ModuleCommon.valueobjects.Customer;
import ModuleCommon.valueobjects.Item;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PanelCustomerShop extends JPanel implements PanelSearchBar.SearchListener, PanelCustomerShopItemFunctions.CustomerItemFunctionsListener {
    private IShop shop = null;
    private PanelSearchBar panelSearchBar;
    private PanelItemDisplay panelItemDisplay;
    private PanelCustomerShopTop panelCustomerShopTop;
    private PanelCustomerShopItemFunctions panelCustomerShopItemFunctions;
    private Customer customer;

    public PanelCustomerShop(IShop shop, ShopClientGUI shopClientGUI) throws IOException, ClassNotFoundException {
        super(new BorderLayout());
        this.shop = shop;

        panelCustomerShopTop = new PanelCustomerShopTop(shop, shopClientGUI);
        add(panelCustomerShopTop, BorderLayout.NORTH);
        panelItemDisplay = new PanelItemDisplay(shop);
        add(panelItemDisplay, BorderLayout.CENTER);
        panelSearchBar = new PanelSearchBar(shop, this, shopClientGUI);
        add(panelSearchBar, BorderLayout.SOUTH);
        panelCustomerShopItemFunctions = new PanelCustomerShopItemFunctions(shop,this);
        add(panelCustomerShopItemFunctions, BorderLayout.WEST);

        // Border Style
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    public void onSearchedFor(Map<Integer, Item> searchResultList) {
        ArrayList<Item> searchResult = new ArrayList<>(searchResultList.values());
        panelItemDisplay.setInventory(searchResult);
    }

    @Override
    public void onSortBy(List<Map.Entry<Integer, Item>> sortedResultList) {
        ArrayList<Item> sortedResult = new ArrayList<>();
        sortedResultList.forEach(i->{sortedResult.add(i.getValue());});
        panelItemDisplay.setInventory(sortedResult);
    }

    @Override
    public void onViewCart(ShopClientGUI shopClientGUI){
        shopClientGUI.getCardLayout().show(shopClientGUI.getCardPanel(), "5");
    }

    /*private class ImageRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int colum){
            String photoName = value.toString();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/itemresources/"+photoName).
                    getImage().getScaledInstance(60,60, Image.SCALE_DEFAULT));
            return new JLabel(imageIcon);
        }
    }*/
}
