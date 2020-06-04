package com.daytoday.customer.dailydelivery;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
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

public class UserFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    MaterialTextView usertextview,userid;
    EditText usernameEditText,userphoneEditText,userAddress;
    MaterialButton button;
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
        username=firebaseAuth.getCurrentUser().getDisplayName().toUpperCase();
        usertextview.setText(username);
        userid.setText("ID-"+firebaseAuth.getUid());
        usernameEditText.setText(username);
        userphoneEditText.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
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
                        Snackbar.make(getActivity().findViewById(android.R.id.content),"Changes will take sometime to reflect.",Snackbar.LENGTH_LONG).show();
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
        getAddress();//------getting address please check here
        return view;
    }


    public void getAddress()
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth;
        firebaseAuth=FirebaseAuth.getInstance();
        Log.e("TAG", "getAddress: "+firebaseAuth.getUid() );
        firestore.collection("Cust_User_Info").document(firebaseAuth.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        String name = documentSnapshot.get("Name").toString();
                        String address = documentSnapshot.get("Address").toString();
                        String PhoneNo = documentSnapshot.get("PhoneNo").toString();
                        Log.e("TAG", "onEvent: "+ address);
                        userAddress.setText(address);
                    }
                });
    }


    public void updateData()
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("Cust_User_Info").document(FirebaseAuth.getInstance().getUid());
        Map<String,Object> updateMap=new HashMap<>();
        updateMap.put("Name",usernameEditText.getText().toString().trim());
        updateMap.put("Address",userAddress.getText().toString().trim());
        documentReference.update(updateMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                        Snackbar.make(getActivity().findViewById(android.R.id.content),"Profile Updated Successfully",Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                        Snackbar.make(getActivity().findViewById(android.R.id.content),"Profile update failed. Try Again",Snackbar.LENGTH_LONG).show();
                    }
                });
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(usernameEditText.getText().toString().trim())
                .build();
        firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");
                        }
                    }
                });
    }
}
