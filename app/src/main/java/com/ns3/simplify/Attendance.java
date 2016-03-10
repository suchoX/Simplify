package com.ns3.simplify;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.ns3.simplify.realm.Register;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class Attendance extends AppCompatActivity
{
    ListView listView;
    FloatingActionButton fab;
    Intent intent;

    Realm realm;
    RealmResults<Register> allBatch;
    int numBatch;
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
            if(Build.VERSION.SDK_INT >=23)
                window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_blue_dark));
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.main_blue_dark));
            }
        }

        listView = (ListView) findViewById(android.R.id.list);
        listView.addHeaderView(new View(this), null, false);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(listView);

        realm = Realm.getInstance(this);
        allBatch = realm.where(Register.class).findAll();
        numBatch = allBatch.size();
        ObjectItem[] ObjectItemData = new ObjectItem[numBatch];
        int i;
        for(i=0 ; i<numBatch ; i++)
            ObjectItemData[i] = new ObjectItem(allBatch.get(i).getBatch(),allBatch.get(i).getSubject());

        ListViewAdapter listAdapter = new ListViewAdapter(this,R.layout.list_item,ObjectItemData);
        listView.setAdapter(listAdapter);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(Attendance.this, Add_class.class);
                Attendance.this.startActivity(intent);
                finish();
            }
        });
    }

    public void selected_class(String BatchID)
    {
        intent = new Intent(Attendance.this, Bluetooth_scan.class);
        intent.putExtra("BatchID", BatchID);
        Attendance.this.startActivity(intent);
    }
}
