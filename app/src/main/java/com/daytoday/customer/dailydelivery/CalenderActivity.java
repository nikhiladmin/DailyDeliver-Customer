package com.daytoday.customer.dailydelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.NumberPicker;

import com.google.android.gms.common.util.ScopeUtil;
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
import java.util.Iterator;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {
    MaterialCalendarView calendarView;
    String bussID,custID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        bussID  = getIntent().getStringExtra("buisness-Id");
        custID = getIntent().getStringExtra("Customer-Id");
        getSupportActionBar().setTitle("Calender");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarView = findViewById(R.id.calendar);
        DatesViewModel datesViewModel = new DatesViewModel(bussID,custID);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                CalendarDay day = CalendarDay.from(date.getYear(),date.getMonth(),date.getDay());
                AlertDialog.Builder builder = new AlertDialog.Builder(CalenderActivity.this);
                builder.setTitle("Accept").setMessage("Do you Accept The Product");;
                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SendToAccept(date,bussID,custID);
                    }
                }).setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendToReject(date,bussID,custID);
                    }
                });
                builder.show();
            }
        });

        datesViewModel.getAcceptedList().observe(this, new Observer<List<CalendarDay>>() {
            @Override
            public void onChanged(List<CalendarDay> dates) {
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,R.drawable.accepted_color,dates);
                calendarView.addDecorators(decorator);
            }
        });

        datesViewModel.getCancelledList().observe(this, new Observer<List<CalendarDay>>() {
            @Override
            public void onChanged(List<CalendarDay> dates) {
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,R.drawable.canceled_color,dates);
                calendarView.addDecorators(decorator);
            }
        });

        datesViewModel.getPendingList().observe(this, new Observer<List<CalendarDay>>() {
            @Override
            public void onChanged(List<CalendarDay> dates) {
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,R.drawable.pending_color,dates);
                Log.i("msg","called");
                calendarView.addDecorator(decorator);
            }
        });
    }

    private void sendToReject(CalendarDay day,String bussID,String custID) {
        HashMap<String,String> value = new HashMap<>();
        String currDate = "" + day.getYear() + day.getMonth() + day.getDay();
        FirebaseUser curruser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        value.put("Year", String.valueOf(day.getYear()));
        value.put("Mon", String.valueOf(day.getMonth()));
        value.put("Day", String.valueOf(day.getDay()));

        reference.child("Buss-Cust-DayWise").child(bussID).child(custID).child("Pending")
                .child(currDate)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null)
                        {
                            String quantity = dataSnapshot.child("quantity").getValue().toString();
                            value.put("quantity",quantity);
                            reference.child("Buss-Cust-DayWise").child(bussID).child(custID).child("Rejected")
                                    .child(currDate).setValue(value);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        reference.child("Buss-Cust-DayWise").child(bussID).child(custID).child("Pending")
                .child(currDate).removeValue();
    }

    private void SendToAccept(CalendarDay day,String bussID,String custID) {
        HashMap<String,String> value = new HashMap<>();
        String currDate = "" + day.getYear() + day.getMonth() + day.getDay();
        FirebaseUser curruser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        value.put("Year", String.valueOf(day.getYear()));
        value.put("Mon", String.valueOf(day.getMonth()));
        value.put("Day", String.valueOf(day.getDay()));

        reference.child("Buss-Cust-DayWise").child(bussID).child(custID).child("Pending")
                .child(currDate)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null)
                        {
                            String quantity = dataSnapshot.child("quantity").getValue().toString();
                            value.put("quantity",quantity);
                            reference.child("Buss-Cust-DayWise").child(bussID).child(custID).child("Accepted")
                                    .child(currDate).setValue(value);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        reference.child("Buss-Cust-DayWise").child(bussID).child(custID).child("Pending")
                .child(currDate).removeValue();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
