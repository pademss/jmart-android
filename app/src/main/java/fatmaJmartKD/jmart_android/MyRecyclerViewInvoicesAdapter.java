package fatmaJmartKD.jmart_android;

/**
 * Class MyRecyclerViewInvoicesAdapter - Mengatur jalannya halaman about me
 *
 * @author Fatma Putri Ramadhani
 *
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;

import fatmaJmartKD.jmart_android.model.Payment;
import fatmaJmartKD.jmart_android.model.Product;

public class MyRecyclerViewInvoicesAdapter extends RecyclerView.Adapter<MyRecyclerViewInvoicesAdapter.ViewHolder> {
    private static final Gson gson = new Gson();
    private List<Payment> mData;
    private List<Product> invoiceProducts;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;



    MyRecyclerViewInvoicesAdapter(Context context, List<Payment> data, List<Product> invoiceProducts) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.invoiceProducts = invoiceProducts;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row2, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //untuk mendapatkan data
        Payment paymentName = mData.get(position);
        Product productName = invoiceProducts.get(position);
        if(productName.toString().length() >= 38){
            holder.invoiceName.setTextSize(12.0f);
            holder.invoiceName.setMaxEms(10);
        }else{ }
        holder.invoiceName.setText(productName.toString());
        holder.id = paymentName.id;
        holder.invoiceStatus.setText(paymentName.history.get(paymentName.history.size() - 1).status.toString());
        holder.invoiceAmount.setText("x"+String.valueOf(paymentName.productCount));
        holder.invoiceTotalPrice.setText(String.valueOf(Math.round(productName.price * paymentName.productCount * 100.00)/100.00));
        switch (paymentName.shipment.plan){
            case 0:
                holder.invoiceShipmentPlan.setText(("INSTANT"));
                break;
            case 1:
                holder.invoiceShipmentPlan.setText(("SAME_DAY"));
                break;
            case 2:
                holder.invoiceShipmentPlan.setText(("NEXT_DAY"));
                break;
            case 4:
                holder.invoiceShipmentPlan.setText(("KARGO"));
                break;
            default:
                holder.invoiceShipmentPlan.setText(("REGULER"));
                break;
        }
        holder.invoiceBuyer.setText("Buyer ID: " + paymentName.buyerId);
        holder.invoiceShipmentAddress.setText(paymentName.shipment.address);
        if (paymentName.history.get(paymentName.history.size() - 1).status.toString() != "WAITING_CONFIRMATION") {
            holder.btnAcceptInvoice.setVisibility(View.GONE);
            holder.btnCancelTransaction.setVisibility(View.GONE);
        }
        if (paymentName.history.get(paymentName.history.size() - 1).status.toString() == "ON_PROGRESS") {
            holder.btnSubmitReceipt.setVisibility(View.VISIBLE);
            holder.receipt.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    //Refresh list apabila terdapat update
    public void refresh(List<Payment> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void refreshProducts(List<Product> invoiceProducts) {
        this.invoiceProducts = invoiceProducts;
        notifyDataSetChanged();
    }

    // scrolled page
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView invoiceName;
        TextView invoiceStatus;
        TextView invoiceAmount;
        TextView invoiceTotalPrice;
        TextView invoiceShipmentPlan;
        TextView productId;
        TextView invoiceBuyer;
        TextView invoiceShipmentAddress;
        ImageView img_productInvoice;
        EditText receipt;
        Button btnAcceptInvoice, btnCancelTransaction;
        Button btnSubmitReceipt;
        int id;

        ViewHolder(View itemView) {
            super(itemView);
            invoiceName = itemView.findViewById(R.id.productNameInvoice);
            invoiceStatus = itemView.findViewById(R.id.statusInvoice);
            invoiceAmount = itemView.findViewById(R.id.amountInvoice);
            invoiceTotalPrice = itemView.findViewById(R.id.totalInvoice);
            invoiceShipmentPlan = itemView.findViewById(R.id.shipmentPlanInvoice);
            invoiceBuyer = itemView.findViewById(R.id.buyerIdInvoice);
            invoiceShipmentAddress = itemView.findViewById(R.id.addressInvoice);
            btnAcceptInvoice = itemView.findViewById(R.id.buttonAcceptInvoice);
            btnCancelTransaction = itemView.findViewById(R.id.buttonCancelInvoce);
            receipt = itemView.findViewById(R.id.fieldReceipt);
            btnSubmitReceipt = itemView.findViewById(R.id.buttonSubmitReceipt);

            //untuk menaccept pembelian
            btnAcceptInvoice.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    StringRequest cancelRequest = new StringRequest(Request.Method.POST, "http://192.168.100.6:8080/payment/"+id+"/accept", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Toast.makeText(mInflater.getContext(), "Accept successful", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(mInflater.getContext(), "Accept unsuccessful", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mInflater.getContext(), "Accept unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(mInflater.getContext());
                    queue.add(cancelRequest);
                }
            });

            //untuk mencancle pembelian
            btnCancelTransaction.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    StringRequest cancelRequest = new StringRequest(Request.Method.POST, "http://192.168.100.6:8080/payment/"+id+"/cancel", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Toast.makeText(mInflater.getContext(), "Cancel successful", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(mInflater.getContext(), "Cancel unsuccessful", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mInflater.getContext(), "Cancel unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(mInflater.getContext());
                    queue.add(cancelRequest);
                }
            });

            //untuk mensubmit resi
            btnSubmitReceipt.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String receiptText = receipt.getText().toString();
                    StringRequest cancelRequest = new StringRequest(Request.Method.POST, "http://192.168.100.6:8080/payment/"+id+"/submit?receipt="+receiptText, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Toast.makeText(mInflater.getContext(), "Submit successful", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(mInflater.getContext(), "Submit unsuccessful", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mInflater.getContext(), "Submit unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(mInflater.getContext());
                    queue.add(cancelRequest);
                }
            });
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id) {
        return mData.get(id).toString();
    }
    int getClickedItemId(int id){ return mData.get(id).id;}

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

