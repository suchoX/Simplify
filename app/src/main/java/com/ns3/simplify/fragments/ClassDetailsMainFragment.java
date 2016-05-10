package com.ns3.simplify.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ns3.simplify.ClassDetailsActivity;
import com.ns3.simplify.R;

/**
 * Created by ASUS on 26-Apr-16.
 */
public class ClassDetailsMainFragment extends Fragment
{
    View view;
    LinearLayout scanStudentsLayout,studentListLayout,addStudentLayout,exportListLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_class_details_main, container, false);
        scanStudentsLayout = (LinearLayout)view.findViewById(R.id.scan_students_layout);
        studentListLayout = (LinearLayout)view.findViewById(R.id.student_list_layout);
        addStudentLayout = (LinearLayout)view.findViewById(R.id.add_student_layout);
        exportListLayout = (LinearLayout)view.findViewById(R.id.export_list_layout);

        scanStudentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClassDetailsActivity)getActivity()).startBluetoothScanActivity();
            }
        });

        studentListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClassDetailsActivity)getActivity()).showStudentListFragment();
            }
        });

        exportListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClassDetailsActivity)getActivity()).exportExcelSheet();
            }
        });

        addStudentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClassDetailsActivity)getActivity()).showGraphFragment();
            }
        });

        return view;
    }

}
