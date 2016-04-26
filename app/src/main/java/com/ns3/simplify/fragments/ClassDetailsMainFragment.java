package com.ns3.simplify.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ns3.simplify.R;

/**
 * Created by ASUS on 26-Apr-16.
 */
public class ClassDetailsMainFragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_details_main, container, false);
    }

}
