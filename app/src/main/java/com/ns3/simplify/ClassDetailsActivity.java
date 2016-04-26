package com.ns3.simplify;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ns3.simplify.fragments.ClassDetailsMainFragment;
import com.ns3.simplify.realm.Register;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ClassDetailsActivity extends AppCompatActivity {

    Toolbar mToolbar;
    TextView batchNameToolbar,batchSubjectToolbar;

    String batchID,batchName,batchSub;

    Realm realm;
    RealmConfiguration realmConfig;
    Register batch;

    ClassDetailsMainFragment classDetailsMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);

        batchID = getIntent().getStringExtra("BatchID");
        batch = realm.where(Register.class).equalTo("BatchID",batchID).findFirst();
        batchName = batch.getBatch();
        batchSub = batch.getSubject();

        initToolbar();

        classDetailsMainFragment = new ClassDetailsMainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.class_details_frame, classDetailsMainFragment);
        transaction.commit();
    }

    private void initToolbar()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        batchNameToolbar = (TextView)findViewById(R.id.batch_name_toolbar);
        batchSubjectToolbar = (TextView)findViewById(R.id.batch_subject_toolbar);
        batchNameToolbar.setText(batchName);
        batchSubjectToolbar.setText(batchSub);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}