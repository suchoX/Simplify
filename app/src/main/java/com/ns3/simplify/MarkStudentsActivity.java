package com.ns3.simplify;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


import com.ns3.simplify.adapters.MarkStudentListAdapter;
import com.ns3.simplify.realm.DateRegister;
import com.ns3.simplify.realm.Register;
import com.ns3.simplify.realm.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MarkStudentsActivity extends AppCompatActivity
{
    private static final String TAG = "MArkStudentsActivity";
    Toolbar mToolbar;

    String batchID;
    ArrayList<String> macID;
    MarkStudentListAdapter markStudentListAdapter;

    Realm realm;
    RealmConfiguration realmConfig;
    RealmResults<Student> studentList;
    RealmList<Student> presentStudentList;
    Register register;
    DateRegister record;
    int value;

    ListView markStudentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_students);

        initToolbar();
        value = getIntent().getIntExtra("Value",1);

        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        markStudentListView = (ListView)findViewById(R.id.student_listview);

        macID = new ArrayList<String>();

        batchID = getIntent().getStringExtra("Batch ID");
        macID = getIntent().getStringArrayListExtra("MAC ID's");
        Log.d("Mark",""+macID.size()+" "+batchID);
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.mark_students_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.marked_menu:
                presentStudentList = markStudentListAdapter.getPresentStudents();
                markInDatabase();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void markInDatabase()
    {
        Calendar c = Calendar.getInstance();

        record = new DateRegister();
        realm.beginTransaction();
        record.setDateID(realm.where(DateRegister.class).findAll().size() + 1);
        record.setDateToday(new Date());
        record.setValue(value);
        record.setStudentPresent(presentStudentList);
        realm.copyToRealmOrUpdate(record);
        realm.commitTransaction();

        register = realm.where(Register.class).equalTo("BatchID",batchID).findFirst();
        realm.beginTransaction();
        register.getRecord().add(record);
        realm.commitTransaction();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
