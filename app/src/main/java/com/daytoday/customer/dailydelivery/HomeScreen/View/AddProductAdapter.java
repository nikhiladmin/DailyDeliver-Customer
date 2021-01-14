package com.daytoday.customer.dailydelivery.HomeScreen.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.RealtimeDatabase;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class AddProductAdapter extends RecyclerView.Adapter<AddProductAdapter.AddProductViewHolder> {
    Context context;
    List<String> list;

    public AddProductAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AddProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.add_product,parent,false);
        return new AddProductViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull AddProductViewHolder holder, int position) {
        holder.id.setText(list.get(position));
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBuisness(list.get(position));
            }
        });
    }

    private void AddBuisness(String id) {
        DatabaseReference reference = RealtimeDatabase.getInstance().getReference();
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        reference.child("Buss_Cust_Rel").child(id).child(currentuser.getUid()).setValue(true);
        reference.child("Cust_Buss_Rel").child(currentuser.getUid()).child(id).setValue(true);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AddProductViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        MaterialCardView materialCardView;
        public AddProductViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.product_id);
            materialCardView = itemView.findViewById(R.id.searchcardview);
        }
    }
}
