package fatmaJmartKD.jmart_android.request;

/**
 * Class RegisterStoreRequest - Melakukan request untuk membuat store baru
 * dari sebuah akun
 *
 * @author Fatma Putri Ramadhani
 *
 */


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//Class for registering a new store requests
public class RegisterStoreRequest extends StringRequest{
    private Map<String, String> params;

    public RegisterStoreRequest(int id, String name, String address, String phoneNumber, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, "http://192.168.100.6:8080/account/"+id+"/registerStore", listener, errorListener);
        params = new HashMap<>();
        params.put("name", name);
        params.put("address", address);
        params.put("phoneNumber", phoneNumber);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
