package ModuleServer.domain;

import ModuleCommon.valueobjects.User;
import ModuleServer.persistence.FilePersistenceManager;
import ModuleServer.persistence.PersistenceManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides basic methods for the management
 * of different kinds of e-shop users.
 *
 * @author Chachulski, Mathea
 */

public class UserManager implements Serializable {
    protected volatile Map<String, User> userList = new HashMap<>();
    PersistenceManager persistenceManager = new FilePersistenceManager();

    public UserManager(){

    }

    protected int getLastNumber(){
        int highestNumber = 0;
        for (User user: userList.values()){
            int userNumber = user.getNumber();
            if (userNumber > highestNumber){
                highestNumber = userNumber;
            }
        }
        return highestNumber;
    }
    protected int createNextNumber(){
        int lastNumber = getLastNumber();
        return lastNumber + 1;
    }

}
