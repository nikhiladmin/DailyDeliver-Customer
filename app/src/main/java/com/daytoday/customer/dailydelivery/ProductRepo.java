package com.daytoday.customer.dailydelivery;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.xml.sax.helpers.LocatorImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductRepo {
    public MutableLiveData<List<Product>> requestProduct() {
        MutableLiveData<List<Product>> mutableLiveData = new MutableLiveData<>();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        //-----------------------------------------Initialise Here ---------------------------------

        reference.child("Cust_Buss_Rel").child(currUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                List<Product> list = new ArrayList<>();
                while (iterator.hasNext())
                {
                    DataSnapshot currentSnapshot = (DataSnapshot) iterator.next();
                   firestore.collection("Buss_Info").document(currentSnapshot.getKey())
                           .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                               @Override
                               public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                   String productName = documentSnapshot.get("Name").toString();
                                   String MonthDay = documentSnapshot.get("M_Or_D").toString();
                                   String price = documentSnapshot.get("Price").toString();
                                   //String image = documentSnapshot.get("").toString();
                                   String cust_cout = documentSnapshot.get("No_Of_Cust").toString();
                                   String PhoneNo = documentSnapshot.get("PhoneNo").toString();
                                   String Address = documentSnapshot.get("Address").toString();
                                   list.add(new Product(productName,MonthDay,price,"google.com",cust_cout,PhoneNo,Address,currentSnapshot.getKey()));
                                   mutableLiveData.setValue(list);
                               }
                           });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //-----------------------------------------Ends Here ---------------------------------------
        return mutableLiveData;
    }
}
