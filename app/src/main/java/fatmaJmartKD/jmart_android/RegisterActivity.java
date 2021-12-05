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

import org.json.JSONObject;

import java.io.FileNotFoundException;

import fatmaJmartKD.jmart_android.request.LoginRequest;
import fatmaJmartKD.jmart_android.request.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_loginHere;
    private EditText etEmail;
    private EditText etName;
    private EditText etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.editTextTextPersonName);
        etEmail = findViewById(R.id.editTextTextEmailAddress2);
        etPassword = findViewById(R.id.editTextTextPassword2);
        btnRegister = findViewById(R.id.button2);

        RequestQueue queue = Volley.newRequestQueue(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                RegisterRequest registerRequest = new RegisterRequest(name, email, password, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj != null){
                                Toast.makeText(getApplicationContext(), "Register successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Register unsuccessful, jsonObj null", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Register unsuccessful, error occured", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Register unsuccessful, error occured", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(registerRequest);
            }
        });

//        tv_loginHere = (TextView) findViewById(R.id.tv_loginHere);
//        tv_loginHere.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                openLoginActivity();
//            }
//        });
    }


    public void openLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
    }
}