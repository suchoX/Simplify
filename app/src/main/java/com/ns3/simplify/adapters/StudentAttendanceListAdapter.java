package com.ns3.simplify.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.ns3.simplify.R;
import com.ns3.simplify.fragments.StudentAttendanceFragment;
import com.ns3.simplify.realm.DateRegister;
import com.ns3.simplify.realm.Register;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by ASUS on 04-May-16.
 */
public class StudentAttendanceListAdapter extends BaseAdapter
{
    Context context;
    StudentAttendanceFragment studentAttendanceFragment;

    RealmList<DateRegister> registerRecords;
    ArrayList<Integer> presentDatesID;
    int totalNumRecords;
    int totalDaysPresent;

    TextView dateView;
    CheckBox presentCheck;

    boolean checked=false;

    public StudentAttendanceListAdapter(Context context, RealmList<DateRegister> registerRecords,ArrayList<Integer> presentDatesID,int totalNumRecords, int totalDaysPresent,StudentAttendanceFragment studentAttendanceFragment) {
        this.context = context;
        this.registerRecords = registerRecords;
        this.presentDatesID = presentDatesID;
        this.totalNumRecords = totalNumRecords;
        this.totalDaysPresent = totalDaysPresent;
        this.studentAttendanceFragment = studentAttendanceFragment;
    }


    @Override
    public int getCount() {
        if(registerRecords!=null)
            return registerRecords.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return registerRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.record_list_item, parent, false);
        }

        dateView = (TextView)convertView.findViewById(R.id.record_date);
        presentCheck = (CheckBox)convertView.findViewById(R.id.student_present_check);

        if(registerRecords.get(position).getValue()==1)
            dateView.setText(registerRecords.get(position).getDateToday().getDate()+"-"+(registerRecords.get(position).getDateToday().getMonth()+1)+"-"+(registerRecords.get(position).getDateToday().getYear()+1900));
        else
            dateView.setText(registerRecords.get(position).getDateToday().getDate()+"-"+(registerRecords.get(position).getDateToday().getMonth()+1)+"-"+(registerRecords.get(position).getDateToday().getYear()+1900)+" ("+registerRecords.get(position).getValue()+")");

        if(presentDatesID.contains(registerRecords.get(position).getDateID()))
            presentCheck.setChecked(true);
        else
            presentCheck.setChecked(false);


        presentCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()) {
                    if(!checked) {
                        studentAttendanceFragment.markStudent(registerRecords.get(position).getDateID());
                        checked = true;
                    }
                    else {
                        checkOnceDialog();
                        presentCheck.setChecked(true);
                    }
                }
                else {
                    if(!checked) {
                        studentAttendanceFragment.unmarkStudent(registerRecords.get(position).getDateID());
                        checked = true;
                    }
                    else {
                        checkOnceDialog();
                        presentCheck.setChecked(false);
                    }
                }
            }
        });


        /*presentCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    studentAttendanceFragment.markStudent(registerRecords.get(position).getDateID());
                }
                else
                {
                    studentAttendanceFragment.unmarkStudent(registerRecords.get(position).getDateID());
                }
            }
        });*/

        return convertView;

    }

    private void checkOnceDialog()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Note");
        alert.setMessage(" You can Mark or Unmark a student here only once! To mark again, go back and select the student again!");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }
}