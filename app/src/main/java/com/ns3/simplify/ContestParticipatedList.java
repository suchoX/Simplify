package com.ns3.simplify;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ns3.simplify.realm.ContestParticipant;
import com.ns3.simplify.realm.Participant;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

    public class ContestParticipatedList extends AppCompatActivity {

        ListView contestParticipatedListView;
        Realm realm;
        RealmConfiguration realmConfig;
        RealmResults<ContestParticipant> results;

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
            contestParticipatedListView = (ListView)findViewById(R.id.participantHistoryListView);

            realmConfig = new RealmConfiguration.Builder(this).build();
            realm = Realm.getInstance(realmConfig);

            results = realm.where(ContestParticipant.class).findAll();

            ArrayAdapter<ContestParticipant> adapter = new ArrayAdapter<ContestParticipant>(this, android.R.layout.simple_list_item_2, android.R.id.text1, results) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(results.get(position).getCid() + ". " + results.get(position).getContestName() + " " + results.get(position).getDate());
                    text2.setText("Score : " + results.get(position).getMarks());
                    return view;
                }
            };
            contestParticipatedListView.setAdapter(adapter);
        }

}
