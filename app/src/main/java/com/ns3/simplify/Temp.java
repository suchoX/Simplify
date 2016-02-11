package com.ns3.simplify;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Temp extends AppCompatActivity
{
    Bundle scan_data;
    ArrayAdapter<String> mArrayAdapter1;
    ListView lv1;
    String name[];
    String mac[];
    int number,i;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if(Build.VERSION.SDK_INT >=23) {
                window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_blue_dark));
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                window.setStatusBarColor(getResources().getColor(R.color.main_blue_dark));
            }

        }

        scan_data=new Bundle();
        name=new String[100];
        mac=new String[100];
        lv1=(ListView)findViewById(R.id.lv1);
        lv1.addHeaderView(new View(this), null, false);
        mArrayAdapter1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,0);
        lv1.setAdapter(mArrayAdapter1);

        scan_data=getIntent().getExtras();
        name=scan_data.getStringArray("Name");
        mac=scan_data.getStringArray("MAC");
        number=scan_data.getInt("Number");
        Toast.makeText(this,name[0]+" "+mac[0],Toast.LENGTH_SHORT).show();
        for(i=0 ; i<number ; i++)
            mArrayAdapter1.add(name[i]+"\n"+mac[i]);
    }

}
