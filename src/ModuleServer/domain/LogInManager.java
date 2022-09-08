package ModuleServer.domain;

import ModuleCommon.Exceptions.NameNotExistendException;
import ModuleCommon.Exceptions.PasswordNotExistendException;
import ModuleCommon.valueobjects.LogInPhase;
import ModuleCommon.valueobjects.User;
import ModuleCommon.valueobjects.UserType;
import ModuleServer.persistence.FilePersistenceManager;
import ModuleServer.persistence.PersistenceManager;

import java.io.IOException;
import java.util.Map;

import static ModuleCommon.valueobjects.LogInPhase.LOGGED_IN;
import static ModuleCommon.valueobjects.LogInPhase.LOGGED_OUT;


/**
 * Class for the management of customer- and staff login inside a simple webshop.
 * Extends AccessManager
 * It inherits following methods from its parent class:
 * getter and setter for UserType and LoginPhase, setter for currentUser
 *
 * @author Mathea, Chachulski
 */

public class LogInManager{
    private PersistenceManager persistenceManager = new FilePersistenceManager();

    public LogInManager() throws IOException {
    }

    private UserType checkUserType(String name) throws NameNotExistendException, IOException, ClassNotFoundException {
        if (getCurrentUserList().containsKey(name) && getCurrentUserList().get(name).getUserType() == UserType.EMPLOYEE){
            return UserType.EMPLOYEE;
        }
        if  (getCurrentUserList().containsKey(name) && getCurrentUserList().get(name).getUserType() == UserType.CUSTOMER){
            return UserType.CUSTOMER;
        } else throw new NameNotExistendException();
    }

    private User getCurrentUser(String name) throws IOException, ClassNotFoundException {
        return getCurrentUserList().get(name);
    }

    protected void processLoginInfo(String name, String password) throws PasswordNotExistendException, NameNotExistendException, IOException, ClassNotFoundException {
        try {
            UserType userType = checkUserType(name);
            if (getCurrentUserList().containsKey(name) && checkPassword(name, password)) {
                setLogInPhase(LOGGED_IN);
                setUserType(userType);
                setCurrentUser(getCurrentUser(name));
            } else {
                throw new PasswordNotExistendException();
            }
        }catch (ModuleCommon.Exceptions.NameNotExistendException wrongName) {
            throw new NameNotExistendException();
        }catch (IOException ioe){
            throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }
    private boolean checkPassword(String name, String password) throws IOException, ClassNotFoundException {
        try {
            return (getCurrentUserList().get(name).getPassword().equals(password));
        } catch (NullPointerException e){
            return false;
        }catch (IOException ioe){
        throw new IOException();
        }catch (ClassNotFoundException cnfe) {
            throw new ClassNotFoundException();
        }
    }
    protected void logOut(){
        setCurrentUser(null);
        setLogInPhase(LOGGED_OUT);
        setUserType(null);
    }
    protected LogInPhase getLogInPhase(){
        return SessionState.logInPhase;
    }

    protected void setLogInPhase(LogInPhase logInPhase){
        SessionState.logInPhase = logInPhase;
    }

    protected UserType getUserType(){
        return SessionState.userType;
    }

    protected void setUserType(UserType userType){
        SessionState.userType = userType;
    }

    protected void setCurrentUser(User user){
        SessionState.currentUser = user;
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
