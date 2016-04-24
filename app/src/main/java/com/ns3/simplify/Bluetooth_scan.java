package com.ns3.simplify;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

public class Bluetooth_scan extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    IntentFilter filter;
    BroadcastReceiver mReceiver;
    String name[],macid[];
    int count=0;
    Bundle scan_data;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if(Build.VERSION.SDK_INT >=23)
                window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_pink_dark));
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(R.color.main_pink_dark));
            }
        }

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();

        init();
        if(mBluetoothAdapter==null)
        {
            Toast.makeText(getApplicationContext(),"Your device doesn't support bluetooth!",Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            if (!mBluetoothAdapter.isEnabled())
            {
                turnOnBT();
            }
        }

        scan_data=new Bundle();
        name=new String[100];
        macid=new String[100];
        count=0;
        searchDevices();
    }
    private void turnOnBT() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
    }
    public void init()
    {
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);

        //Create a BroadCastReceiver for ACTION_FOUND
        mReceiver=new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                //When discovery finds a device
                if(BluetoothDevice.ACTION_FOUND.equals((action)))
                {
                    //Get the BluetoothDevice object from the Intent
                    BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //Add the name and address to an array adapter to show in a ListView
                    name[count]=device.getName().toString();
                    macid[count]=device.getAddress().toString();
                    Toast.makeText(context,name[count]+" "+macid[count],Toast.LENGTH_SHORT).show();
                    count++;
                }

                else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals((action)))
                {
                    if(mBluetoothAdapter.getState()==mBluetoothAdapter.STATE_OFF)
                    {
                        turnOnBT();
                    }
                }
                else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals((action)))
                {
                    /*scan_data.putStringArray("MAC",macid);
                    scan_data.putStringArray("Name",name);
                    scan_data.putInt("Number", count);
                    intent = new Intent(Bluetooth_scan.this, Temp.class);
                    intent.putExtras(scan_data);
                    Bluetooth_scan.this.startActivity(intent);*/

                }
            }
        };
        registerReceiver(mReceiver, filter);
        filter=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filter);
        filter=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        filter=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED)
        {
            Toast.makeText(getApplicationContext(),"Bluetooth must be enabled!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void searchDevices()
    {
        count=0;
        mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.startDiscovery();
    }
}