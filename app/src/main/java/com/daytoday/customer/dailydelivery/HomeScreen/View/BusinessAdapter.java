package com.daytoday.customer.dailydelivery.HomeScreen.View;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.daytoday.customer.dailydelivery.HomeScreen.Model.Product;
import com.daytoday.customer.dailydelivery.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder> {
    Context context;
    List<Product> productList;
    ClickListener listener;

    public interface ClickListener{
        void addBusiness(String uid);
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public BusinessAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public BusinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_card,parent,false);
        return new BusinessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessViewHolder holder, int position) {
        holder.buss_name.setText(productList.get(position).getName());
        holder.buss_adress.setText(productList.get(position).getAddress());
        holder.buss_price.setText("₹" + productList.get(position).getPrice() + " - " +(productList.get(position).getDOrM().equals("D") ? "Daily" : "Monthly"));

        if (productList.get(position).getImgurl() != null) {
            Picasso.get()
                    .load(productList.get(position).getImgurl())
                    .resize(5000, 5000)
                    .centerCrop()
                    .into(holder.buss_img);
        }
        if(productList.get(position).getConnected())
        {
            holder.add_buss.setText("Added");
            holder.add_buss.setEnabled(false);
        }
        holder.add_buss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addBusiness(productList.get(position).getBussId());
                holder.add_buss.setText("Added");
            }
        });
        holder.find_location.setOnClickListener(view -> {
                String uri  = "https://maps.google.co.in/maps?q="+productList.get(position).getAddress();
            Intent goToAddress = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            context.startActivity(goToAddress);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class BusinessViewHolder extends RecyclerView.ViewHolder {
        ImageView buss_img;
        Button add_buss;
        TextView buss_name,buss_adress,buss_price;
        LottieAnimationView find_location;
        public BusinessViewHolder(@NonNull View itemView) {
            super(itemView);
            buss_img = itemView.findViewById(R.id.img_buss);
            add_buss = itemView.findViewById(R.id.add_buss);
            buss_name = itemView.findViewById(R.id.product_name);
            buss_adress = itemView.findViewById(R.id.product_address);
            buss_price = itemView.findViewById(R.id.product_price);
            find_location = itemView.findViewById(R.id.find_location);
        }
    }
}
