package fatmaJmartKD.jmart_android;

/**
 * Class MyRecyclerViewAdapter - Mengatur jalannya halaman pada recycler view di main page
 *
 * @author Fatma Putri Ramadhani
 *
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fatmaJmartKD.jmart_android.model.Product;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Product> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    MyRecyclerViewAdapter(Context context, List<Product> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product productName = mData.get(position);
        holder.myTextView.setText(productName.toString());
        holder.rv_tv_productPrice.setText(String.valueOf(Math.round(productName.price * 100.00)/100.00));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void refresh(List<Product> data) {
        this.mData = data;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        TextView rv_tv_productPrice;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvProductName);
            rv_tv_productPrice = itemView.findViewById(R.id.rv_tv_productPrice);
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

