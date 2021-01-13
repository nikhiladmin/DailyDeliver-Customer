package com.daytoday.customer.dailydelivery.HomeScreen.View;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daytoday.customer.dailydelivery.HomeScreen.Model.Product;
import com.daytoday.customer.dailydelivery.HomeScreen.ViewModel.ProductViewModel;
import com.daytoday.customer.dailydelivery.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ProductFragment extends Fragment {
    RecyclerView product_list;
    ProgressBar progressBar;
    View view;
    ProductViewModel viewModel;
    View noBussView;

    public ProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_product, container, false);
        }
        product_list = view.findViewById(R.id.product_list);
        if (!isConnectedNetwork(getActivity())) {
            Log.e("TAG", "onCreateView: " + isConnectedNetwork(getActivity()));
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Check your Internet Connection", Snackbar.LENGTH_LONG).show();
        }
        progressBar = view.findViewById(R.id.prgbar);
        product_list.setHasFixedSize(true);
        product_list.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        progressBar.setVisibility(View.VISIBLE);
        noBussView = view.findViewById(R.id.noBusslayout);
        viewModel.getProduct().observe(getActivity(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                ProductAdapter adapter = new ProductAdapter(products, getContext());
                product_list.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                if(products.size()<=0)
                {
                    product_list.setVisibility(View.GONE);
                    noBussView.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }


    public static boolean isConnectedNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
