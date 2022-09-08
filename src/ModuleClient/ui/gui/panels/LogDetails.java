package ModuleClient.ui.gui.panels;

import ModuleClient.ui.gui.LogTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class LogDetails extends JFrame {

    private JFrame frame;
    //private Shop shop = new Shop("Shop");

    public LogDetails(java.util.List<String> logList) throws IOException, ClassNotFoundException {

        initialize(logList);

    }

    private void initialize(java.util.List<String> logList) {
        frame = new JFrame();
        setTitle("Log Details: ");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        //java.util.List<String> logDisplay = new ArrayList<>(shop.returnLogList());
        java.util.List<String> logDisplay = new ArrayList<>(logList);
        LogTableModel logTableModel = new LogTableModel(logDisplay);
        JTable logTable = new JTable(logTableModel);

        // TODO: Höhe responsive machen
        logTable.setRowHeight(10);

        logTable.getTableHeader().setReorderingAllowed(false);

        logTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        logTable.getColumnModel().getColumn(0).setPreferredWidth(1000);
        logTable.getColumnModel().getColumn(0).setCellRenderer(new WordWrapCellRenderer());

        // JScrollPane is needed for JTable display
        JScrollPane scrollPane = new JScrollPane(logTable);
        add(scrollPane, BorderLayout.CENTER);

    }

    static class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {
        WordWrapCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int heightVar = 10;

            setText(value.toString());
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height+heightVar);
            if (table.getRowHeight(row) != getPreferredSize().height+heightVar) {
                table.setRowHeight(row, getPreferredSize().height+heightVar);
            }
            return this;
        }
    }
}
