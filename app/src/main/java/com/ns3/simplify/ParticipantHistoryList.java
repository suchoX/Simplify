package com.ns3.simplify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

    ListView participantHistoryList;
    Realm realm;
    RealmConfiguration realmConfig;
    RealmResults<ContestHost> results_temp;
    RealmResults<Marks> result_temp;
    RealmList<Participant> results;
    ContestHost contestHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_history_list);
        participantHistoryList = (ListView)findViewById(R.id.contestHistoryListView);

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
                text2.setText("Score : " + results.get(position).getMarksList());
                return view;
            }
        };
        participantHistoryList.setAdapter(adapter);
    }
}
