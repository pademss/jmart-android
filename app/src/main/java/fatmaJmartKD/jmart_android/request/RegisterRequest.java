package fatmaJmartKD.jmart_android.request;

/**
 * Class RegisterRequest - Melakukan request untuk membuat akun baru
 *
 * @author Fatma Putri Ramadhani
 *
 */


import java.util.HashMap;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest{
    private static final String URL = "http://192.168.100.6:8080/account/register";
    private final Map<String, String> params;

    public RegisterRequest(String name, String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
