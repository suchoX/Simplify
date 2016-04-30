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

import java.util.ArrayList;

public class BluetoothScanActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    IntentFilter filter;
    BroadcastReceiver mReceiver;
    String name[];
    ArrayList<String> macID;
    int count=0;
    Bundle scan_data;

    String batchID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);

        batchID = getIntent().getStringExtra("Batch ID");

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
        macID = new ArrayList<String>();
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
                    macID.add(device.getAddress().toString());
                    Toast.makeText(context,name[count]+" "+ macID.get(count),Toast.LENGTH_SHORT).show();
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
                    Intent in = new Intent(BluetoothScanActivity.this, MarkStudentsActivity.class);
                    in.putExtra("Batch ID",batchID);
                    in.putStringArrayListExtra("MAC ID's",macID);
                    startActivity(in);
                    finish();
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