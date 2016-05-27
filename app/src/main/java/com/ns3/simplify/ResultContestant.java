package com.ns3.simplify;

import android.app.FragmentTransaction;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.main_red));
        }
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
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
