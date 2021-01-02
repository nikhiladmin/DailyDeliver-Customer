package com.daytoday.customer.dailydelivery.searchui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daytoday.customer.dailydelivery.R;

public class SearchAdapter extends PagedListAdapter<Business,SearchAdapter.SearchViewHolder> {

    public static final DiffUtil.ItemCallback<Business> diffcall = new DiffUtil.ItemCallback<Business>() {
        @Override
        public boolean areItemsTheSame(@NonNull Business oldItem, @NonNull Business newItem) {
            return oldItem==newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Business oldItem, @NonNull Business newItem) {
            return true;
        }
    };
    Context context;
    public SearchAdapter(Context context)
    {
        super(diffcall);
        this.context=context;
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_adapter,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.t1.setText(""+getItem(position).getBussname()+" "+getItem(position).getBussuserId());
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder
    {
        TextView t1;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.search_business);
        }
    }
}
