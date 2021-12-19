package fatmaJmartKD.jmart_android;

/**
 * Class AboutMeActivity - Mengatur jalannya halaman about me
 *
 * @author Fatma Putri Ramadhani
 *
 */


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
    private Button buttonTopUp, buttonInvoice;
    private EditText topUpInput, registerStoreName, registerStoreAddress, registerStorePhone;
    private CardView cardViewStoreInfo;
    private CardView cardViewRegisterStore;
    private Button buttonRegisterStoreCV, buttonCancelRegisterStore, buttonRegisterStore;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me2);

        RequestQueue queue = Volley.newRequestQueue(this);

        nameInfoDetail = findViewById(R.id.tvName);
        emailInfoDetail = findViewById(R.id.tvEmail);
        balanceInfoDetail = findViewById(R.id.tvBalance);
        topUpInput = findViewById(R.id.fieldTopUp);
        nameInfoDetail.setText(LoginActivity.getLoggedAccount().name);
        emailInfoDetail.setText(LoginActivity.getLoggedAccount().email);
        balanceInfoDetail.setText(String.valueOf(LoginActivity.getLoggedAccount().balance));

        cardViewRegisterStore = findViewById(R.id.cv_registerStore);
        cardViewStoreInfo = findViewById(R.id.cv_storeExists);

        buttonRegisterStoreCV = findViewById(R.id.buttonRegisterStore);
        buttonCancelRegisterStore = findViewById(R.id.buttonCancel);

        //klik button invoice
        buttonInvoice = findViewById(R.id.buttonInvoice);
        buttonInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InvoiceHistoryActivity.class);
                startActivity(intent);
            }
        });

        //klik button topup untuk menambah balance
        buttonTopUp = findViewById(R.id.buttonTopUp);
        buttonTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String balance = topUpInput.getText().toString();
                String URL = "http://192.168.100.6:8080/account/" + LoginActivity.getLoggedAccount().id+ "/topUp";
                StringRequest topUpRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LoginActivity.reloadLoggedAccount(response);
                        try {
                            Toast.makeText(getApplicationContext(), "Top Up successful", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(getIntent());
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

        //jika terdapat store, maka menampilkan informasi store
        if (LoginActivity.getLoggedAccount().store!=null){
            buttonRegisterStoreCV.setVisibility(View.GONE);
            cardViewStoreInfo.setVisibility(View.VISIBLE);
            nameInfoStore = findViewById(R.id.tvStoreName);
            addressInfoStore = findViewById(R.id.tvStoreAddress);
            phoneInfoStore = findViewById(R.id.tvStorePhoneNumber);
            nameInfoStore.setText(LoginActivity.getLoggedAccount().store.name);
            addressInfoStore.setText(LoginActivity.getLoggedAccount().store.address);
            phoneInfoStore.setText(LoginActivity.getLoggedAccount().store.phoneNumber);
        }

        //jika tidak ada store, maka dapat melakukan register store
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
        buttonRegisterStore = findViewById(R.id.buttonRegister);
        registerStoreName = findViewById(R.id.fieldNameStore);
        registerStoreAddress = findViewById(R.id.fieldAddressStore);
        registerStorePhone = findViewById(R.id.fieldPhoneNumberStore);
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