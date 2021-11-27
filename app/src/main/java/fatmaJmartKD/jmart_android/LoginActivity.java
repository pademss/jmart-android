package fatmaJmartKD.jmart_android;

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

import fatmaJmartKD.jmart_android.model.Account;
import fatmaJmartKD.jmart_android.request.LoginRequest;

public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{
    private static final Gson gson = new Gson();
    private static Account loggedAccount = null;
    private TextView textView2;
    private EditText editTextTextEmailAddress;
    private EditText editTextTextPassword;
    private Button button;

    public static Account getLoggedAccount(){
        return loggedAccount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        button = findViewById(R.id.button);
        textView2 = findViewById(R.id.textView2);

        button.setOnClickListener(this::onLoginClick);

        textView2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRegisterActivity();
            }
        });
    }
    private void onLoginClick(View view){
        String email = editTextTextEmailAddress.getText().toString();
        String pass = editTextTextPassword.getText().toString();
        Response.Listener<String> listener = s -> {

        };

        Response.ErrorListener error = item -> {

        };

        LoginRequest loginRequest = new LoginRequest(email, pass, listener, error);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(loginRequest);

    }

    public void onResponse(String response){
        Intent i = new Intent(this, MainActivity.class);
        Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show();
        startActivity(i);
    }

    public void openRegisterActivity(){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Intent i = new Intent(this, MainActivity.class);
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        startActivity(i);
    }
}