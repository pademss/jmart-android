package fatmaJmartKD.jmart_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fatmaJmartKD.jmart_android.request.RegisterStoreRequest;

public class AboutMeActivity extends AppCompatActivity {
    private TextView nameInfoDetail;
    private TextView emailInfoDetail;
    private TextView balanceInfoDetail;
    private TextView nameInfoStore;
    private TextView addressInfoStore;
    private TextView phoneInfoStore;
    private Button buttonTopUp;
    private EditText topUpInput, registerStoreName, registerStoreAddress, registerStorePhone;
    private CardView cardViewStoreInfo;
    private CardView cardViewRegisterStore;
    private Button buttonRegisterStoreCV, buttonCancelRegisterStore, buttonRegisterStore;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        RequestQueue queue = Volley.newRequestQueue(this);

        nameInfoDetail = findViewById(R.id.tv_userName);
        emailInfoDetail = findViewById(R.id.tv_userEmail);
        balanceInfoDetail = findViewById(R.id.tv_userBalance);
        topUpInput = findViewById(R.id.et_topUpAmount);
        nameInfoDetail.setText(LoginActivity.getLoggedAccount().name);
        emailInfoDetail.setText(LoginActivity.getLoggedAccount().email);
        balanceInfoDetail.setText(String.valueOf(LoginActivity.getLoggedAccount().balance));

        cardViewRegisterStore = findViewById(R.id.cv_registerStore);
        cardViewStoreInfo = findViewById(R.id.cv_storeExists);

        buttonRegisterStoreCV = findViewById(R.id.btnRegisterStore);
        buttonCancelRegisterStore = findViewById(R.id.btnRegisterStoreCancel);

        buttonTopUp = findViewById(R.id.btnTopUp);
        buttonTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String balance = topUpInput.getText().toString();
                String idAccount = String.valueOf(LoginActivity.getLoggedAccount().id);
                String URL = "http://192.168.100.6/account/" + idAccount + "/topUp";
                StringRequest topUpRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LoginActivity.reloadLoggedAccount(response);
                        try {
                            Toast.makeText(getApplicationContext(), "Top Up successful", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Top Up unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Top Up unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("balance", balance);
                        return params;
                    }
                };
                queue.add(topUpRequest);
            }
        });

        if (LoginActivity.getLoggedAccount().store!=null){
            buttonRegisterStoreCV.setVisibility(View.GONE);
            cardViewStoreInfo.setVisibility(View.VISIBLE);
            nameInfoStore = findViewById(R.id.tv_storeNameF);
            addressInfoStore = findViewById(R.id.tv_storeAddressF);
            phoneInfoStore = findViewById(R.id.tv_storePhoneNumberF);
            nameInfoStore.setText(LoginActivity.getLoggedAccount().store.name);
            addressInfoStore.setText(LoginActivity.getLoggedAccount().store.address);
            phoneInfoStore.setText(LoginActivity.getLoggedAccount().store.phoneNumber);
        }
        buttonRegisterStoreCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonRegisterStoreCV.setVisibility(View.INVISIBLE);
                cardViewRegisterStore.setVisibility(View.VISIBLE);
            }
        });
        buttonCancelRegisterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonRegisterStoreCV.setVisibility(View.VISIBLE);
                cardViewRegisterStore.setVisibility(View.INVISIBLE);
            }
        });
        buttonRegisterStore = findViewById(R.id.btnRegisterStoreConfirm);
        registerStoreName = findViewById(R.id.et_storeName);
        registerStoreAddress = findViewById(R.id.et_storeAddress);
        registerStorePhone = findViewById(R.id.et_storePhoneNumber);
        buttonRegisterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = registerStoreName.getText().toString();
                String address = registerStoreAddress.getText().toString();
                String phoneNumber = registerStorePhone.getText().toString();
                RegisterStoreRequest registerStoreRequest = new RegisterStoreRequest(LoginActivity.getLoggedAccount().id, name, address ,phoneNumber, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LoginActivity.insertLoggedAccountStore(response);
                        try {
                            Toast.makeText(getApplicationContext(), "Register Store successful", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Register Store unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Register Store unsuccessful, error occurred banget", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(registerStoreRequest);
            }
        });
    }



}