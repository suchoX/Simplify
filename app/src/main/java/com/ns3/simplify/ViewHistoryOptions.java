package com.ns3.simplify;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ViewHistoryOptions extends AppCompatActivity {

    Toolbar mToolbar;

    Button participantBtn, hostBtn;

    Realm realm;
    RealmConfiguration realmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_options);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_red));
        }

        initToolbar();

        participantBtn = (Button) findViewById(R.id.viewParticipantHistory);
        hostBtn = (Button) findViewById(R.id.viewContestHostHistory);


        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realmConfig = new RealmConfiguration.Builder(this).build();
        // Get a Realm instance for this thread
        realm = Realm.getInstance(realmConfig);

        participantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                new RealmBrowser.Builder(ViewHistoryOptions.this)
                        .add(realm, ContestParticipant.class)
                        .show();
                */
                Intent intent = new Intent(ViewHistoryOptions.this, ContestParticipatedList.class);
                startActivity(intent);
            }
        });

        hostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                new RealmBrowser.Builder(ViewHistoryOptions.this)
                        .add(realm, ContestHost.class)
                        .add(realm, Participant.class)
                        .add(realm, Marks.class)
                        .show();
                */
                Intent intent = new Intent(ViewHistoryOptions.this, ContestHistoryList.class);
                startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View History");
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