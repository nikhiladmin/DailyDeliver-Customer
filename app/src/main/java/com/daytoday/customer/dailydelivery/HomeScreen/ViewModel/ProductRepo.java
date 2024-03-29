package com.daytoday.customer.dailydelivery.HomeScreen.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.daytoday.customer.dailydelivery.HomeScreen.Model.Product;
import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.CustRelBussResponse;
import com.daytoday.customer.dailydelivery.Utilities.AppConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepo {
    public MutableLiveData<List<Product>> requestProduct() {
        MutableLiveData<List<Product>> mutableLiveData = new MutableLiveData<>();
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        ApiInterface apiInterface = Client.getClient().create(ApiInterface.class);
        //-----------------------------------------Initialise Here ---------------------------------

        /*reference.child("Cust_Buss_Rel").child(currUser.getUid()).addValueEventListener(new ValueEventListener() {
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
        });*/
        Call<CustRelBussResponse> custRelBussResponseCall = apiInterface.getRelBuss(currUser.getUid());
        custRelBussResponseCall.enqueue(new Callback<CustRelBussResponse>() {
            @Override
            public void onResponse(Call<CustRelBussResponse> call, Response<CustRelBussResponse> response) {
                mutableLiveData.setValue(response.body().getCust());
            }

            @Override
            public void onFailure(Call<CustRelBussResponse> call, Throwable t) {
                Log.e(AppConstants.ERROR_LOG,"Some Error Occurred in ProductRepo Error is : { " + t.getMessage() + " }");
            }
        });
        //-----------------------------------------Ends Here ---------------------------------------
        return mutableLiveData;
    }
}
