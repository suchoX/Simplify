package com.ns3.simplify;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.ns3.simplify.adapters.ListViewAdapter;
import com.ns3.simplify.others.ObjectItem;
import com.ns3.simplify.realm.Register;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class Attendance extends AppCompatActivity
{
    ListView listView;
    FloatingActionButton fab;
    Intent intent;

    Realm realm;
    RealmResults<Register> allBatch;
    RealmConfiguration realmConfig;
    int numBatch;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_blue));
        }
        initToolbar();

        listView = (ListView) findViewById(android.R.id.list);
        listView.addHeaderView(new View(this), null, false);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(listView);

        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);
        allBatch = realm.where(Register.class).findAll();
        numBatch = allBatch.size();
        ObjectItem[] ObjectItemData = new ObjectItem[numBatch];
        int i;
        for(i=0 ; i<numBatch ; i++)
            ObjectItemData[i] = new ObjectItem(allBatch.get(i).getBatchID(),allBatch.get(i).getSubject(),allBatch.get(i).getSubjectCode(),allBatch.get(i).getBatch(),allBatch.get(i).getSemester(),allBatch.get(i).getStream(),allBatch.get(i).getSection(),allBatch.get(i).getGroup());

        ListViewAdapter listAdapter = new ListViewAdapter(this,R.layout.list_item,ObjectItemData);
        listView.setAdapter(listAdapter);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(Attendance.this, Add_class.class);
                Attendance.this.startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Attendance");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void selected_class(String BatchID)
    {
        intent = new Intent(Attendance.this, ClassDetailsActivity.class);
        intent.putExtra("BatchID", BatchID);
        Attendance.this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
