package com.ns3.simplify;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.realmbrowser.RealmBrowser;
import com.dd.realmbrowser.RealmBrowserActivity;
import com.dd.realmbrowser.RealmFilesActivity;
import com.dd.realmbrowser.RealmModelsActivity;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.ns3.simplify.realm.Register;
import com.ns3.simplify.realm.Student;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class Add_class extends AppCompatActivity
{

    int card_padding;
    DisplayMetrics metrics;
    int height;
    EditText batch_edit,subject_edit;
    ImageView imageView;
    String batch,subject,batchid;

    Excel_sheet_access excel_sheet;

    Realm realm;
    RealmResults<Register> checkBatch;

    private static final int FILE_SELECT_CODE = 0;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        initToolbar();

        realm = Realm.getInstance(this);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        height= metrics.heightPixels;
        card_padding=height/20;

        batch_edit = (EditText)findViewById(R.id.edit_batch);
        subject_edit = (EditText)findViewById(R.id.edit_subject);
        Button b=(Button)findViewById(R.id.realm);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RealmBrowser.getInstance().addRealmModel(Student.class, Register.class);
                RealmFilesActivity.start(Add_class.this);

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
                else {
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
                Excel_sheet_access.readExcelFile(this,uri,batchid,batch,subject);
                Toast.makeText(Add_class.this, "Batch Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Add_class.this,Attendance.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void generateBatchID()    //Concatenate Batch Name and Subject to create final batch name
    {
        batch=batch_edit.getText().toString();
        subject=subject_edit.getText().toString();

        batchid = batch;
        batchid = batchid.concat(subject);
        batchid = batchid.replaceAll("\\s+","");    //remove spaces from batch name
        batchid = batchid.toLowerCase();
        Toast.makeText(this,batchid,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
