package com.ns3.simplify.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ns3.simplify.R;
import com.ns3.simplify.adapters.ReviewAdapter;
import com.ns3.simplify.others.Question;

import java.util.ArrayList;

/**
 * Created by ASUS on 12-May-16.
 */
public class ReviewListFragment extends Fragment {

    ListView reviewList;
    ArrayList<Question> questions;
    int questionOrder[], answerMarked[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        reviewList = (ListView) view.findViewById(R.id.reviewListView);

        ReviewAdapter adapter = new ReviewAdapter(reviewList.getContext(), questions, questionOrder, answerMarked);
        reviewList.setAdapter(adapter);

        return view;
    }

    public void setQuestions(ArrayList<Question> q) {
        questions = q;
    }

    public void setAnswerMarked(int[] am) {
        answerMarked = am;
    }

    public void setQuestionOrder(int[] qo) {
        questionOrder = qo;
    }
}
