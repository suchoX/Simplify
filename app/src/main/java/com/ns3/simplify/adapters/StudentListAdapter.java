package com.ns3.simplify.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ns3.simplify.R;
import com.ns3.simplify.realm.Student;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by ASUS on 30-Apr-16.
 */
public class StudentListAdapter extends BaseAdapter
{
    Context context;
    RealmList<Student> studentList;

    TextView rollView,nameView,phoneView,mac1View,mac2View;

    public StudentListAdapter(Context context, RealmList<Student> studentList) {
       this.context = context;
        this.studentList = studentList;
    }


    @Override
    public int getCount() {
        if(studentList!=null)
            return studentList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.stuent_list_item, parent, false);
        }
        rollView = (TextView)convertView.findViewById(R.id.student_item_roll);
        nameView = (TextView)convertView.findViewById(R.id.student_item_name);
        phoneView = (TextView)convertView.findViewById(R.id.student_item_phone);
        mac1View = (TextView)convertView.findViewById(R.id.student_item_mac1);
        mac2View = (TextView)convertView.findViewById(R.id.student_item_mac2);

        rollView.setText(studentList.get(position).getRoll_number());
        nameView.setText(studentList.get(position).getStudent_name());
        phoneView.setText("M: "+studentList.get(position).getPhone_no());
        mac1View.setText("MAC :"+studentList.get(position).getMac_ID1());

        return convertView;

    }
}
