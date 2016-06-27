package com.ns3.simplify;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.ns3.simplify.others.Excel_sheet_access;
import com.ns3.simplify.realm.DateRegister;
import com.ns3.simplify.realm.Register;
import com.ns3.simplify.realm.Student;
import com.scand.realmbrowser.RealmBrowser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class Add_class extends AppCompatActivity
{
    private static String TAG = "Add_class";

    int card_padding;
    DisplayMetrics metrics;
    int height;
    EditText batch_edit,subject_edit,subjectcode_edit,semester_edit,stream_edit,section_edit,group_edit;
    ImageView imageView;
    String batch,subject,batchid,subjectcode,semester,stream,section,group;
    int semesterI,batchI;
    CheckBox groupCheck;
    boolean groupB = false;
    int year;

    Excel_sheet_access excel_sheet;

    Realm realm;
    RealmResults<Register> checkBatch;
    RealmConfiguration realmConfig;

    private static final int FILE_SELECT_CODE = 0;

    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_blue));
        }
        initToolbar();

        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        height= metrics.heightPixels;
        card_padding=height/20;

        batch_edit = (EditText)findViewById(R.id.edit_batch);
        subject_edit = (EditText)findViewById(R.id.edit_subject);
        semester_edit = (EditText)findViewById(R.id.edit_semester);
        subjectcode_edit = (EditText)findViewById(R.id.edit_subjectcode);
        stream_edit = (EditText)findViewById(R.id.edit_stream);
        section_edit = (EditText)findViewById(R.id.edit_section);
        group_edit = (EditText)findViewById(R.id.edit_group);

        groupCheck = (CheckBox)findViewById(R.id.check_groupcheck);
        groupCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupCheck.isChecked())
                {
                    groupB = true;
                    findViewById(R.id.add_class_layout_9).setVisibility(View.VISIBLE);
                }
                else
                {
                    groupB = false;
                    findViewById(R.id.add_class_layout_9).setVisibility(View.GONE);
                }
            }
        });

        Button bu = (Button)findViewById(R.id.realm_browser);
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Class<? extends RealmObject>> classes = new ArrayList<>();
                classes.add(Register.class);
                classes.add(Student.class);
                classes.add(DateRegister.class);
                new RealmBrowser.Builder(Add_class.this)
                        .add(realm, classes)
                        .show();
            }
        });


        imageView=(ImageView)findViewById(R.id.button_import_excel);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (subject_edit.getText().toString().trim().equalsIgnoreCase(""))
                    subject_edit.setError("This field can not be blank");
                else if (subjectcode_edit.getText().toString().trim().equalsIgnoreCase(""))
                    subjectcode_edit.setError("This field can not be blank");
                else if (batch_edit.getText().toString().trim().equalsIgnoreCase(""))
                    batch_edit.setError("This field can not be blank");
                else if (semester_edit.getText().toString().trim().equalsIgnoreCase(""))
                    semester_edit.setError("This field can not be blank");
                else if (stream_edit.getText().toString().trim().equalsIgnoreCase(""))
                    stream_edit.setError("This field can not be blank");
                else if (section_edit.getText().toString().trim().equalsIgnoreCase(""))
                    section_edit.setError("This field can not be blank");
                else if (groupB && group_edit.getText().toString().trim().equalsIgnoreCase(""))
                    group_edit.setError("This field can not be blank");
                else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(Add_class.this);
                    alert.setTitle("Excel Sheet Import");
                    alert.setMessage(" Points to be noted-\n\n" +
                            "- The Excel file MUST be a XLS file not a XLSX file.\n\n" +
                            "- The Excel sheet has 5 columns (Left to Right)-\n \t* Roll Number\n \t* Name\n \t* Phone Number\n \t* MacID1(Optional)\n \t* MacID2(Optional).\n\n" +
                            "- The Excel Sheet preferably should be created in Microsoft Excel, and not opened in the Android device.\n\n");
                    alert.setPositiveButton("Select Excel File", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            year = Integer.parseInt(semester_edit.getText().toString().trim());
                            Add_class.this.generateBatchID();
                            checkBatch = realm.where(Register.class).equalTo("BatchID",batchid).findAll();  //Checking if The same BatchID already exists
                            if(checkBatch.size() == 0) {
                                Intent intent = new Intent(Add_class.this, FilePickerActivity.class);
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                                intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                                intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                                //intent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                                startActivityForResult(intent, FILE_SELECT_CODE);
                            }
                            else
                                Toast.makeText(Add_class.this,"Same Batch Name and Subject already exists",Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();

                }
            }
        });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Class");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
                Excel_sheet_access.readExcelFile(this,uri,batchid,subject,subjectcode,batchI,semesterI,stream,section,group);
                Toast.makeText(Add_class.this, "Batch Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Add_class.this,Attendance.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void generateBatchID()    //Concatenate Batch Name and Subject to create final batch name
    {
        batch=batch_edit.getText().toString().trim();
        batchI = Integer.parseInt(batch);
        subject=subject_edit.getText().toString().trim();
        subjectcode = subjectcode_edit.getText().toString().trim();
        semester = semester_edit.getText().toString().trim();
        semesterI = Integer.parseInt(semester);
        stream = stream_edit.getText().toString().trim();
        section = section_edit.getText().toString().trim();
        if(groupB)
            group = group_edit.getText().toString().trim();
        else
            group = "N/A";

        batchid = subject;
        batchid = batchid.concat(subjectcode);
        batchid = batchid.concat(batch);
        batchid = batchid.concat(semester);
        batchid = batchid.concat(stream);
        batchid = batchid.concat(section);
        if (groupB)
            batchid.concat(group);
        batchid = batchid.replaceAll("\\s+","");    //remove spaces from batch name
        batchid = batchid.toLowerCase();
    }



    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
