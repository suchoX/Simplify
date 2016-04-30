package com.ns3.simplify;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;


import com.ns3.simplify.adapters.MarkStudentListAdapter;
import com.ns3.simplify.realm.Register;
import com.ns3.simplify.realm.Student;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MarkStudentsActivity extends AppCompatActivity
{
    Toolbar mToolbar;

    String batchID;
    ArrayList<String> macID;
    MarkStudentListAdapter markStudentListAdapter;

    Realm realm;
    RealmConfiguration realmConfig;
    RealmResults<Student> studentList;

    ListView markStudentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_students);

        initToolbar();

        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        markStudentListView = (ListView)findViewById(R.id.student_listview);

        macID = new ArrayList<String>();

        batchID = getIntent().getStringExtra("Batch ID");
        macID = getIntent().getStringArrayListExtra("MAC ID's");
        studentList = realm.where(Register.class).equalTo("BatchID",batchID).findFirst().getStudents().sort("Roll_number");
        markStudentListAdapter = new MarkStudentListAdapter(this,studentList,macID);
        markStudentListView.setAdapter(markStudentListAdapter);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mark Students");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}