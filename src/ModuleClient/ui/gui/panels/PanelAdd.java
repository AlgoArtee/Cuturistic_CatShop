package ModuleClient.ui.gui.panels;

import ModuleCommon.IShop;
import ModuleCommon.valueobjects.Item;

import javax.swing.*;
import java.io.IOException;

public class PanelAdd extends JPanel {
    //PanelAdd uses this interface (implemented as an inner class) to let the registered Listeners know that
    // a new Item has been added. The interface is implemented by the ShopClientGUI.
    public interface AddListener {
        void onItemAdded(Item item) throws IOException, ClassNotFoundException;
    }
private IShop shop = null;
    private AddListener addListener = null;

    private JTextField textfieldNummer = new JTextField();
    private JTextField textfieldName = new JTextField();
    private JTextField textfieldDescription = new JTextField();
    private JTextField textfieldPrice = new JTextField();
    private JTextField textfieldAmountInStock = new JTextField();
    private JTextField textfieldAvailability = new JTextField();
    private JTextField textfieldBulkSize = new JTextField();
    private JButton addButton = new JButton("Artikel hinzufügen");
}
