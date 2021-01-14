package com.daytoday.customer.dailydelivery.NotificationUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.AppUtils;
import com.daytoday.customer.dailydelivery.Utilities.Request;
import com.daytoday.customer.dailydelivery.searchui.Business;
import com.daytoday.customer.dailydelivery.searchui.SearchAdapter;
import com.google.android.material.textview.MaterialTextView;

public class NotificationAdapter extends PagedListAdapter<Notification, NotificationAdapter.NotificationViewHolder> {
    public static final DiffUtil.ItemCallback<Notification> diffcall = new DiffUtil.ItemCallback<Notification>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
            return true;
        }
    };
    Context context;

    public NotificationAdapter(Context context) {
        super(diffcall);
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        String from = getItem(position).getFrom();
        String quantity = getItem(position).getQuantity();
        String name = getItem(position).getName();
        String time = getItem(position).getTime();
        String status = "";
        String currStatus = getItem(position).getStatus();
        if (currStatus.equals("1")) {
            status = Request.PENDING;
            holder.notiStatusText.setText(Request.PENDING);
            holder.notiStatusText.setBackground(context.getDrawable(R.drawable.rounded_background_pending));
        } else if (currStatus.equals("0")) {
            status = Request.ACCEPTED;
            holder.notiStatusText.setText(Request.ACCEPTED);
            holder.notiStatusText.setBackground(context.getDrawable(R.drawable.rounded_background_accepted));
        } else if (currStatus.equals("-1")) {
            status = Request.REJECTED;
            holder.notiStatusText.setText(Request.REJECTED);
            holder.notiStatusText.setBackground(context.getDrawable(R.drawable.rounded_background_rejected));
        } else {
            status = "Connected";
            holder.notiStatusText.setVisibility(View.GONE);
        }
        holder.t1.setText(context.getResources().getString(R.string.notificationMsg, from, quantity, name, status, time));
        AppUtils.getAgoTime(getItem(position).getTime());
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView t1, notiStatusText;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.notification_msg);
            notiStatusText = itemView.findViewById(R.id.statusText);
        }
    }
}
