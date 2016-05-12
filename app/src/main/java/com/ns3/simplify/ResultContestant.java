package com.ns3.simplify;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.ns3.simplify.fragments.ReviewListFragment;
import com.ns3.simplify.fragments.ScoreDisplayFragment;

public class ResultContestant extends AppCompatActivity {

    ScoreDisplayFragment scoreDisplayFragment;
    ReviewListFragment reviewListFragment;
    int quesOrder[], answerMarked[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_contestant);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        scoreDisplayFragment = new ScoreDisplayFragment();
        reviewListFragment = new ReviewListFragment();

        initToolbar();

        quesOrder = getIntent().getIntArrayExtra("quesorder");
        answerMarked = getIntent().getIntArrayExtra("answermarked");
        showScoreDisplayFragment();
    }

    public void showScoreDisplayFragment() {
        scoreDisplayFragment.setScoreText(getIntent().getStringExtra("result"));
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameResult, scoreDisplayFragment);
        fragmentTransaction.commit();
    }

    public void showReviewListFragment() {
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        reviewListFragment.setQuestions(DisplayQuestions.questions);
        reviewListFragment.setQuestionOrder(quesOrder);
        reviewListFragment.setAnswerMarked(answerMarked);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameResult, reviewListFragment);
        fragmentTransaction.commit();

    }

    private void initToolbar() {
        Toolbar mToolbar;
        mToolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Results");
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
