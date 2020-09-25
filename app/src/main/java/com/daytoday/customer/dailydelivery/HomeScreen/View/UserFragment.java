package com.daytoday.customer.dailydelivery.HomeScreen.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.daytoday.customer.dailydelivery.LoginActivity.LoginPage;
import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    MaterialTextView usertextview,userid;
    EditText usernameEditText,userphoneEditText,userAddress;
    MaterialButton button,signoutbutton;
    String username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        usertextview=view.findViewById(R.id.UsersName);
        userid=view.findViewById(R.id.currId);
        usernameEditText=view.findViewById(R.id.myacc_name);
        userphoneEditText=view.findViewById(R.id.myacc_phone);
        userphoneEditText.setEnabled(false);
        userAddress=view.findViewById(R.id.myacc_address);
        button=view.findViewById(R.id.submitbutton);
        signoutbutton=view.findViewById(R.id.signout);
        username=firebaseAuth.getCurrentUser().getDisplayName().toUpperCase();
        usertextview.setText(SaveOfflineManager.getUserName(getContext()));
        userid.setText("ID-"+ SaveOfflineManager.getUserId(getContext()));
        usernameEditText.setText(SaveOfflineManager.getUserName(getContext()));
        userphoneEditText.setText(SaveOfflineManager.getUserPhoneNumber(getContext()));
        userAddress.setText(SaveOfflineManager.getUserAdress(getContext()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getActivity());
                alertDialog.setMessage("This will be reflected in all the businesses you are connected.");
                alertDialog.setTitle("You are about to modify your profile details");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateData();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content),"Profile Update Cancelled",Snackbar.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        signoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getActivity());
                alertDialog.setMessage("Do you want to logout?");
                alertDialog.setTitle("Confirm");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        getActivity().finishAffinity();
                        startActivity(new Intent(getActivity(), LoginPage.class));
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
        //getAddress();//------getting address please check here
        return view;
    }


    public void getAddress()
    {
        String address="";
        userAddress.setText(address);
    }


    public void updateData()
    {
        String name=usernameEditText.getText().toString();
        String address=userAddress.getText().toString();
        String phone=userphoneEditText.getText().toString();
        String custid=SaveOfflineManager.getUserId(getContext());
        Log.e("tag",""+name+" "+address+" "+phone+" "+custid);
        ApiInterface apiInterface= Client.getClient().create(ApiInterface.class);
        Call<YesNoResponse> updateUserInfo = apiInterface.updateCutUserDetails(name,phone,address,custid);
        updateUserInfo.enqueue(new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {
                Snackbar.make(getActivity().findViewById(android.R.id.content),"Changes will take sometime to reflect.",Snackbar.LENGTH_LONG).show();
                saveOffline(name,address,phone);
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {
                Log.e("tag",""+t);
                Snackbar.make(getActivity().findViewById(android.R.id.content),"Data Update Failed.Try Again",Snackbar.LENGTH_LONG).show();
            }
        });
    }
    private void saveOffline(String name, String adress,String phone) {
        SaveOfflineManager.setUserName(getContext(),name);
        SaveOfflineManager.setUserAdress(getContext(),adress);
        SaveOfflineManager.setUserPhoneNumber(getContext(),phone);
    }
}
