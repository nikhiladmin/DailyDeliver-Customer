package com.daytoday.customer.dailydelivery.HomeScreen.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.daytoday.customer.dailydelivery.HomeScreen.Model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
                   firestore.collection("Buss_Info").document(currentSnapshot.getKey()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                           DocumentSnapshot documentSnapshot = task.getResult();
                           String productName = documentSnapshot.get("Name").toString();
                           String MonthDay = documentSnapshot.get("M_Or_D").toString();
                           String price = documentSnapshot.get("Price").toString();
                           String image = documentSnapshot.get("productImg").toString();
                           String cust_cout = documentSnapshot.get("No_Of_Cust").toString();
                           String PhoneNo = documentSnapshot.get("PhoneNo").toString();
                           String Address = documentSnapshot.get("Address").toString();
                           list.add(new Product(productName,MonthDay,price,image,cust_cout,PhoneNo,Address,currentSnapshot.getKey()));
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
