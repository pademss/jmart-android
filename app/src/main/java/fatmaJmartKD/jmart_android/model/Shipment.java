package fatmaJmartKD.jmart_android.model;

/**
 * Class Shipment - write a description of the class here
 *
 * @author Fatma Putri Ramadhani
 *
 */

public class Shipment {
    public String address;
    public int cost;
    public byte plan;
    public String receipt;

    //constructor
    public Shipment(String address, int cost, byte plan, String receipt){
        this.address = address;
        this.cost = cost;
        this.plan = plan;
        this.receipt = receipt;
    }
}
