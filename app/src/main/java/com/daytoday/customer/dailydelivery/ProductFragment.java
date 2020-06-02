package com.daytoday.customer.dailydelivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ProductFragment extends Fragment {
    RecyclerView product_list;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        product_list = view.findViewById(R.id.product_list);
        if(!isConnectedNetwork(getActivity()))
        {
            Log.e("TAG", "onCreateView: "+isConnectedNetwork(getActivity()) );
            Snackbar.make(getActivity().findViewById(android.R.id.content),"Please Check your Internet Connection",Snackbar.LENGTH_LONG).show();
        }
        progressBar=view.findViewById(R.id.prgbar);
        product_list.setHasFixedSize(true);
        product_list.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductViewModel viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        progressBar.setVisibility(View.VISIBLE);
        viewModel.getProduct().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                ProductAdapter adapter = new ProductAdapter(products,getContext());
                product_list.setAdapter(adapter);
               progressBar.setVisibility(View.GONE);
            }
        });
        return view;
    }


    public static boolean isConnectedNetwork (Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo () != null && cm.getActiveNetworkInfo ().isConnectedOrConnecting ();
    }
}
