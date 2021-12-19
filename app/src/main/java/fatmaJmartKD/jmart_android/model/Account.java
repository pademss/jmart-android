package fatmaJmartKD.jmart_android.model;

/**
 * Class Account - write a description of the class here
 *
 * @author Fatma Putri Ramadhani
 *
 */


public class Account extends Serializable{
    public double balance;
    public String email;
    public String name;
    public String password;
    public Store store;

    public Account(String name, String email, String password, double balance){
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }
}

