package ModuleClient.ui.gui.panels;

import ModuleClient.ui.gui.ShopClientGUI;
import ModuleCommon.IShop;
import ModuleServer.domain.Shop;

import javax.swing.*;

public class AddItem extends JFrame {
    private JFrame frameAddItem;
    private IShop shop = null;
    private ShopClientGUI shopClientGUI = null;

    public AddItem(IShop shop, ShopClientGUI shopClientGUI)  {
        this.shop = shop;
        this.shopClientGUI = shopClientGUI;
        JFrame frameAddItem = new JFrame();
        frameAddItem.setTitle("Add A New Item To The Inventory: ");
        frameAddItem.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameAddItem.setSize(500, 400);
        frameAddItem.setLocationRelativeTo(null);
        frameAddItem.setResizable(false);

        PanelAddItem panelAddItem = new PanelAddItem(shop, shopClientGUI);
        frameAddItem.add(panelAddItem);

        frameAddItem.setVisible(true);
    }
}
