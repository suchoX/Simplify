package com.ns3.simplify;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ns3.simplify.others.Score;

public class ContestantResultList extends AppCompatActivity {

    ListView resultList;
    public static ArrayAdapter<Score> adapter;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contestant_result_list);

        resultList = (ListView) findViewById(R.id.contestantResultListView);

        adapter = new ArrayAdapter<Score>(this, android.R.layout.simple_list_item_2, android.R.id.text1, NsdChatActivity.mConnection.scoreList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text2.setTextSize(20);

                text1.setText(String.valueOf(position+1)+ ". " + NsdChatActivity.mConnection.scoreList.get(position).getUsername());
                text2.setText(String.valueOf(NsdChatActivity.mConnection.scoreList.get(position).getMarks()));
                return view;
            }
        };
        resultList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        context = this;
    }
}
