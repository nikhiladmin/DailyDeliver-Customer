package com.daytoday.customer.dailydelivery.HomeScreen.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.daytoday.customer.dailydelivery.CalendarBottomSheet;
import com.daytoday.customer.dailydelivery.Dates;
import com.daytoday.customer.dailydelivery.HomeScreen.ViewModel.DatesViewModel;
import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.AppConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalenderActivity extends AppCompatActivity {
    MaterialCalendarView calendarView;
    String bussID,custID,bussCustId;
    ApiInterface apiInterface;
    List<Dates> pendingDates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        bussCustId = "" + getIntent().getStringExtra("buisness-customer-Id");
        Log.i("msg","shuvam " + bussCustId);
        bussID  = getIntent().getStringExtra("buisness-Id");
        custID = getIntent().getStringExtra("Customer-Id");
        getSupportActionBar().setTitle("Calender");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarView = findViewById(R.id.calendar);
        DatesViewModel datesViewModel = new DatesViewModel(bussCustId);
        apiInterface = Client.getClient().create(ApiInterface.class);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                CalendarDay day = CalendarDay.from(date.getYear(),date.getMonth(),date.getDay());
//                AlertDialog.Builder builder = new AlertDialog.Builder(CalenderActivity.this);
//                builder.setTitle("Accept").setMessage("Do you Accept The Product");;
//                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        SendToAccept(date,bussCustId);
//                    }
//                }).setNegativeButton("Reject", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        sendToReject(date,bussCustId);
//                    }
//                });
//                builder.show();
                for(int i=0;i<pendingDates.size();i++)
                {
                    Log.e("TAG",""+pendingDates.get(i).getDate()+"  "+date);
                    if(pendingDates.get(i).getDate().equals(date))
                    {
                        Log.e("TAG","GOT IT");
                    }
                }
                CalendarBottomSheet calendarBottomSheet = new CalendarBottomSheet(pendingDates);
                Bundle bundle = new Bundle();
                calendarBottomSheet.setArguments(bundle);
                calendarBottomSheet.show(getSupportFragmentManager(),"CalendarActivity");
            }
        });
        datesViewModel.getAcceptedList().observe(this, new Observer<List<Dates>>() {
            @Override
            public void onChanged(List<Dates> dates) {
                //todo string to date change
                datesViewModel.rejectDataFromApi().observe(CalenderActivity.this, rejecteddates -> {
                    CircleDecorator rejectDeco = new CircleDecorator(CalenderActivity.this,R.drawable.canceled_color,rejecteddates);
                    calendarView.addDecorator(rejectDeco);
                });
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,R.drawable.accepted_color,dates);
                calendarView.addDecorators(decorator);
            }
        });




        datesViewModel.getCancelledList().observe(this, new Observer<List<Dates>>() {
            @Override
            public void onChanged(List<Dates> dates) {
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,R.drawable.canceled_color,dates);
                calendarView.addDecorators(decorator);
            }
        });

        datesViewModel.getPendingList().observe(this, new Observer<List<Dates>>() {
            @Override
            public void onChanged(List<Dates> dates) {
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this, R.drawable.pending_color, dates);
                calendarView.addDecorator(decorator);
                pendingDates = dates;
            }
        });
    }

    private void sendToReject(CalendarDay day,String busscustID) {
        Callback<YesNoResponse> addRejectedRequestCall = new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {
                Log.i("message","Response Successful " + response.body().getMessage());
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {
                Log.i(AppConstants.ERROR_LOG,"Some Error Occurred in CalenderActivity Error is : {" + t.getMessage() + " }");
            }
        };
        HashMap<String,String> value = new HashMap<>();
        String currDate = "" + day.getYear() + day.getMonth() + day.getDay();
        String databaseDate=""+day.getYear()+day.getMonth()+(day.getDay()<=9 ? "0"+day.getDay():day.getDay());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        value.put("Year", String.valueOf(day.getYear()));
        value.put("Mon", String.valueOf(day.getMonth()));
        value.put("Day", String.valueOf(day.getDay()));

        reference.child("Buss_Cust_DayWise").child(busscustID).child("Pending")
                .child(currDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null)
                        {
                            String quantity = dataSnapshot.child("quantity").getValue().toString();
                            value.put("quantity",quantity);
                            reference.child("Buss_Cust_DayWise").child(busscustID).child("Rejected")
                                    .child(currDate).setValue(value);
                            Call<YesNoResponse> addRejectedRequest = apiInterface.addRejectedRequest(busscustID,quantity,databaseDate);
                            addRejectedRequest.enqueue(addRejectedRequestCall);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        reference.child("Buss_Cust_DayWise").child(busscustID).child("Pending")
                .child(currDate).removeValue();
    }

    private void SendToAccept(CalendarDay day,String busscustID) {
        //TODO Accept The Buisness
        Callback<YesNoResponse> addAcceptedCallback = new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {
                Log.i("message","Response Successful " + response.body().getMessage());
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {
                Log.i(AppConstants.ERROR_LOG,"Some Error Occurred in CalenderActivity Error is : {" + t.getMessage() + " }");
            }
        };
        HashMap<String,String> value = new HashMap<>();
        String currDate = "" + day.getYear() + day.getMonth() + day.getDay();
        String databaseDate=""+day.getYear()+day.getMonth()+(day.getDay()<= 9 ? "0"+day.getDay():day.getDay());
        FirebaseUser curruser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        value.put("Year", String.valueOf(day.getYear()));
//        value.put("Mon", String.valueOf(day.getMonth()));
//        value.put("Day", String.valueOf(day.getDay()));

        reference.child("Buss_Cust_DayWise").child(busscustID).child("Pending")
                .child(currDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null)
                        {
                            String quantity = dataSnapshot.child("quantity").getValue().toString();
                            //value.put("quantity",quantity);
                            //reference.child("Buss_Cust_DayWise").child(bussID).child(custID).child("Accepted")
                            //        .child(currDate).setValue(value);
                            Log.i("msg","done " + quantity+currDate);
                            Call<YesNoResponse> addAcceptedRequest = apiInterface.addAcceptedRequest(busscustID,quantity,databaseDate);
                            addAcceptedRequest.enqueue(addAcceptedCallback);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        reference.child("Buss_Cust_DayWise").child(busscustID).child("Pending")
                .child(currDate).removeValue();
        reference.child("Buss_Cust_DayWise").child(busscustID).child("Rejected")
                .child(currDate).removeValue();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
