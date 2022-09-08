package ModuleCommon.valueobjects;

import java.io.Serializable;

/**
 * Blueprint for a customer address.
 *
 * @author Chachulski, Mathea
 */
public class Address implements Serializable {
    private String streetAndNumber = "";
    private int postalCode = 0;
    private String placeOfResidence = "";

    public Address(String streetAndNumber, int postalCode, String placeOfResidence){
        this.streetAndNumber = streetAndNumber;
        this.postalCode = postalCode;
        this.placeOfResidence = placeOfResidence;
    }

    @Override
    public String toString(){
        return this.streetAndNumber + ";" + this.postalCode + ";" +
                this.placeOfResidence;
    }

}
