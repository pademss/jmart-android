package fatmaJmartKD.jmart_android;

/**
 * Class PaymentActivity - Mengatur jalannya halaman payment
 *
 * @author Fatma Putri Ramadhani
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import fatmaJmartKD.jmart_android.model.Account;
import fatmaJmartKD.jmart_android.model.Product;
//import fatmaJmartKD.jmart_android.request.CreatePaymentRequest;
import fatmaJmartKD.jmart_android.request.CreatePaymentRequest;
import fatmaJmartKD.jmart_android.request.RequestFactory;

public class PaymentActivity extends AppCompatActivity {
    public static final String EXTRA_AMOUNT = "fatmaJmartKD.jmart_android.EXTRA_AMOUNT";
    public static final String EXTRA_SHIPMENTADDRESS = "fatmaJmartKD.jmart_android.EXTRA_SHIPMENTADDRESS";
    private Product fetchedProduct;
    private static final Gson gson = new Gson();
    private Button btnSubmitPayment, btnCancelPayment;
    private TextView tv_productNamePayment, tv_pricePayment, tv_discountPayment, tv_sellerId;
    private TextView tv_totalPrice, tv_balancePayment, tv_shipmentPayment;
    private EditText et_amountPayment, et_shipmentAddress;
    private double productPrice;
    private byte shipmentPlans;
    private boolean enoughBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        RequestQueue queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        int productId = intent.getIntExtra(MainActivity.EXTRA_PRODUCTID, 0);
        productPrice = intent.getDoubleExtra(ProductDetailActivity.EXTRA_PRICE, 0);
        Account currentLogged = LoginActivity.getLoggedAccount();

        tv_productNamePayment = findViewById(R.id.productNamePayment);
        tv_pricePayment = findViewById(R.id.priceProductPayment);
        tv_discountPayment = findViewById(R.id.discountProductPayment);
        tv_sellerId = findViewById(R.id.sellerId);
        tv_totalPrice = findViewById(R.id.totalPricePayment);
        tv_balancePayment = findViewById(R.id.balancePayment);
        tv_shipmentPayment = findViewById(R.id.shipmentPayment);
        et_shipmentAddress = findViewById(R.id.fieldShipmentAddress);
        et_amountPayment = findViewById(R.id.fieldAmount);
        et_amountPayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            //menghitung jumlah product dan total harganya
            @Override
            public void afterTextChanged(Editable s) {
                int getNewAmount;
                try{
                    getNewAmount = Integer.parseInt(et_amountPayment.getText().toString());
                }catch(NumberFormatException e){
                    getNewAmount = 0;
                }
                if (!(getNewAmount > 0)) {
                    et_amountPayment.setText(String.valueOf(1));
                    tv_totalPrice.setText(String.valueOf(productPrice * 1));
                }else{
                    tv_totalPrice.setText(String.valueOf(getNewAmount * productPrice));
                }
                if(Double.parseDouble(tv_totalPrice.getText().toString()) > Double.parseDouble((tv_balancePayment.getText().toString()))){
                    enoughBalance = false;
                }
                else {
                    enoughBalance = true;
                }
            }
        });

        //mendapatkan data product sesuai idnya dari requestfactory
        StringRequest fetchProductDataRequest = RequestFactory.getById("product", productId, new Response.Listener<String>() {
            int amount = Integer.parseInt(et_amountPayment.getText().toString());
            @Override
            public void onResponse(String response) {
            fetchedProduct = gson.fromJson(response, Product.class);
            tv_productNamePayment.setText(fetchedProduct.name);
            double productPrice = Math.round((fetchedProduct.price * 100.00)/100.00);
            double productDiscount = Math.round((fetchedProduct.discount * 100.00)/100.0);
            tv_pricePayment.setText(String.valueOf(productPrice));
            tv_discountPayment.setText(String.valueOf(productDiscount));
            tv_sellerId.setText(String.valueOf(fetchedProduct.accountId));
            tv_totalPrice.setText(String.valueOf(amount * (productPrice - productDiscount)));
            tv_balancePayment.setText(String.valueOf(currentLogged.balance));
            shipmentPlans = fetchedProduct.shipmentPlans;
                switch (shipmentPlans){
                    case 0:
                        tv_shipmentPayment.setText("INSTANT");
                        break;
                    case 1:
                        tv_shipmentPayment.setText("SAME_DAY");
                        break;
                    case 2:
                        tv_shipmentPayment.setText("NEXT_DAY");
                        break;
                    case 4:
                        tv_shipmentPayment.setText("KARGO");
                        break;
                    default:
                        tv_shipmentPayment.setText("REGULER");
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        queue.add(fetchProductDataRequest);
        btnSubmitPayment = findViewById(R.id.btnSubmitPayment);
        btnCancelPayment = findViewById(R.id.btnCancelPayment);

        //melakukan pembelian
        btnSubmitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(EXTRA_AMOUNT, Integer.parseInt(et_amountPayment.getText().toString()));
                if(isEmpty(et_shipmentAddress)){
                    Toast.makeText(getApplicationContext(), "Shipment address can't be empty.", Toast.LENGTH_LONG).show();
                }
                if(enoughBalance == false){
                    Toast.makeText(getApplicationContext(), "Payment unsuccessful. Your balance is not enough", Toast.LENGTH_LONG).show();
                }
                else{
                    intent.putExtra(EXTRA_SHIPMENTADDRESS, et_shipmentAddress.getText().toString());
                    CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest(String.valueOf(LoginActivity.getLoggedAccount().id), String.valueOf(productId), et_amountPayment.getText().toString(), et_shipmentAddress.getText().toString(), String.valueOf(shipmentPlans),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Toast.makeText(getApplicationContext(), "Payment has been submitted. Waiting seller's response", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Create payment unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Create payment unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                                }
                            });
                    queue.add(createPaymentRequest);
                }
            }
        });

        //button cancel
        btnCancelPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}