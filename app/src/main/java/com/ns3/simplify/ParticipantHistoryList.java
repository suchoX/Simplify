package com.ns3.simplify;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ns3.simplify.realm.ContestHost;
import com.ns3.simplify.realm.Marks;
import com.ns3.simplify.realm.Participant;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ParticipantHistoryList extends AppCompatActivity {

    Toolbar mToolbar;
    ListView participantHistoryList;
    Realm realm;
    RealmConfiguration realmConfig;
    RealmResults<ContestHost> results_temp;
    RealmList<Participant> results;
    ContestHost contestHost;
    Marks marks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_history_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_red));
        }
        participantHistoryList = (ListView)findViewById(R.id.participantHistoryListView);

        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        results_temp = realm.where(ContestHost.class).findAll();
        contestHost = results_temp.get(getIntent().getIntExtra("position",0));
        results = contestHost.getParticipantList();

        ArrayAdapter<Participant> adapter = new ArrayAdapter<Participant>(this, android.R.layout.simple_list_item_2, android.R.id.text1, results) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(results.get(position).getName() + " " + results.get(position).getEmail());
                marks = results.get(position).getMarksList().where().equalTo("cid",contestHost.getCid()).findFirst();
                if(marks != null)
                    text2.setText("Score : " + marks.getMarks());
                else
                    text2.setText("Score : Null");
                return view;
            }
        };
        participantHistoryList.setAdapter(adapter);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contest " + contestHost.getContestName() + "Participants");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
