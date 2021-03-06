package fatmaJmartKD.jmart_android.model;

/**
 * Class Product - write a description of the class here
 *
 * @author Fatma Putri Ramadhani
 *
 */

public class Product extends Serializable{

    public int accountId;
    public ProductCategory category;
    public boolean conditionUsed;
    public double discount;
    public String name;
    public double price;
    public byte shipmentPlans;
    public int weight;

    @Override
    public String toString(){
        return name;
    }
}
