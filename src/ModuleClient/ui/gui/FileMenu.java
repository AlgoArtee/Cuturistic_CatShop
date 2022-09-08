package ModuleClient.ui.gui;

import ModuleCommon.IShop;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FileMenu extends JMenu implements ActionListener {
    private IShop shop = null;

    public FileMenu(IShop shop) {
        super("Datei");

        this.shop = shop;

        JMenuItem item = new JMenuItem("Speichern");
        item.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        item.addActionListener(this);
        add(item);

        addSeparator();

        item = new JMenuItem("Beenden");
        item.addActionListener(this);
        add(item);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch(command) {
            case "Speichern":
                try {
                    this.shop.saveCurrentItemList();
                } catch (IOException ex) {
                    System.out.println("Speichern fehlgeschlagen!");
                }
                break;

            case "Beenden":
                System.exit(0);
                break;

            default:
                throw new IllegalArgumentException("Unbekanntes MenuItem!");
        }
    }
}
