package com.daytoday.customer.dailydelivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ProductFragment extends Fragment {
    RecyclerView product_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        product_list = view.findViewById(R.id.product_list);
        product_list.setHasFixedSize(true);
        product_list.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductViewModel viewModel = new ProductViewModel();
        viewModel.getProduct().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                ProductAdapter adapter = new ProductAdapter(products,getContext());
                product_list.setAdapter(adapter);
            }
        });
        return view;
    }
}
