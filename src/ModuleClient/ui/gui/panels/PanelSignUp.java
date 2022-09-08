package ModuleClient.ui.gui.panels;

import ModuleCommon.IShop;
import ModuleClient.ui.gui.ShopClientGUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PanelSignUp extends JPanel {
    private JButton backButton;
    private JButton signUpButton;
    private JButton clearAllButton;
    private JTextField nameTextField = new JTextField();
    private JTextField passwordTextField = new JTextField();
    private JTextField streetNumberTextField = new JTextField();
    private JTextField postalCodeTextField = new JTextField();
    private JTextField placeOfResidenceTextField = new JTextField();
    private ShopClientGUI shopClientGUI = null;
    private IShop shop = null;

    public PanelSignUp(IShop shop, ShopClientGUI shopClientGUI) {
        super(new BorderLayout());

        JPanel panelSignUpForm = new JPanel();
        panelSignUpForm.setLayout(new BoxLayout(panelSignUpForm, BoxLayout.Y_AXIS));
        this.add(panelSignUpForm, BorderLayout.CENTER);

        // Button that leads back to the landingpage
        backButton = new JButton("back");
        panelSignUpForm.add(backButton);

        // sign-up form
        panelSignUpForm.add(new JLabel("Name:"));
        //nameTextField.setPreferredSize(new Dimension(150, 30));
        panelSignUpForm.add(nameTextField);
        panelSignUpForm.add(new JLabel("Password"));
        //passwordTextField.setPreferredSize(new Dimension(150, 30));
        panelSignUpForm.add(passwordTextField);
        panelSignUpForm.add(new JLabel("Street and Number"));
        //streetNumberTextField.setPreferredSize(new Dimension(150, 30));
        panelSignUpForm.add(streetNumberTextField);
        panelSignUpForm.add(new JLabel("Postal Code"));
        //postalCodeTextField.setPreferredSize(new Dimension(150, 30));
        panelSignUpForm.add(postalCodeTextField);
        panelSignUpForm.add(new JLabel("Place of Residence"));
        //placeOfResidenceTextField.setPreferredSize(new Dimension(150, 30));
        panelSignUpForm.add(placeOfResidenceTextField);

        //nameTextField.setText("");

        clearAllButton = new JButton("Clear All Fields");
        panelSignUpForm.add(clearAllButton);

        signUpButton = new JButton("Sign me up!");
        panelSignUpForm.add(signUpButton);

        signUpButton.addActionListener(e -> {
            try {
                String customerName = nameTextField.getText();
                String password = passwordTextField.getText();
                String streetNumber = streetNumberTextField.getText();
                int postalCode = Integer.parseInt(postalCodeTextField.getText());
                String placeOfResidence = placeOfResidenceTextField.getText();

                shop.signUpCustomer(customerName, password, streetNumber, postalCode, placeOfResidence);
                shopClientGUI.getCardLayout().show(shopClientGUI.getCardPanel(), "2");
                JOptionPane.showMessageDialog(null, "Welcome "+ customerName + " - the best shopping experience of your life starts now!", "Information", JOptionPane.INFORMATION_MESSAGE);
                clearTextFields();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "-- All fields are required and only numbers are allowed for postal code! --", "Information", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "-- A user with this name already exists --", "Information", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "-- failed to process the data correctly --", "Information", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearAllButton.addActionListener(e -> {
            clearTextFields();
        });

        backButton.addActionListener(e -> {
            shopClientGUI.getCardLayout().show(shopClientGUI.getCardPanel(), "1");
            clearTextFields();
        });
    }
    private void clearTextFields(){
        nameTextField.setText("");
        passwordTextField.setText("");
        streetNumberTextField.setText("");
        postalCodeTextField.setText("");
        placeOfResidenceTextField.setText("");
    }
}
