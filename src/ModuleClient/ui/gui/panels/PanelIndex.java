package ModuleClient.ui.gui.panels;

import ModuleCommon.IShop;
import ModuleClient.ui.gui.ShopClientGUI;
import ModuleCommon.valueobjects.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class PanelIndex extends JPanel implements PanelSearchBar.SearchListener {

    // Timer
    Timer timer;

    // End Timer
    private IShop shop = null;
    private PanelSearchBar panelSearchBar;
    private PanelAccess panelAccess;
    private PanelItemDisplay panelItemDisplay;
    private ShopClientGUI shopClientGUI;

    public PanelIndex(IShop shop, ShopClientGUI shopClientGUI) throws IOException, ClassNotFoundException {
        super(new BorderLayout());
        //shop = CatShopClient Instanz




        this.shop = shop;
        this.shopClientGUI = shopClientGUI;
        panelSearchBar = new PanelSearchBar(this.shop, this, shopClientGUI);
        add(panelSearchBar, BorderLayout.NORTH);
        panelItemDisplay = new PanelItemDisplay(this.shop);
        add(panelItemDisplay, BorderLayout.CENTER);
        panelAccess = new PanelAccess(this.shop, shopClientGUI);
        add(panelAccess, BorderLayout.SOUTH);

        // Border Style
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    public void onSearchedFor(Map<Integer, Item> searchResultList) {
        ArrayList<Item> searchResult = new ArrayList<>(searchResultList.values());
        panelItemDisplay.setInventory(searchResult);
    }


}
