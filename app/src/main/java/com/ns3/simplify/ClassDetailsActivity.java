package com.ns3.simplify;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.ns3.simplify.fragments.ClassDetailsMainFragment;
import com.ns3.simplify.fragments.GraphFragment;
import com.ns3.simplify.fragments.StudentAttendanceFragment;
import com.ns3.simplify.fragments.StudentListFragment;
import com.ns3.simplify.realm.Register;
import com.ns3.simplify.realm.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ClassDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Toolbar mToolbar;
    TextView batchNameToolbar,batchSubjectToolbar;

    String batchID,batchName,batchSub;

    Realm realm;
    RealmConfiguration realmConfig;
    Register batch;

    ClassDetailsMainFragment classDetailsMainFragment;
    StudentListFragment studentListFragment;
    StudentAttendanceFragment studentAttendanceFragment;
    GraphFragment graphFragment;

    AlertDialog beforeScanDialog;
    AlertDialog.Builder tempBuilder;
    LayoutInflater factory;
    View beforeScanView;


    ArrayList<String> macID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        batchID = getIntent().getStringExtra("BatchID");
        batch = realm.where(Register.class).equalTo("BatchID",batchID).findFirst();
        batchName = batch.getBatch();
        batchSub = batch.getSubject();

        initToolbar();

        classDetailsMainFragment = new ClassDetailsMainFragment();
        studentListFragment = new StudentListFragment();
        studentAttendanceFragment = new StudentAttendanceFragment();
        graphFragment = new GraphFragment();

        showClassDetailsMainFragment();
    }

    private void initToolbar()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        batchNameToolbar = (TextView)findViewById(R.id.batch_name_toolbar);
        batchSubjectToolbar = (TextView)findViewById(R.id.batch_subject_toolbar);
        batchNameToolbar.setText(batchName);
        batchSubjectToolbar.setText(batchSub);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void showStudentListFragment()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.class_details_frame, studentListFragment).addToBackStack("StudentList");
        transaction.commit();
        studentListFragment.getActivityContext(this);
        studentListFragment.getBatchID(batchID);
    }

    public void showClassDetailsMainFragment()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.class_details_frame, classDetailsMainFragment).addToBackStack("ClassDetailsMain");
        transaction.commit();
    }

    public void showStudentAttendanceFragment(String rollNum)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.class_details_frame, studentAttendanceFragment).addToBackStack("StudentAttendance");
        transaction.commit();
        studentAttendanceFragment.getActivityContext(this);
        studentAttendanceFragment.getBatchID(batchID);
        studentAttendanceFragment.getStudentRoll(rollNum);
    }

    public void showGraphFragment()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.class_details_frame, graphFragment).addToBackStack("Graph");
        transaction.commit();
        graphFragment.getActivityContext(this);
        graphFragment.getBatchID(batchID);
    }

    public void startBluetoothScanActivity()
    {

        final EditText numberScansView,valueView;
        Button scanNow,directMark;
        tempBuilder = new AlertDialog.Builder(this);
        beforeScanDialog = tempBuilder.create();
        factory = getLayoutInflater();
        beforeScanView = factory.inflate(R.layout.dialog_before_scan,null);
        beforeScanDialog.setView(beforeScanView);

        numberScansView = (EditText)beforeScanView.findViewById(R.id.number_scans_edit);
        valueView = (EditText)beforeScanView.findViewById(R.id.value_edit);
        scanNow = (Button)beforeScanView.findViewById(R.id.scan_now);
        scanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberScans,value;
                if(numberScansView.getText().toString().trim().equalsIgnoreCase(""))
                    numberScansView.setError("This cannot be empty");
                else if(valueView.getText().toString().trim().equalsIgnoreCase(""))
                    valueView.setError("This cannot be empty");
                else {
                    numberScans = Integer.parseInt(numberScansView.getText().toString().trim());
                    value = Integer.parseInt(valueView.getText().toString().trim());
                    Intent intent = new Intent(ClassDetailsActivity.this, BluetoothScanActivity.class);
                    intent.putExtra("Batch ID", batchID);
                    intent.putExtra("Value",value);
                    intent.putExtra("Number Scans",numberScans);
                    startActivity(intent);
                    finish();
                }
            }
        });

        directMark = (Button)beforeScanView.findViewById(R.id.direct_mark);
        directMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                macID = new ArrayList<String>();
                int value;
                if(valueView.getText().toString().trim().equalsIgnoreCase(""))
                    valueView.setError("This cannot be empty");
                else {
                    value = Integer.parseInt(valueView.getText().toString().trim());
                    Intent in = new Intent(ClassDetailsActivity.this, MarkStudentsActivity.class);
                    in.putExtra("Batch ID", batchID);
                    in.putExtra("Value",value);
                    in.putStringArrayListExtra("MAC ID's", macID);
                    startActivity(in);
                    finish();
                }


            }
        });

        beforeScanDialog.setCancelable(true);
        beforeScanDialog.show();

    }

    public void addStudent()
    {
        tempBuilder = new AlertDialog.Builder(this);
        beforeScanDialog = tempBuilder.create();
        factory = getLayoutInflater();
        beforeScanView = factory.inflate(R.layout.dialog_add_student,null);
        beforeScanDialog.setView(beforeScanView);
        beforeScanDialog.show();

        final EditText rollView,nameView,phoneView,mac1View,mac2View;
        rollView = (EditText)beforeScanView.findViewById(R.id.add_roll);
        nameView = (EditText)beforeScanView.findViewById(R.id.add_name);
        phoneView = (EditText)beforeScanView.findViewById(R.id.add_phone);
        mac1View = (EditText)beforeScanView.findViewById(R.id.add_mac1);
        mac2View = (EditText)beforeScanView.findViewById(R.id.add_mac2);
        final Button addStudent = (Button)beforeScanView.findViewById(R.id.add_student);
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roll,name,phone,mac1,mac2;
                roll = rollView.getText().toString().trim();
                name = nameView.getText().toString().trim();
                phone = phoneView.getText().toString().trim();
                mac1 = mac1View.getText().toString().trim();
                mac2 = mac2View.getText().toString().trim();
                if(roll.equals(""))
                    rollView.setError("Cant be Empty");
                else if(name.equals(""))
                    nameView.setError("Cant be Empty");
                else if(phone.equals(""))
                    phoneView.setError("Cant be Empty");
                else if(mac1.equals(""))
                    mac1View.setError("Cant be Empty");
                else
                {
                    addStudenttoDatabase(roll,name,phone,mac1,mac2);
                    beforeScanDialog.dismiss();
                }
            }
        });
    }

    private void addStudenttoDatabase(String roll,String name,String phone,String mac1,String mac2)
    {
        realm.beginTransaction();
        Student student=new Student();
        student.setRoll_number(roll);
        student.setStudent_name(name);
        student.setPhone_no(phone);
        student.setMac_ID1(mac1.toUpperCase());
        if(mac2.length()>0)
            student.setMac_ID2(mac2.toUpperCase());
        realm.copyToRealmOrUpdate(student);
        realm.commitTransaction();

        Register register;
        register = realm.where(Register.class).equalTo("BatchID",batchID).findFirst();
        realm.beginTransaction();
        register.getStudents().add(student);
        realm.commitTransaction();
        Toast.makeText(this,name+" added to this Register",Toast.LENGTH_LONG).show();
    }

    public void exportExcelSheet()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Choose Date Range?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Calendar now = Calendar.getInstance();
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                ClassDetailsActivity.this,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        );
                        dpd.show(getFragmentManager(), "Datepickerdialog");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        String formattedDate = df.format(c.getTime());
                        Excel_sheet_access.saveExcelFile(ClassDetailsActivity.this,"Full Attendance data till "+formattedDate+".xls",batchID);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd)
    {
        Toast.makeText(this,""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year+" "+dayOfMonthEnd+"/"+(monthOfYearEnd+1)+"/"+yearEnd,Toast.LENGTH_LONG).show();
    }

    private String getCurrentFragmentName()
    {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();

        String fragmentName;

        if (backStackEntryCount > 0)
            fragmentName = getFragmentManager().getBackStackEntryAt(backStackEntryCount - 1).getName();
        else
            fragmentName = "";
        return fragmentName;
    }

    @Override
    public void onBackPressed()
    {
        if(getCurrentFragmentName().equals("StudentList"))
            showClassDetailsMainFragment();
        else if(getCurrentFragmentName().equals("Graph"))
            showClassDetailsMainFragment();
        else if(getCurrentFragmentName().equals("ClassDetailsMain"))
            finish();
        else if(getCurrentFragmentName().equals("StudentAttendance"))
            showStudentListFragment();

    }


}
