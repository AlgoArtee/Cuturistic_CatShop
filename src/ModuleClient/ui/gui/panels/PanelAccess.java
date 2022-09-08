package ModuleClient.ui.gui.panels;

import ModuleCommon.Exceptions.NameNotExistendException;
import ModuleCommon.Exceptions.PasswordNotExistendException;
import ModuleCommon.valueobjects.LogInPhase;
import ModuleCommon.IShop;
import ModuleClient.ui.gui.ShopClientGUI;
import ModuleCommon.valueobjects.User;
import ModuleCommon.valueobjects.UserType;
import ModuleServer.domain.SessionState;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PanelAccess extends JPanel {
    private IShop shop = null;
    protected ShopClientGUI shopClientGUI;
    private JTextField nameTextField = new JTextField();
    private JTextField passwordTextField = new JTextField();
    private JButton signInButton = new JButton("Sign in");
    private JButton signUpButton = new JButton("Sign me up!");
    protected User user;
    private LogInPhase logInPhase;
    private SessionState sessionState;

    public PanelAccess(IShop shop, ShopClientGUI shopClientGUI) throws IOException, ClassNotFoundException {
        super(new FlowLayout());
        //shop = CatShopClient Instanz
        this.shop = shop;
        this.shopClientGUI = shopClientGUI;
        add(new JLabel("Name:"));
        nameTextField.setPreferredSize(new Dimension(150, 30));
        add(nameTextField);
        add(new JLabel("Password:"));
        passwordTextField.setPreferredSize(new Dimension(150, 30));
        add(passwordTextField);
        add(signInButton);
        sessionState = null;

        // TODO: Abstand einfügen

        add(new JLabel("New Customer?"));
        add(signUpButton);

        signInButton.addActionListener(e -> {
            String customerName = nameTextField.getText();
            String password = passwordTextField.getText();

            try {
                shop.logIn(customerName, password);
                JOptionPane.showMessageDialog(null, "Login Successfull!", "Information", JOptionPane.INFORMATION_MESSAGE);
                clearTextFields();
                if (shop.isLoggedInAs(UserType.CUSTOMER)) {
                    // --- Personalization
                    shopClientGUI.getPanelCustomerShop().setBorder(BorderFactory.createTitledBorder(customerName));
                    // End Personalization

                    shopClientGUI.getCardLayout().show(shopClientGUI.getCardPanel(), "2");
                } else if (shop.isLoggedInAs(UserType.EMPLOYEE)) {
                    // --- Personalization
                    shopClientGUI.getPanelEmployeeShop().setBorder(BorderFactory.createTitledBorder(customerName));
                    // End Personalization

                    shopClientGUI.getCardLayout().show(shopClientGUI.getCardPanel(), "4");
                } else {

                }
            } catch (PasswordNotExistendException noSuchPassword) {
                JOptionPane.showMessageDialog(null, noSuchPassword.getMessage(), "Information", JOptionPane.INFORMATION_MESSAGE);
                clearTextFields();
            } catch (NameNotExistendException noSuchUser) {
                JOptionPane.showMessageDialog(null, noSuchUser.getMessage(), "Information", JOptionPane.INFORMATION_MESSAGE);
                clearTextFields();
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "-- failed to process the data correctly --", "Information", JOptionPane.INFORMATION_MESSAGE);
                clearTextFields();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "please check your spelling", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        signUpButton.addActionListener(e -> {
            shopClientGUI.getCardLayout().show(shopClientGUI.getCardPanel(), "3");
        });
    }
    private void clearTextFields(){
        nameTextField.setText("");
        passwordTextField.setText("");
    }
}
