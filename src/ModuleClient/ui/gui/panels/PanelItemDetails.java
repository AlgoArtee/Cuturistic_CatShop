package ModuleClient.ui.gui.panels;

import ModuleCommon.valueobjects.Item;

import javax.swing.table.AbstractTableModel;

public class PanelItemDetails extends AbstractTableModel {

    private Item item;

    public PanelItemDetails(Item item){
        this.item = item;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Item selectedItem = item;

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
        }
        return null;
    }
}