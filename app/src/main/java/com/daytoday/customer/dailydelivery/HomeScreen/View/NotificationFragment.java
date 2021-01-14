package com.daytoday.customer.dailydelivery.HomeScreen.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.daytoday.customer.dailydelivery.NotificationUI.Notification;
import com.daytoday.customer.dailydelivery.NotificationUI.NotificationAdapter;
import com.daytoday.customer.dailydelivery.NotificationUI.NotificationViewModel;
import com.daytoday.customer.dailydelivery.NotificationUI.NotificationViewModelFactory;
import com.daytoday.customer.dailydelivery.R;

public class NotificationFragment extends Fragment {

    public NotificationFragment() {
    }

    NotificationViewModel viewModel;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView = view.findViewById(R.id.notification_recycler);
        swipeRefreshLayout = view.findViewById(R.id.pullToRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final NotificationAdapter notificationAdapter = new NotificationAdapter(getActivity());

        viewModel = new ViewModelProvider(this, new NotificationViewModelFactory(getActivity().getApplication(), "1212")).get(NotificationViewModel.class);
        viewModel.pagedListLiveData.observe(getActivity(), new Observer<PagedList<Notification>>() {
            @Override
            public void onChanged(PagedList<Notification> notifications) {
                notificationAdapter.submitList(notifications);
            }
        });
        recyclerView.setAdapter(notificationAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                viewModel.refresh();
            }
        });
        return view;
    }
}
