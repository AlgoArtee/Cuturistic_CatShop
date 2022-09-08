package ModuleClient.ui.gui.panels;

import ModuleCommon.IShop;
import ModuleClient.ui.gui.ItemDetails;
import ModuleClient.ui.gui.ItemTableModel;
import ModuleCommon.valueobjects.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class PanelItemDisplay extends JPanel{
    protected ItemTableModel itemTableModel;
    private JTable itemTable;
    private IShop shop = null;
    public PanelItemDisplay(IShop shop) throws IOException, ClassNotFoundException {
        super(new BorderLayout());
        this.shop = shop;
        java.util.List<Item> itemDisplay = new ArrayList<>(shop.getInventory().values());
        itemTableModel = new ItemTableModel(itemDisplay);
        itemTable = new JTable(itemTableModel);

        // Bring some items into spotlight: Sizing the 2 first columns different than the rest
        /*
        for(int i=0; i<shop.getInventory().size(); i++)
        {
            if(i==0 || i==1){
                itemTable.setRowHeight(i,100);
            }else{
                itemTable.setRowHeight(i,70);
            }
        }*/
        // TODO: Höhe responsive machen
        itemTable.setRowHeight(70);

        itemTable.getTableHeader().setReorderingAllowed(false);
        itemTable.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());

        //if (shop.isLoggedInAs(shop.getUserType())){
            addDetailDisplay();
        //} else {
            System.out.println("Not Logged In");
        //}


        // JScrollPane is needed for JTable display
        JScrollPane scrollPane = new JScrollPane(itemTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addDetailDisplay() throws IOException, ClassNotFoundException {

        //shop = new Shop("Shop");
        itemTable.addMouseListener(new MouseAdapter() {

            Item selectedItem;
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    System.out.println("I double-clicked");

                    try {
                        Object selectedData = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
                        System.out.println(selectedData);

                        selectedItem = shop.getInventory().get(selectedData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    //table.getSelectedRow();

                    ItemDetails itemDetails = new ItemDetails(selectedItem);
                }
            }
        });
    }

    public void setInventory(ArrayList<Item> newInventoryDisplay){

        itemTableModel.setInventory(newInventoryDisplay);

    }

    class ImageRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int colum){
            String photoName = value.toString();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/ModuleClient/itemresources/"+photoName).
                    getImage().getScaledInstance(60,60, Image.SCALE_DEFAULT));
            return new JLabel(imageIcon);
        }
    }
}
