package com.ns3.simplify;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.ns3.simplify.fragments.ClassDetailsMainFragment;
import com.ns3.simplify.fragments.GraphFragment;
import com.ns3.simplify.fragments.StudentAttendanceFragment;
import com.ns3.simplify.fragments.StudentListFragment;
import com.ns3.simplify.others.Excel_sheet_access;
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

public class ClassDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int FILE_SELECT_CODE = 100;

    Toolbar mToolbar;
    TextView batchNameToolbar,batchSubjectToolbar;

    String batchID,subName,subCode,stream,section;
    int batchD;

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
        subName = batch.getSubject();
        subCode = batch.getSubjectCode();
        stream = batch.getStream();
        section = batch.getSection();
        batchD = batch.getBatch();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_blue));
        }
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
        batchNameToolbar.setText(subName+" - "+ subCode);
        batchSubjectToolbar.setText(stream+" "+section+" - "+batchD);
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


    boolean selectDateCheck = false;
    boolean dateSelected = false;
    TextView dateText;
    Date selectedDate;

    public void startBluetoothScanActivity()
    {

        final EditText numberScansView,valueView;
        final Button scanNow,directMark,selectDate;
        final CheckBox dateCheck;
        tempBuilder = new AlertDialog.Builder(this);
        beforeScanDialog = tempBuilder.create();
        factory = getLayoutInflater();
        beforeScanView = factory.inflate(R.layout.dialog_before_scan,null);
        beforeScanDialog.setView(beforeScanView);

        dateText = (TextView)beforeScanView.findViewById(R.id.date_text);
        selectDate = (Button)beforeScanView.findViewById(R.id.select_date);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ClassDetailsActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        dateCheck = (CheckBox)beforeScanView.findViewById(R.id.date_check);
        dateCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectDateCheck = isChecked;
                if(isChecked) {
                    selectDate.setVisibility(View.VISIBLE);
                    dateText.setVisibility(View.VISIBLE);
                }
                else {
                    selectDate.setVisibility(View.GONE);
                    dateText.setVisibility(View.GONE);
                }

            }
        });

        numberScansView = (EditText)beforeScanView.findViewById(R.id.number_scans_edit);
        valueView = (EditText)beforeScanView.findViewById(R.id.value_edit);
        scanNow = (Button)beforeScanView.findViewById(R.id.scan_now);
        scanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberScans,value;
                if(selectDateCheck && !dateSelected)
                    Toast.makeText(ClassDetailsActivity.this,"Please Choose a Date or Uncheck Select Date",Toast.LENGTH_LONG).show();
                else if(numberScansView.getText().toString().trim().equalsIgnoreCase(""))
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
                    intent.putExtra("Manual Date",selectDateCheck);
                    intent.putExtra("Selected Date",selectedDate);
                    startActivity(intent);
                    beforeScanDialog.dismiss();
                }
            }
        });

        directMark = (Button)beforeScanView.findViewById(R.id.direct_mark);
        directMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                macID = new ArrayList<String>();
                int value;
                if(selectDateCheck && !dateSelected)
                    Toast.makeText(ClassDetailsActivity.this,"Please Choose a Date or Uncheck Select Date",Toast.LENGTH_LONG).show();
                else if(valueView.getText().toString().trim().equalsIgnoreCase(""))
                    valueView.setError("This cannot be empty");
                else {
                    value = Integer.parseInt(valueView.getText().toString().trim());
                    Intent in = new Intent(ClassDetailsActivity.this, MarkStudentsActivity.class);
                    in.putExtra("Batch ID", batchID);
                    in.putExtra("Value",value);
                    in.putStringArrayListExtra("MAC ID's", macID);
                    in.putExtra("Manual Date",selectDateCheck);
                    in.putExtra("Selected Date",selectedDate);
                    startActivity(in);
                    beforeScanDialog.dismiss();
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
                else
                {
                    addStudenttoDatabase(roll,name,phone,mac1,mac2);
                    beforeScanDialog.dismiss();
                }
            }
        });
    }

    public void importData()
    {
        Intent intent = new Intent(ClassDetailsActivity.this, FilePickerActivity.class);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK)
        {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false))
            {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    ClipData clip = data.getClipData();
                    if (clip != null)
                    {
                        for (int i = 0; i < clip.getItemCount(); i++)
                        {
                            Uri uri = clip.getItemAt(i).getUri();
                            Toast.makeText(this,""+uri.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    // For Ice Cream Sandwich
                }
                else
                {
                    ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null)
                    {
                        for (String path: paths)
                        {
                            Uri uri = Uri.parse(path);
                            Toast.makeText(this,""+uri.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
            else
            {
                Uri uri = data.getData();
                Excel_sheet_access.readExcelFile(this,uri,batchID);
                Toast.makeText(ClassDetailsActivity.this, "Students Added!", Toast.LENGTH_SHORT).show();
            }
        }
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
                        // TODO: 28-May-16
                        //Register register = realm.where(Register.class).equalTo("BatchID",batchID).findFirst();
                        Excel_sheet_access.saveExcelFile(ClassDetailsActivity.this,"Full_attendance_data_till_"+formattedDate+"-"+subCode+".xls",batchID);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd)
    {
        Date fromDate = new Date(year,monthOfYear,dayOfMonth);
        Date toDate = new Date(yearEnd,monthOfYearEnd,dayOfMonthEnd);
        //Toast.makeText(this,""+fromDate.getDate()+"/"+(fromDate.getMonth()+1)+"/"+fromDate.getYear()+" "+toDate.getDate()+"/"+(toDate.getMonth()+1)+"/"+toDate.getYear(),Toast.LENGTH_LONG).show();

        if(!selectDateCheck)
            Excel_sheet_access.saveExcelFile(ClassDetailsActivity.this,"Attendance_data_from_"+dayOfMonth+"-"+(monthOfYear+1)+"-"+year+"_to_"+dayOfMonthEnd+"-"+(monthOfYearEnd+1)+"-"+yearEnd+"_"+subCode+".xls",batchID,fromDate,toDate);
        else
        {
            dateText.setVisibility(View.VISIBLE);
            dateText.setText(""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
            selectedDate = new Date(year-1900,monthOfYear,dayOfMonth);
            dateSelected = true;
        }
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.class_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.delete_menu:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Delete Class?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Register register = realm.where(Register.class).equalTo("BatchID",batchID).findFirst();
                                realm.beginTransaction();
                                register.deleteFromRealm();
                                realm.commitTransaction();
                                Intent intent = new Intent(ClassDetailsActivity.this, Homescreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("Open Bluetooth",true);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {

                            }
                        }).create();
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
