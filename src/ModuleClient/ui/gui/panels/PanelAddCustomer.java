package ModuleClient.ui.gui.panels;

import ModuleCommon.IShop;
import ModuleCommon.valueobjects.Item;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PanelAddCustomer extends JPanel {

    //PanelAddCustomer uses this interface (implemented as an inner class) to let the registered Listeners know that
    // a new Customer has been added. The interface is implemented by the ShopClientGUI.
    public interface AddCustomerListener {
        void onCustomerAdded(Item item) throws IOException, ClassNotFoundException;
    }
private IShop shop = null;
    private AddCustomerListener addListener = null;

    private JTextField textfieldName = new JTextField();
    private JTextField textfieldPassword = new JTextField();
    private JTextField textfieldStreetAndNumber = new JTextField();
    private JTextField textfieldPostalCode = new JTextField();
    private JTextField textfieldPlaceOfResidence = new JTextField();
    private JButton addButton = new JButton("Sign me up!");

    public PanelAddCustomer(){
        super(new FlowLayout());
    }
}
