package ModuleClient.ui.gui;

import ModuleClient.ui.gui.panels.PanelItemDetails;
import ModuleCommon.valueobjects.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ItemDetails extends JFrame {

    private JFrame frame;

    public ItemDetails(Item item){

        initialize(item);

    }

    private void initialize(Item item) {
        frame = new JFrame();
        setTitle("Item Details: " + item.getItemName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        PanelItemDetails pid = new PanelItemDetails(item);
        JTable itemDetails = new JTable(pid);

        itemDetails.setRowHeight(800);

        itemDetails.getTableHeader().setReorderingAllowed(false);

        itemDetails.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        itemDetails.getColumnModel().getColumn(0).setPreferredWidth(50);
        itemDetails.getColumnModel().getColumn(1).setPreferredWidth(100);
        itemDetails.getColumnModel().getColumn(2).setPreferredWidth(300);
        itemDetails.getColumnModel().getColumn(3).setPreferredWidth(500);


        itemDetails.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());
        itemDetails.getColumnModel().getColumn(3).setCellRenderer(new WordWrapCellRenderer());


        // JScrollPane is needed for JTable display
        JScrollPane scrollPane = new JScrollPane(itemDetails);
        add(scrollPane, BorderLayout.CENTER);


    }

    static class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {
        WordWrapCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int heightVar = 200;

            setText(value.toString());
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height+heightVar);
            if (table.getRowHeight(row) != getPreferredSize().height+heightVar) {
                table.setRowHeight(row, getPreferredSize().height+heightVar);
            }
            return this;
        }
    }

    public static class ImageRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int colum){
            String photoName = value.toString();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/ModuleClient/itemresources/"+photoName).
                    getImage().getScaledInstance(220,220, Image.SCALE_DEFAULT));
            return new JLabel(imageIcon);
        }
    }
}
