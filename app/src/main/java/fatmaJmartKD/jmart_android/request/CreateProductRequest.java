package fatmaJmartKD.jmart_android.request;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import fatmaJmartKD.jmart_android.model.ProductCategory;


//Class for creating new product Requests
public class CreateProductRequest extends StringRequest {
    private Map<String, String> params;

    public CreateProductRequest(String accountId, String name, String weight, String conditionUsed, String price,
                                String  discount, String category, String shipmentPlans,
                                Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, "http://192.168.100.6:8080/product/create", listener, errorListener);
        params = new HashMap<>();
        params.put("accountId", accountId);
        params.put("name", name);
        params.put("weight", weight);
        params.put("conditionUsed", conditionUsed);
        params.put("price", price);
        params.put("discount", discount);
        params.put("category", category);
        params.put("shipmentPlans", shipmentPlans);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
