package com.ns3.simplify;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;


public class Attendance extends AppCompatActivity
{
    ListView listView;
    FloatingActionButton fab;
    Intent intent;

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

        ObjectItem[] ObjectItemData = new ObjectItem[6];
        ObjectItemData[0] = new ObjectItem("CSE-2017:A", "CS502");
        ObjectItemData[1] = new ObjectItem("CSE-2018:B", "CS301");
        ObjectItemData[2] = new ObjectItem("CSE-2016:B", "CS703");
        ObjectItemData[3] = new ObjectItem("CSE-2016:B", "CS793");
        ObjectItemData[4] = new ObjectItem("CSE-2017:A", "CS592");
        ObjectItemData[5] = new ObjectItem("CSE-2018:A", "CS392");

        ListViewAdapter listAdapter = new ListViewAdapter(this,R.layout.list_item,ObjectItemData);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new OnItemClickListenerListViewItem(this));

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

    public void selected_class(String batch, String subject)
    {
        intent = new Intent(Attendance.this, Bluetooth_scan.class);
        Attendance.this.startActivity(intent);
    }
}
