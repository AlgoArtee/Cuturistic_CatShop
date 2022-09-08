package ModuleClient.ui.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class LogTableModel extends AbstractTableModel {
    private ArrayList<String> logFile;
    private String[] columnTitles = {"Log File"};

    public LogTableModel(java.util.List<String> currentLogFile){
        logFile = new ArrayList<>();
        //inventory.forEach(currentInventory::putIfAbsent);
        logFile.addAll(currentLogFile);

    }
    /*
    public void setInventory(ArrayList<String> currentInventory){
        logFile.clear();
        logFile.addAll(currentInventory);
        fireTableDataChanged();
    } */

    @Override
    public int getRowCount() {
        return logFile.size();
    }

    @Override
    public int getColumnCount() {
        return columnTitles.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        // Items are stored in a List
        //java.util.List<Item> valueList = new ArrayList<>(inventory.values());

        String logEntry = logFile.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return logEntry;
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnTitles[column];
    }
}
