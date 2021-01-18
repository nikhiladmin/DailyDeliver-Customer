package com.daytoday.customer.dailydelivery.HomeScreen.View;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daytoday.customer.dailydelivery.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ScanFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        return view;
    }
}
