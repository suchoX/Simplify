package com.ns3.simplify.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ns3.simplify.R;
import com.ns3.simplify.adapters.GraphListAdapter;
import com.ns3.simplify.realm.DateRegister;
import com.ns3.simplify.realm.Register;
import com.ns3.simplify.realm.Student;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;


/**
 * Created by ASUS on 10-May-16.
 */
public class GraphFragment extends Fragment
{
    View view;
    Context context;
    String batchID;

    ListView graphList;
    GraphListAdapter graphListAdapter;

    Realm realm;
    RealmConfiguration realmConfig;
    Register register;
    RealmResults<Student> studentList;
    RealmList<DateRegister> dateRegisters;

    ArrayList<ArrayList<Student>> graphStudentList;
    ArrayList<ArrayList<DateRegister>> graphDateRegisters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graph, container, false);

        realmConfig = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(realmConfig);

        register = realm.where(Register.class).equalTo("BatchID",batchID).findFirst();
        studentList = register.getStudents().sort("Roll_number");
        dateRegisters = register.getRecord();

        graphStudentList = new ArrayList<ArrayList<Student>>();

        createGraphList();

        graphList = (ListView)view.findViewById(R.id.graph_list);
        graphListAdapter = new GraphListAdapter(context,graphStudentList,dateRegisters);
        graphList.setAdapter(graphListAdapter);
        return view;
    }

    private void createGraphList()
    {
        int size,k=0;
        if(studentList.size()%10 == 0)
            size =  studentList.size()/10;
        else
            size =  studentList.size()/10 + 1;

        for(int i=0 ; i<size ; i++)
        {
            graphStudentList.add(new ArrayList<Student>());
            if(i==size-1)
                for(int j=0 ; j<studentList.size()%10 ; j++)
                {
                    graphStudentList.get(i).add(studentList.get(k++));
                }
            else
                for(int j=0 ; j<10 ; j++) {
                    graphStudentList.get(i).add(studentList.get(k++));
                }
        }
    }

    public void getActivityContext(android.content.Context context)
    {
        this.context = context;
    }
    public void getBatchID(String batchID)
    {
        this.batchID = batchID;
    }
}
