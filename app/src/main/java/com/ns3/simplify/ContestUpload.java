package com.ns3.simplify;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ns3.simplify.others.Question;
import com.ns3.simplify.others.ReadExcelFile;
import com.ns3.simplify.service.TimerBroadcastService;

import java.util.ArrayList;

public class ContestUpload extends AppCompatActivity {

    public final String TAG = ContestUpload.class.getSimpleName();
    private ArrayList<Question> questionArrayList = new ArrayList<Question>();
    EditText timerHours, timerMins, timerSecs;
    TextView timerTv;
    Button addQuesBtn, sendBtn;
    Intent timerIntent;
    String strHrs, strMins, strSecs;
    long tHrs, tMins, tSecs;
    public static long millSecs;
    long secs, mins, hours;
    boolean timerStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_upload);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initToolbar();

        sendBtn = (Button) findViewById(R.id.sendQuesTimerButton);
        addQuesBtn = (Button) findViewById(R.id.add_ques_btn);
        sendBtn.setVisibility(View.GONE);

        timerHours = (EditText) findViewById(R.id.timerHours);
        timerMins = (EditText) findViewById(R.id.timerMins);
        timerSecs = (EditText) findViewById(R.id.timerSecs);
        timerTv = (TextView) findViewById(R.id.textView5);

        timerSecs.setVisibility(View.GONE);
        timerTv.setVisibility(View.GONE);
    }

    public void addQues(View view) {
        Intent chooseExcelIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseExcelIntent.setType("application/vnd.ms-excel");
        startActivityForResult(chooseExcelIntent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "Result");

        if (resultCode == RESULT_OK && requestCode == 101) {
            if (data == null) {
                Toast.makeText(this, "No file found!", Toast.LENGTH_SHORT).show();
            } else {
                Uri uri = data.getData();
                Log.i(TAG, "Excel URI: " + uri);
                Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
                questionArrayList = ReadExcelFile.readExcelFile(uri);
                sendBtn.setVisibility(View.VISIBLE);
            }
        }
        else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, "There was an error reading the file!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendQues(View view) {
        strHrs = timerHours.getText().toString();
        strMins = timerMins.getText().toString();
        strSecs = timerSecs.getText().toString();

        if(strHrs.equals("") || strMins.equals(""))
            Toast.makeText(this, "Timer not set", Toast.LENGTH_SHORT).show();
        else {
            tHrs = Long.parseLong(strHrs);
            tMins = Long.parseLong(strMins);
            if(strSecs.equals("")){
                tSecs = 0L;
            }
            else
                tSecs = Long.parseLong(strSecs);

            if (tMins >= 60) {
                Toast.makeText(this, "Minutes must be within 0 to 59!", Toast.LENGTH_SHORT).show();
            }
            else if(tHrs == 0 && tMins == 0 && tSecs == 0) {
                Toast.makeText(this, "ContestParticipant duration should be greater than 0 mins", Toast.LENGTH_SHORT).show();
            }
            else {
                if(!timerStarted) {
                    timerIntent = new Intent(this, TimerBroadcastService.class);
                    startService(timerIntent);
                    timerStarted = true;
                    timerSecs.setVisibility(View.VISIBLE);
                    timerTv.setVisibility(View.VISIBLE);
                    timerSecs.setEnabled(false);
                    Log.i(TAG, "Started service");
                }
                millSecs = ((tHrs * 60 + tMins) * 60 + secs)*1000;
                NsdChatActivity.mConnection.sendAllMessage("timer", String.valueOf(millSecs));
                NsdChatActivity.mConnection.sendAllMessage("ques", questionArrayList);
                addQuesBtn.setEnabled(false);
            }
        }
    }

    public void showLeaderBoard(View view) {
        Intent intent = new Intent(this, ContestantResultList.class);
        startActivity(intent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                long millisUntilFinished = intent.getLongExtra("countdown", 0);
                secs = millisUntilFinished / 1000;
                mins = secs / 60;
                secs -= mins * 60;
                hours = mins / 60;
                mins -= hours * 60;
                timerHours.setText(String.valueOf(hours));
                timerMins.setText(String.valueOf(mins));
                timerSecs.setText(String.valueOf(secs));
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(TimerBroadcastService.COUNTDOWN_BR));
        Log.i(TAG, "Registered broadcast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        Log.i(TAG, "Unregistered broadcast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
            Log.d(TAG, e.getMessage());
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        try {
            if (timerIntent != null) {
                stopService(timerIntent);
                Log.i(TAG, "Stopped service");
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        super.onDestroy();
    }

    private void initToolbar() {
        Toolbar mToolbar;
        mToolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Set Timer");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

