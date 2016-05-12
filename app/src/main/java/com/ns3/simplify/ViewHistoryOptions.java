package com.ns3.simplify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ns3.simplify.realm.ContestHost;
import com.ns3.simplify.realm.ContestParticipant;
import com.ns3.simplify.realm.Marks;
import com.ns3.simplify.realm.Participant;
import com.scand.realmbrowser.RealmBrowser;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ViewHistoryOptions extends AppCompatActivity {

    Button participantBtn, hostBtn;

    Realm realm;
    RealmConfiguration realmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_options);

        participantBtn = (Button) findViewById(R.id.viewParticipantHistory);
        hostBtn = (Button) findViewById(R.id.viewContestHostHistory);


        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realmConfig = new RealmConfiguration.Builder(this).build();
        // Get a Realm instance for this thread
        realm = Realm.getInstance(realmConfig);

        participantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new RealmBrowser.Builder(ViewHistoryOptions.this)
                        .add(realm, ContestParticipant.class)
                        .show();
            }
        });

        hostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new RealmBrowser.Builder(ViewHistoryOptions.this)
                        .add(realm, ContestHost.class)
                        .add(realm, Participant.class)
                        .add(realm, Marks.class)
                        .show();
            }
        });
    }
}