package ModuleClient.ui.gui.panels;

import ModuleClient.ui.gui.ShopClientGUI;
import ModuleCommon.IShop;
import ModuleServer.domain.Shop;

import javax.swing.*;

public class AddStaff extends JFrame{
    private JFrame frameAddStaff;
    private IShop shop = null;
    private ShopClientGUI shopClientGUI = null;

    public AddStaff(IShop shop, ShopClientGUI shopClientGUI)  {
        this.shop = shop;
        this.shopClientGUI = shopClientGUI;
        JFrame frameAddStaff = new JFrame();
        frameAddStaff.setTitle("Add A New Team Member: ");
        frameAddStaff.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameAddStaff.setSize(500, 400);
        frameAddStaff.setLocationRelativeTo(null);
        frameAddStaff.setResizable(false);

        PanelAddStaff panelAddStaff = new PanelAddStaff(shop, shopClientGUI);
        frameAddStaff.add(panelAddStaff);

        frameAddStaff.setVisible(true);
    }
}
