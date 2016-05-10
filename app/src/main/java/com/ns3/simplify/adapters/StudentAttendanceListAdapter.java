package com.ns3.simplify.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ns3.simplify.R;
import com.ns3.simplify.realm.DateRegister;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by ASUS on 04-May-16.
 */
public class StudentAttendanceListAdapter extends BaseAdapter
{
    Context context;

    RealmList<DateRegister> registerRecords;
    ArrayList<Integer> presentDatesID;
    int totalNumRecords;
    int totalDaysPresent;

    TextView dateView;
    CheckBox presentCheck;

    public StudentAttendanceListAdapter(Context context, RealmList<DateRegister> registerRecords,ArrayList<Integer> presentDatesID,int totalNumRecords, int totalDaysPresent) {
        this.context = context;
        this.registerRecords = registerRecords;
        this.presentDatesID = presentDatesID;
        this.totalNumRecords = totalNumRecords;
        this.totalDaysPresent = totalDaysPresent;
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
            dateView.setText(registerRecords.get(position).getDateToday().getDate()+"-"+(registerRecords.get(position).getDateToday().getMonth()+1)+"-"+registerRecords.get(position).getDateToday().getYear());
        else
            dateView.setText(registerRecords.get(position).getDateToday().getDate()+"-"+(registerRecords.get(position).getDateToday().getMonth()+1)+"-"+(registerRecords.get(position).getDateToday().getYear()+1900)+" ("+registerRecords.get(position).getValue()+")");

        if(presentDatesID.contains(registerRecords.get(position).getDateID()))
            presentCheck.setChecked(true);
        else
            presentCheck.setChecked(false);

        return convertView;

    }
}