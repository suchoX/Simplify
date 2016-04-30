package com.ns3.simplify.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ns3.simplify.R;
import com.ns3.simplify.adapters.StudentListAdapter;
import com.ns3.simplify.realm.Register;
import com.ns3.simplify.realm.Student;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by ASUS on 30-Apr-16.
 */
public class StudentListFragment extends Fragment
{
    View view;
    android.content.Context context;

    ListView studentListView;
    StudentListAdapter studentListAdapter;
    RealmList<Student> studentList;

    String batchID;

    Realm realm;
    RealmConfiguration realmConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_student_list, container, false);

        realmConfig = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(realmConfig);

        studentListView = (ListView)view.findViewById(R.id.student_listview);
        studentList = realm.where(Register.class).equalTo("BatchID",batchID).findFirst().getStudents();
        studentListAdapter = new StudentListAdapter(context,studentList);
        studentListView.setAdapter(studentListAdapter);

        return view;
    }

    public void getBatchID(String batchID)
    {
        this.batchID = batchID;
    }

    public void getActivityContext(android.content.Context context)
    {
        this.context = context;
    }
}
