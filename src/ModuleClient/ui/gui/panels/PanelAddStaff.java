package ModuleClient.ui.gui.panels;

import ModuleClient.ui.gui.ShopClientGUI;
import ModuleCommon.IShop;
import ModuleCommon.Exceptions.UserAlreadyExistsException;
import ModuleServer.domain.Shop;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PanelAddStaff extends JPanel{
    private JButton addStaffButton;
    private JButton clearAllButton;

    private JTextField nameTextField = new JTextField();
    private JTextField passwordTextField = new JTextField();

    private ShopClientGUI shopClientGUI = null;
    private IShop shop = null;

    public PanelAddStaff(IShop shop, ShopClientGUI shopClientGUI) {
        super(new BorderLayout());

        this.shop = shop;

        JPanel panelAddStaffForm = new JPanel();
        panelAddStaffForm.setLayout(new BoxLayout(panelAddStaffForm, BoxLayout.Y_AXIS));
        panelAddStaffForm.setMaximumSize(new Dimension(50, 25));
        this.add(panelAddStaffForm, BorderLayout.CENTER);
        //frame.setVisible(true);

        // sign-up form
        panelAddStaffForm.add(new JLabel("Employee's Name:"));
        panelAddStaffForm.add(nameTextField);
        panelAddStaffForm.add(new JLabel("Choose a strong password:"));
        panelAddStaffForm.add(passwordTextField);

        clearAllButton = new JButton("Clear All Fields");
        panelAddStaffForm.add(clearAllButton);

        addStaffButton = new JButton("Add As Team Member");
        panelAddStaffForm.add(addStaffButton);

        addStaffButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String password = passwordTextField.getText();
            if (!name.equals("")) {
                try {
                    shop.signUpStaff(name, password);
                    clearTextFields();

                    JOptionPane.showMessageDialog(null, "You successfully added " + name + " as a new team member!", "Well Done", JOptionPane.INFORMATION_MESSAGE);
                } catch (UserAlreadyExistsException uaee){
                    JOptionPane.showMessageDialog(null, "This User already exists!", "Error", JOptionPane.ERROR_MESSAGE);

                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }else {
                JOptionPane.showMessageDialog(null, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        clearAllButton.addActionListener(e -> {
            clearTextFields();
        });

    }
    private void clearTextFields(){
        nameTextField.setText("");
        passwordTextField.setText("");
    }
}
