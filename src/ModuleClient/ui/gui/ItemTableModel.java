package ModuleClient.ui.gui;

import ModuleCommon.valueobjects.Item;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ItemTableModel extends AbstractTableModel {
    private ArrayList<Item> inventory;
    private String[] columnTitles = {"Number", "Name", "Photo","Description", "Price", "Availability", "Bulk Size"};

    public ItemTableModel(java.util.List<Item> currentInventory){
        inventory = new ArrayList<>();
        //inventory.forEach(currentInventory::putIfAbsent);
        inventory.addAll(currentInventory);

    }

    public void setInventory(ArrayList<Item> currentInventory){
        inventory.clear();
        inventory.addAll(currentInventory);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return inventory.size();
    }

    @Override
    public int getColumnCount() {
        return columnTitles.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        // Items are stored in a List

        Item selectedItem = inventory.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return selectedItem.getItemNumber();
            case 1:
                return selectedItem.getItemName();
            case 2:
                return selectedItem.getPic();
            case 3:
                return selectedItem.getDescription();
            case 4:
                return selectedItem.getItemPrice();
            case 5:
                return selectedItem.getIsAvailable() ? "in stock" : "out of stock";
            case 6:
                return selectedItem.getBulkSize() == 0 ? "-" : selectedItem.getBulkSize();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnTitles[column];
    }
}
