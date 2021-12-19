package fatmaJmartKD.jmart_android;

/**
 * Class ProductDetailActivity - Mengatur jalannya halaman detail product
 *
 * @author Fatma Putri Ramadhani
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import fatmaJmartKD.jmart_android.model.Product;
import fatmaJmartKD.jmart_android.request.RequestFactory;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PRICE = "fatmaJmartKD.jmart_android.EXTRA_PRICE";
    private Product fetchedProduct;
    private static final Gson gson = new Gson();
    private Button btnPurchase;
    private TextView tv_detailName, tv_detailPrice, tv_detailWeight, tv_detailDiscount, tv_detailCategory, tv_detailShipment,
                     tv_detailCondition, tv_detailAccount;
    private ImageView image_product;
    private double productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        RequestQueue queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        int productId = intent.getIntExtra(MainActivity.EXTRA_PRODUCTID, 0);

        btnPurchase = findViewById(R.id.buttonPurchase);
        tv_detailName = findViewById(R.id.nameProductDetail);
        tv_detailPrice = findViewById(R.id.priceProductDetail);
        tv_detailWeight = findViewById(R.id.weightProductDetail);
        tv_detailDiscount = findViewById(R.id.discountProductDetail);
        tv_detailCategory = findViewById(R.id.categoryProductDetail);
        tv_detailShipment = findViewById(R.id.shipmentProductDetail);
        tv_detailCondition = findViewById(R.id.conditionProductDetail);
        tv_detailAccount = findViewById(R.id.idSellerDetail);

        //mendapatkan data dari product berdasarkan requestfactory
        StringRequest fetchProductDataRequest = RequestFactory.getById("product", productId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fetchedProduct = gson.fromJson(response, Product.class);
                tv_detailName.setText(fetchedProduct.name);
                if(fetchedProduct.name.length() >= 28){
                    tv_detailName.setTextSize(18.5f);
                    tv_detailName.setMaxEms(10);
                }
                tv_detailPrice.setText(String.valueOf(Math.round(fetchedProduct.price * 100.00)/100.00));
                productPrice = Double.parseDouble(tv_detailPrice.getText().toString());
                tv_detailWeight.setText(String.valueOf(fetchedProduct.weight));
                tv_detailDiscount.setText(String.valueOf(Math.round(fetchedProduct.discount * 100.00)/100.00));
                tv_detailCategory.setText(fetchedProduct.category.toString());
                switch (fetchedProduct.shipmentPlans){
                    case 0:
                        tv_detailShipment.setText("INSTANT");
                        break;
                    case 1:
                        tv_detailShipment.setText("SAME_DAY");
                        break;
                    case 2:
                        tv_detailShipment.setText("NEXT_DAY");
                        break;
                    case 4:
                        tv_detailShipment.setText("KARGO");
                        break;
                    default:
                        tv_detailShipment.setText("REGULER");
                        break;
                }
                tv_detailCondition.setText(fetchedProduct.conditionUsed ? "Used" : "New");
                tv_detailAccount.setText(String.valueOf(fetchedProduct.accountId));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        queue.add(fetchProductDataRequest);

        //button untuk membeli product dan redirect ke class PaymentActivity
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(MainActivity.EXTRA_PRODUCTID, productId);
                intent.putExtra(EXTRA_PRICE, productPrice);
                startActivity(intent);
            }
        });
    }

}