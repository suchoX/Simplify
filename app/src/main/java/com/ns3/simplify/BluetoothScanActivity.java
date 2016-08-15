package com.ns3.simplify;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Date;

public class BluetoothScanActivity extends AppCompatActivity {


    public static final String TAG = BluetoothScanActivity.class.getSimpleName();

    BluetoothAdapter mBluetoothAdapter;
    IntentFilter filter;
    BroadcastReceiver mReceiver;
    String name[];
    ArrayList<String> macID;
    Bundle scan_data;
    int countScans=0;
    int numCountScans;
    int value;
    RippleBackground rippleBackground;

    String batchID;
    boolean selectDateCheck = false;
    Date selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_blue));
        }
        batchID = getIntent().getStringExtra("Batch ID");
        numCountScans = getIntent().getIntExtra("Number Scans",3);
        value = getIntent().getIntExtra("Value",1);
        selectDateCheck = getIntent().getBooleanExtra("Manual Date",false);
        selectedDate = (Date)getIntent().getSerializableExtra("Selected Date");
        //Toast.makeText(this, numCountScans + " " + value, Toast.LENGTH_LONG).show();

        rippleBackground = (RippleBackground)findViewById(R.id.content);

        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        init();
        if(mBluetoothAdapter==null)
        {
            Toast.makeText(getApplicationContext(),"Your device doesn't support bluetooth!",Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            if (!mBluetoothAdapter.isEnabled()) {
                turnOnBT();
            }
        }

        scan_data=new Bundle();
        name=new String[100];
        macID = new ArrayList<String>();
        Log.d(TAG, "onCreate: ");
        searchDevices();
    }
    private void turnOnBT() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
    }
    public void init()
    {
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

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
                    if(!macID.contains(device.getAddress().toUpperCase()))
                        macID.add(device.getAddress().toUpperCase());
                    Toast.makeText(context,device.getName(),Toast.LENGTH_SHORT).show();
                }

                else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals((action)))
                {
                    if(mBluetoothAdapter.getState()== BluetoothAdapter.STATE_OFF) {
                        turnOnBT();
                    }
                }
                else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals((action)))
                {
                    if(countScans == numCountScans-1)
                    {
                        rippleBackground.stopRippleAnimation();
                        Intent in = new Intent(BluetoothScanActivity.this, MarkStudentsActivity.class);
                        in.putExtra("Batch ID", batchID);
                        in.putExtra("Value",value);
                        in.putStringArrayListExtra("MAC ID's", macID);
                        intent.putExtra("Manual Date",selectDateCheck);
                        intent.putExtra("Selected Date",selectedDate);
                        startActivity(in);
                        finish();
                    }
                    else
                    {
                        countScans++;
                        Log.d("Mark","" + countScans);
                        mBluetoothAdapter.startDiscovery();
                    }
                }
            }
        };
        filter=new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        if(mBluetoothAdapter!=null)
            mBluetoothAdapter.cancelDiscovery();
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
        rippleBackground.startRippleAnimation();
        if(mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");

    }
}