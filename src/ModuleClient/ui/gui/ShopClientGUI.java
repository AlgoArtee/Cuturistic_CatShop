package ModuleClient.ui.gui;

import ModuleClient.net.CatShopClient;
import ModuleClient.ui.gui.panels.*;
import ModuleCommon.IShop;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
/**
 * The Shop is divided into client and server. They communicate via sockets.
 *
 * @author Chachulsky, Mathea
 */
public class ShopClientGUI extends JFrame /*implements PanelSearchBar.SearchListener*/{
    private IShop shop;
    private PanelIndex panelIndex;
    private PanelCustomerShop panelCustomerShop;
    private PanelEmployeeShop panelEmployeeShop;
    private PanelSignUp panelSignUp;
    private PanelAddItem panelAddItem;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public ShopClientGUI() throws IOException, ClassNotFoundException {
        super("Shop");

        try {
        shop = new CatShopClient();
        } catch (IOException e) {
            e.printStackTrace();
        }

        panelIndex = new PanelIndex(shop, this);
        panelCustomerShop = new PanelCustomerShop(shop, this);
        panelEmployeeShop = new PanelEmployeeShop(shop, this);
        panelSignUp = new PanelSignUp(shop,this);
        panelAddItem = new PanelAddItem(shop, this);

        // implements a CardLayout
        cardPanel = new JPanel();
        cardPanel.setLayout(new CardLayout());
        cardPanel.add(panelIndex, "1");
        cardPanel.add(panelCustomerShop, "2");
        cardPanel.add(panelSignUp, "3");
        cardPanel.add(panelEmployeeShop, "4");
        cardPanel.add(panelAddItem, "5");

        this.cardLayout = (CardLayout) (cardPanel.getLayout());
        this.cardLayout.show(cardPanel, "1");
        this.getContentPane().add(cardPanel);

        initializeMenuBar();

        addWindowListener(new WindowCloser());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // TODO: Größe responsive machen
        setSize(1000, 700);
        setLocation(500, 200);
        setVisible(true);
    }

    private void initializeMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new FileMenu(shop));
        setJMenuBar(menuBar);
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ShopClientGUI gui = new ShopClientGUI();
    }

    public CardLayout getCardLayout(){
        return this.cardLayout;
    }

    public JPanel getCardPanel(){
        return this.cardPanel;
    }

    public JComponent getPanelCustomerShop() {
        return this.panelCustomerShop;
    }

    public JComponent getPanelEmployeeShop() {
        return this.panelEmployeeShop;
    }
}
