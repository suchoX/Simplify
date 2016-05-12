package com.ns3.simplify;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
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
    EditText batch_edit,subject_edit,year_edit;
    ImageView imageView;
    String batch,subject,batchid;
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

        initToolbar();

        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        height= metrics.heightPixels;
        card_padding=height/20;

        batch_edit = (EditText)findViewById(R.id.edit_batch);
        subject_edit = (EditText)findViewById(R.id.edit_subject);
        year_edit = (EditText)findViewById(R.id.edit_year);

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
                if (batch_edit.getText().toString().trim().equalsIgnoreCase(""))
                    batch_edit.setError("This field can not be blank");
                else if (subject_edit.getText().toString().trim().equalsIgnoreCase(""))
                    subject_edit.setError("This field can not be blank");
                else if (year_edit.getText().toString().trim().equalsIgnoreCase(""))
                    year_edit.setError("This field can not be blank");
                else {
                    year = Integer.parseInt(year_edit.getText().toString().trim());
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
                Excel_sheet_access.readExcelFile(this,uri,batchid,batch,subject,year);
                Toast.makeText(Add_class.this, "Batch Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Add_class.this,Attendance.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void generateBatchID()    //Concatenate Batch Name and Subject to create final batch name
    {
        String temp = year_edit.getText().toString().trim();
        batch=batch_edit.getText().toString().trim();
        subject=subject_edit.getText().toString().trim();

        batchid = batch;
        batchid = batchid.concat(temp);
        batchid = batchid.concat(subject);
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
