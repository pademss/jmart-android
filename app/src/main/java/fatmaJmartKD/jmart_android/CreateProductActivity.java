package fatmaJmartKD.jmart_android;

/**
 * Class CreateProductActivity - Mengatur jalannya halaman create product
 *
 * @author Fatma Putri Ramadhani
 *
 */

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import fatmaJmartKD.jmart_android.request.CreateProductRequest;

public class CreateProductActivity extends AppCompatActivity {
    private Button btnCreateProduct, btnCancelProduct;
    private EditText et_createProductName, et_createProductPrice, et_createProductWeight, et_createProductDiscount;
    private RadioGroup radio_conditionList;
    private boolean newProductCondition = true;
    private Spinner spinner_createCategory, spinner_createShipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        RequestQueue queue = Volley.newRequestQueue(this);

        et_createProductName = findViewById(R.id.fieldNameProduct);
        et_createProductPrice = findViewById(R.id.fieldPriceProduct);
        et_createProductWeight = findViewById(R.id.fieldWeightProduct);
        et_createProductDiscount = findViewById(R.id.fieldDiscountProduct);
        spinner_createCategory = findViewById(R.id.spinnerCategory);
        spinner_createShipment = findViewById(R.id.spinnerShipmentPlan);

        //radio button untuk condition
        radio_conditionList = findViewById(R.id.radio_conditionList);
        radio_conditionList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rbNew:
                        newProductCondition = true;
                        break;
                    case R.id.rbUsed:
                        newProductCondition = false;
                        break;
                }
            }
        });

        //klik button create product
        btnCreateProduct = findViewById(R.id.buttonCreate);
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountId = String.valueOf(LoginActivity.getLoggedAccount().id);
                String productName = et_createProductName.getText().toString();
                String productPrice = et_createProductPrice.getText().toString();
                String productWeight = et_createProductWeight.getText().toString();
                String productDiscount = et_createProductDiscount.getText().toString();
                String productCategory = spinner_createCategory.getSelectedItem().toString();
                String productShipment = spinner_createShipment.getSelectedItem().toString();

                //mengubah shipment plan menjadi byte
                switch (productShipment) {
                    case "INSTANT":
                        productShipment = String.valueOf(0);
                        break;
                    case "SAME_DAY":
                        productShipment = String.valueOf(1);
                        break;
                    case "NEXT_DAY":
                        productShipment = String.valueOf(2);
                        break;
                    case "REGULER":
                        productShipment = String.valueOf(3);
                        break;
                    case "KARGO":
                        productShipment = String.valueOf(4);
                        break;
                    default:
                        productShipment = String.valueOf(3);
                        break;
                }
                System.out.println(productCategory + "  " + productShipment);

                //mengirimkan ke create product request
                CreateProductRequest createProductRequest = new CreateProductRequest(accountId, productName, productWeight,
                        String.valueOf(newProductCondition), productPrice, productDiscount, productCategory, productShipment,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Toast.makeText(getApplicationContext(), "Create product successful", Toast.LENGTH_LONG).show();
                                    finish();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Create product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Create product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(createProductRequest);
            }
        });

        //klik button untuk cancel
        btnCancelProduct = findViewById(R.id.buttonCancelProduct);
        btnCancelProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}