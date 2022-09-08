package ModuleCommon.valueobjects;

import java.io.Serializable;

import static ModuleCommon.valueobjects.UserType.EMPLOYEE;

/**
 * Class represents a single webshop customer.
 * Extends User.
 * It inherits following methods from its parent class:
 * getNumber(), addToCart(), getCurrentCart
 *
 * @author Malla Mirza, Chachulski, Mathea
 */
public class Employee extends User implements Serializable {
    private String password;

    public Employee(String employeeName, int number, String password){
        super(employeeName, number, password);
        this.name = employeeName;
        this.number = number;
        this.password = password;
        this.userType = EMPLOYEE;
    }
}
