package fatmaJmartKD.jmart_android;

/**
 * Class LoginActivity - Mengatur jalannya halaman log in
 *
 * @author Fatma Putri Ramadhani
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import fatmaJmartKD.jmart_android.model.Account;
import fatmaJmartKD.jmart_android.model.Store;
import fatmaJmartKD.jmart_android.request.LoginRequest;


public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
    private static final Gson gson = new Gson();
    private static Account loggedAccount = null;

    public static Account getLoggedAccount(){
        return loggedAccount;
    }

    public static void setLoggedAccount(Account account){
        loggedAccount = account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editEmail = findViewById(R.id.editTextTextEmailAddress);
        EditText editPassword = findViewById(R.id.editTextTextPassword);
        Button loginButton = findViewById(R.id.button);
        TextView registerLogin = findViewById(R.id.textView2);

//        editEmail.setText("fatma.putri@ui.ac.id");
//        editPassword.setText("Fatma123");

        //klik untuk melakukan login dan mencocokan pada database
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest newLogin = new LoginRequest(
                        editEmail.getText().toString(),
                        editPassword.getText().toString(),
                        new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject != null) {
                                        Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_SHORT).show();
                                        loggedAccount = gson.fromJson(jsonObject.toString(), Account.class);
                                        Intent loginSuccess = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(loginSuccess);
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),
                                        "Server error", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(newLogin);
            }
        });
        registerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPage = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerPage);
            }
        });
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

    }

    public static void reloadLoggedAccount(String response){
        loggedAccount = gson.fromJson(response, Account.class);
    }

    public static void insertLoggedAccountStore(String response){
        Store newStore = gson.fromJson(response, Store.class);
        loggedAccount.store = newStore;
    }
}