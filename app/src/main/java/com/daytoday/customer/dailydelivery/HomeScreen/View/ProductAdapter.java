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

import com.bumptech.glide.Glide;
import com.daytoday.customer.dailydelivery.HomeScreen.Model.Product;
import com.daytoday.customer.dailydelivery.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    List<Product> buss_list;
    Context context;
    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

    public ProductAdapter(List<Product> buss_list, Context context) {
        this.buss_list = buss_list;
        this.context = context;
        currUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.single_product, parent, false);
        return new ProductViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.buss_name.setText(buss_list.get(position).getName());
        holder.buss_add.setText(buss_list.get(position).getAddress());
        holder.price.setText("Rs. " + buss_list.get(position).getPrice() + " - " + buss_list.get(position).getDOrM());
        // holder.customers.setText("( " + buss_list.get(position).getCust_cout() + " Customers )");
        if (buss_list.get(position).getImgurl() != null) {
            Glide.with(context).load(buss_list.get(position).getImgurl()).error(R.drawable.box001).into(holder.buss_img);
        }
        if (buss_list.get(position).getPhoneno() != null) {
            holder.call_img.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + buss_list.get(position).getPhoneno()));
                v.getContext().startActivity(intent);
            });
        }else
        {
            holder.call_img.setVisibility(View.GONE);
        }
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(),CalenderActivity.class);
            Log.i("msg" ,buss_list.get(position).toString());
            intent.putExtra(CalenderActivity.CURRENT_PRODUCT,buss_list.get(position));
            intent.putExtra(CalenderActivity.CUSTOMER_ID,currUser.getUid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return buss_list.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        Button buss_status_btn;
        TextView buss_name, buss_add, price, customers;
        ImageView buss_img, call_img;
        MaterialCardView cardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            buss_add = itemView.findViewById(R.id.buss_address);
            price = itemView.findViewById(R.id.price);
            //customers = itemView.findViewById(R.id.customers);
            buss_name = itemView.findViewById(R.id.buss_name);
            buss_img = itemView.findViewById(R.id.buss_img);
            call_img = itemView.findViewById(R.id.call_buss_btn);
            cardView = itemView.findViewById(R.id.producardview);
        }
    }
}
