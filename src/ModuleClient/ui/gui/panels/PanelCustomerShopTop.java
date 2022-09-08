package ModuleClient.ui.gui.panels;

import ModuleCommon.IShop;
import ModuleClient.ui.gui.ShopClientGUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PanelCustomerShopTop extends JPanel {
    private ShopClientGUI shopClientGUI = null;
    private JButton signOutButton = new JButton("Sign Out");

    public PanelCustomerShopTop(IShop shop, ShopClientGUI shopClientGUI) throws IOException, ClassNotFoundException {
        super(new BorderLayout());
        add(signOutButton, BorderLayout.EAST);
        signOutButton.addActionListener(e -> {
            shop.logOut();
            shopClientGUI.getCardLayout().show(shopClientGUI.getCardPanel(), "1");
        });

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 5));
    }
}
