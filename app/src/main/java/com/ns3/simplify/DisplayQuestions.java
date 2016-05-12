package com.ns3.simplify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ns3.simplify.others.Msg;
import com.ns3.simplify.others.Question;
import com.ns3.simplify.service.TimerBroadcastService;

import java.util.ArrayList;
import java.util.Random;

public class DisplayQuestions extends AppCompatActivity {

    private final String TAG = DisplayQuestions.class.getSimpleName();

    protected Msg message;
    protected int quesNo;
    public static ArrayList<Question> questions;
    TextView quesStatement, timerTextView;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    Button next_btn, sub_btn;
    Random rand = new Random();
    boolean set[];
    boolean doubleBackToExitPressedOnce = false;
    Intent timerIntent;
    int count, i, x, quesOrder[], answerMarked[];
    long mins, secs;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_question);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initToolbar();

        quesStatement = (TextView) findViewById(R.id.quesStatement);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupOptions);
        radioButton1 = (RadioButton) findViewById(R.id.option1);
        radioButton2 = (RadioButton) findViewById(R.id.option2);
        radioButton3 = (RadioButton) findViewById(R.id.option3);
        radioButton4 = (RadioButton) findViewById(R.id.option4);
        next_btn = (Button) findViewById(R.id.nextButton);
        sub_btn = (Button) findViewById(R.id.submitButton);
        sub_btn.setVisibility(View.GONE);

        message = (Msg) getIntent().getSerializableExtra("quesMsg");
        questions = message.getQuestionArray();
        quesNo = questions.size();
        set = new boolean[quesNo];
        quesOrder = new int[quesNo];
        answerMarked = new int[quesNo];

        while(count < quesNo) {
            i = rand.nextInt(quesNo);
            while (set[i])
                i = rand.nextInt(quesNo);
            set[i] = true;
            quesOrder[count++] = i;
        }

        quesStatement.setText(String.valueOf(x+1) + ". " + questions.get(quesOrder[x]).quesStatement);
        radioButton1.setText(questions.get(quesOrder[x]).option1);
        radioButton2.setText(questions.get(quesOrder[x]).option2);
        radioButton3.setText(questions.get(quesOrder[x]).option3);
        radioButton4.setText(questions.get(quesOrder[x]).option4);
        x++;
        if(x == quesNo) {
            next_btn.setVisibility(View.GONE);
            sub_btn.setVisibility(View.VISIBLE);
        }
        timerIntent = new Intent(this, TimerBroadcastService.class);
        startService(timerIntent);
        Log.i(TAG, "Started service");
    }

    public void nextClicked(View view) {

        checkMarkedAnswer();

        radioGroup.clearCheck();
        quesStatement.setText(String.valueOf(x+1) + ". " + questions.get(quesOrder[x]).quesStatement);
        radioButton1.setText(questions.get(quesOrder[x]).option1);
        radioButton2.setText(questions.get(quesOrder[x]).option2);
        radioButton3.setText(questions.get(quesOrder[x]).option3);
        radioButton4.setText(questions.get(quesOrder[x]).option4);
        x++;

        if(x == quesNo) {
            next_btn.setVisibility(View.GONE);
            sub_btn.setVisibility(View.VISIBLE);
        }
    }

    public void submitClicked(View view) {

        checkMarkedAnswer();

        int marks = 0;
        for (int y =0; y < x; y++) {
            if(questions.get(quesOrder[y]).correctOption == answerMarked[y]) {
                marks++;
            }
        }
        NsdChatActivity.mConnection.sendAllMessage("score", String.valueOf(marks));
        Intent intent = new Intent(this, ResultContestant.class);
        intent.putExtra("result",String.valueOf(marks) + " out of " + String.valueOf(quesNo));
        intent.putExtra("quesorder", quesOrder);
        intent.putExtra("answermarked", answerMarked);
        startActivity(intent);
    }

    private void checkMarkedAnswer() {
        int check = radioGroup.getCheckedRadioButtonId();

        if (check == -1) {
            answerMarked[x - 1] = -1;
        } else if (check == R.id.option1) {
            answerMarked[x - 1] = 1;
        } else if (check == R.id.option2) {
            answerMarked[x - 1] = 2;
        } else if (check == R.id.option3) {
            answerMarked[x - 1] = 3;
        } else if (check == R.id.option4) {
            answerMarked[x - 1] = 4;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                long millisUntilFinished = intent.getLongExtra("countdown", 0);
                if(millisUntilFinished == 0) {
                    next_btn.setVisibility(View.GONE);
                    sub_btn.setVisibility(View.VISIBLE);
                }
                secs = millisUntilFinished/1000;
                mins = secs / 60;
                secs -= mins * 60;
                time = mins + " : " + secs;
                Log.i(TAG, time);
                timerTextView.setText(time);
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
        stopService(timerIntent);
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void initToolbar() {
        Toolbar mToolbar;
        mToolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Questions");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
