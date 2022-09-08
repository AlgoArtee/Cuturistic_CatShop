package ModuleClient.ui.gui;

import ModuleCommon.valueobjects.UserType;
import ModuleCommon.valueobjects.Customer;
import ModuleCommon.valueobjects.User;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserTableModel extends AbstractTableModel {
    private List<User> userList;
    // the password is not displayed due to regulations of the European DSGVO
    private String[] columnTitles = {"Number", "Name", "User Type", "Address"};

    public UserTableModel(Map<String, User> currentUserList){
        userList = new ArrayList<>();
        userList.addAll(currentUserList.values());
    }
    public void setUserList(List<User> currentUserList){
        userList.clear();
        userList.addAll(currentUserList);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return userList.size();
    }

    @Override
    public int getColumnCount() {
        return columnTitles.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User selectedUser = userList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return selectedUser.getNumber();
            case 1:
                return selectedUser.getName();
            case 2:
                return selectedUser.getUserType().equals(UserType.EMPLOYEE) ? "employee" : "customer";
            case 3:
                if (selectedUser.getUserType().equals(UserType.CUSTOMER)){
                    Customer selectedCustomer = (Customer)selectedUser;
                    return selectedCustomer.getAddress();
                } else return "-";
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnTitles[column];
    }
}

