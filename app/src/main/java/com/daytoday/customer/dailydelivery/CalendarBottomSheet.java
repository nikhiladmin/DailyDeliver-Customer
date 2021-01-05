package com.daytoday.customer.dailydelivery;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.daytoday.customer.dailydelivery.HomeScreen.Model.Product;
import com.daytoday.customer.dailydelivery.HomeScreen.Model.Transaction;
import com.daytoday.customer.dailydelivery.HomeScreen.View.CalenderActivity;
import com.daytoday.customer.dailydelivery.Utilities.FirebaseUtils;
import com.daytoday.customer.dailydelivery.Utilities.Request;
import com.daytoday.customer.dailydelivery.databinding.BottomsheetCalendarDetailsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CalendarBottomSheet extends BottomSheetDialogFragment {

    BottomsheetCalendarDetailsBinding binding;
    Transaction transaction;
    Product product;
    CalendarDay date;
    public CalendarBottomSheet(Transaction transaction,@NonNull CalendarDay date) {
        this.transaction = transaction;
        this.date = date;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.bottomsheet_calendar_details,container,false);
        this.product = getArguments().getParcelable(CalenderActivity.CURRENT_PRODUCT);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(transaction!=null)
        {
            binding.quantity.setText(""+transaction.getQuantity());
            binding.totalPrice.setText(product.getPrice()+" / "+product.getdOrM());
            Date parseDate = null;
            try {
                parseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(transaction.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat formatter=new SimpleDateFormat("E,dd MMM yyyy HH:mm:ss");
            String s= formatter.format(parseDate);
            binding.newReqMade.setText("Request Made on "+s);


            if(transaction.getStatus().equals(Request.ACCEPTED))
            {
                binding.statusText.setText(Request.ACCEPTED);
                binding.statusText.setBackground(getActivity().getDrawable(R.drawable.rounded_background_accepted));
                binding.acceptButton.setEnabled(false);
                binding.rejectButton.setEnabled(false);
            }else if(transaction.getStatus().equals(Request.PENDING))
            {
                binding.statusText.setText(Request.PENDING);
                binding.statusText.setBackground(getActivity().getDrawable(R.drawable.rounded_background_pending));
            }else if(transaction.getStatus().equals(Request.REJECTED))
            {
                binding.statusText.setText(Request.REJECTED);
                binding.statusText.setBackground(getActivity().getDrawable(R.drawable.rounded_background_rejected));
                binding.acceptButton.setEnabled(false);
                binding.rejectButton.setEnabled(false);
            }
        }
        binding.acceptButton.setOnClickListener(v->{
            sendToAccept(date,transaction);
            binding.acceptButton.setEnabled(false);
            binding.rejectButton.setEnabled(false);
            binding.statusText.setText(Request.ACCEPTED);
            binding.statusText.setBackground(getActivity().getDrawable(R.drawable.rounded_background_accepted));
        });
        binding.rejectButton.setOnClickListener(v->{
            sendToReject(date,transaction);
            binding.acceptButton.setEnabled(false);
            binding.rejectButton.setEnabled(false);
            binding.statusText.setText(Request.REJECTED);
            binding.statusText.setBackground(getActivity().getDrawable(R.drawable.rounded_background_rejected));
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendToReject(CalendarDay day, Transaction transaction) {
        HashMap<String,String> value = FirebaseUtils.getValueMapOfRequest(day,transaction.getQuantity(), Request.REJECTED);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(product.getUniqueId().toString());
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day,reference,Request.REJECTED);
        FirebaseUtils.decrementAccToReq(day,reference,Request.PENDING);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendToAccept(CalendarDay day, Transaction transaction) {
        HashMap<String,String> value = FirebaseUtils.getValueMapOfRequest(day,transaction.getQuantity(), Request.ACCEPTED);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(product.getUniqueId().toString());
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day,reference,Request.ACCEPTED);
        FirebaseUtils.decrementAccToReq(day,reference,Request.PENDING);
    }
}
