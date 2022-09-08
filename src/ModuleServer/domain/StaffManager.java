package ModuleServer.domain;

import ModuleCommon.Exceptions.UserAlreadyExistsException;
import ModuleCommon.valueobjects.Employee;
import ModuleCommon.valueobjects.User;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Class for the management of webshop employees.
 * It inherits following methods from its parent class UserManager:
 * getUserList(), getLastNumber(), createNextNumber(), checkPassword().
 *
 * @author Mathea, Malla-Mirza, Chachulski
 */

public class StaffManager extends UserManager implements Serializable {

    public StaffManager() throws IOException, ClassNotFoundException {

        try{
            //addFirstEmployees();
            userList = getCurrentUserList();
        }catch (IOException ioe){
            throw new IOException();
        }catch (ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }
    }

    // zu Testzwecken, solange die Persistenz-Schicht noch nicht aktiv ist
    protected void addFirstEmployees() throws IOException, ClassNotFoundException {
        try {
            Map<String, User> userListToBeExtended = getCurrentUserList();
            User laura = new Employee("Laura Chachulski", createNextNumber(), "Tick");
            userListToBeExtended.put("Laura Chachulski", laura);
            User maya = new Employee("Maya Malla-Mirza", createNextNumber(), "Trick");
            userListToBeExtended.put("Maya Malla-Mirza", maya);
            User nina = new Employee("Katharina Mathea", createNextNumber(), "Track");
            userListToBeExtended.put("Katharina Mathea", nina);
            this.persistenceManager.writeUserList(userListToBeExtended);
        }catch (IOException ioe){
            throw new IOException();
        }catch (ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }
    }

    protected void addEmployee(String name, String password) throws ModuleCommon.Exceptions.UserAlreadyExistsException, IOException, ClassNotFoundException {
        User newEmployee = new Employee(name, createNextNumber(), password);
        try {
            if (!getCurrentUserList().containsKey(name)) {
                Map<String, User> userListToBeExtended = getCurrentUserList();
                userListToBeExtended.put(name, newEmployee);
                this.persistenceManager.writeUserList(userListToBeExtended);
            } else {
                throw new UserAlreadyExistsException(name);
            }
        }catch (IOException ioe){
            throw new IOException();
        }catch (ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }
    }

    private Map<String, User> getCurrentUserList() throws IOException, ClassNotFoundException {
        try {
            Map<String, User> currentUserList = this.persistenceManager.readUserList();
            return currentUserList;
        } catch (IOException ioe) {
            throw new IOException();
        } catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }

}




