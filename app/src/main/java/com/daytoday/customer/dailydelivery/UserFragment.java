package com.daytoday.customer.dailydelivery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

public class UserFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    MaterialTextView usertextview,userid;
    EditText usernameEditText,userphoneEditText;
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
        username=firebaseAuth.getCurrentUser().getDisplayName().toUpperCase();
        usertextview.setText(username);
        userid.setText("ID-"+firebaseAuth.getUid());
        usernameEditText.setText(username);
        userphoneEditText.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
        return view;
    }
}
