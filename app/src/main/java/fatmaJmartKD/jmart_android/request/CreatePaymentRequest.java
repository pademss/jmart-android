package fatmaJmartKD.jmart_android.request;

/**
 * Class CreatePaymentRequest - Melakukan request ketika membeli
 * barang dan melakukan payment
 *
 * @author Fatma Putri Ramadhani
 *
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreatePaymentRequest extends StringRequest {
    private Map<String, String> params;

    public CreatePaymentRequest(String buyerId, String productId, String productCount, String shipmentAddress, String shipmentPlan,
                                Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, "http://192.168.100.6:8080/payment/create", listener, errorListener);
        params = new HashMap<>();
        params.put("buyerId", buyerId);
        params.put("productId", productId);
        params.put("productCount", productCount);
        params.put("shipmentAddress", shipmentAddress);
        params.put("shipmentPlan", shipmentPlan);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
