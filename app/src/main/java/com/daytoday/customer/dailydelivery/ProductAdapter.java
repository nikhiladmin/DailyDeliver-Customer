package com.daytoday.customer.dailydelivery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    List<Product> buss_list;
    Context context;

    public ProductAdapter(List<Product> buss_list, Context context) {
        this.buss_list = buss_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.single_product,parent,false);
        return new ProductViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.buss_name.setText(buss_list.get(position).getProductName());
        holder.buss_add.setText(buss_list.get(position).getAddress());
        holder.price.setText(buss_list.get(position).getPrice() + " /" +buss_list.get(position).getMonthDay());
        holder.customers.setText("( " + buss_list.get(position).getCust_cout() + " Customers )");
        holder.call_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+buss_list.get(position).getPhoneNo()));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buss_list.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        Button buss_status_btn;
        TextView buss_name,buss_add,price,customers;
        ImageView buss_img,call_img;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            buss_add = itemView.findViewById(R.id.buss_address);
            price = itemView.findViewById(R.id.price);
            customers = itemView.findViewById(R.id.customers);
            buss_name = itemView.findViewById(R.id.buss_name);
            buss_img = itemView.findViewById(R.id.buss_img);
            buss_status_btn = itemView.findViewById(R.id.buss_status_btn);
            call_img = itemView.findViewById(R.id.call_buss_btn);
        }
    }
}
