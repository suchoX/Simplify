package com.ns3.simplify;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ns3.simplify.realm.ContestHost;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ContestHistoryList extends AppCompatActivity {

    ListView contestHistoryList;
    Realm realm;
    RealmConfiguration realmConfig;
    RealmResults<ContestHost> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_history_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_red));
        }
        contestHistoryList = (ListView)findViewById(R.id.contestHistoryListView);

        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        results = realm.where(ContestHost.class).findAll();

        ArrayAdapter<ContestHost> adapter = new ArrayAdapter<ContestHost>(this, android.R.layout.simple_list_item_2, android.R.id.text1, results) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(results.get(position).getContestName() + " " + results.get(position).getDate());
                text2.setText("Participants : " + results.get(position).getParticipantList().size());
                return view;
            }
        };
        contestHistoryList.setAdapter(adapter);
        contestHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ContestHistoryList.this, ParticipantHistoryList.class);
                intent.putExtra("position",i);
                startActivity(intent);
            }
        });
    }
}
