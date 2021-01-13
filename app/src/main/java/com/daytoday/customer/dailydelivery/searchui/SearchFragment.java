package com.daytoday.customer.dailydelivery.searchui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.searchui.SearchAdapter;
import com.daytoday.customer.dailydelivery.searchui.SearchViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    ImageButton searchbtn;
    EditText searchField;
    SearchViewModel searchViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.products);
        searchbtn = view.findViewById(R.id.search_button);
        searchField = view.findViewById(R.id.search_field);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchViewModel = new ViewModelProvider(this, new SearchViewModelFactory(getActivity().getApplication(),null,FirebaseAuth.getInstance().getUid())).get(SearchViewModel.class);
        final SearchAdapter adapter=new SearchAdapter(getActivity());
        searchViewModel.pagedListLiveData.observe(getActivity(), new Observer<PagedList<Business>>() {
            @Override
            public void onChanged(PagedList<Business> businesses) {
                adapter.submitList(businesses);
            }
        });
        recyclerView.setAdapter(adapter);
        searchViewModel.filterTextAll.setValue("");
        searchbtn.setOnClickListener(v->{
            String searchQuery = searchField.getText().toString();
            searchViewModel.filterTextAll.setValue(searchQuery);
        });
        return view;
    }
}
