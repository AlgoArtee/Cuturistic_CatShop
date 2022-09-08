package ModuleCommon.valueobjects;

import java.io.Serializable;

/**
 * Class represents a single webshop user.
 * Provides basic methods for different types of users.
 *
 * @author Chachulski, Mathea
 */
public class User implements Serializable {
    public String name = "";
    protected int number = 0;
    public String password = "";
    protected UserType userType = null;

    public User(String name, int number, String password){
        this.name = name;
        this.number = number;
        this.password = password;
    }

    public int getNumber(){
        return this.number;
    }

    public UserType getUserType(){
        return this.userType;
    }

    public String getPassword(){ return this.password; }

    public String getName() { return this.name; }

}
