package com.ns3.simplify.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ns3.simplify.R;
import com.ns3.simplify.others.Question;

import java.util.ArrayList;

/**
 * Created by ASUS on 12-May-16.
 */
public class ReviewAdapter extends ArrayAdapter<Question> {

    Context mContext;
    protected ArrayList<Question> questions = new ArrayList<Question>();
    int quesOrder[], answerMarked[];
    TextView tvQuesSt;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    RadioGroup rg;
    LinearLayout cv;

    public ReviewAdapter(Context context, ArrayList<Question> questionArrayList, int qo[], int ma[]) {
        super(context, R.layout.activity_review_item, questionArrayList);
        mContext = context;
        questions = questionArrayList;
        quesOrder = qo;
        answerMarked = ma;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_review_item, parent, false);

        tvQuesSt = (TextView) convertView.findViewById(R.id.quesStatement);
        rg = (RadioGroup) convertView.findViewById(R.id.radioGroupOptions);
        radioButton1 = (RadioButton) convertView.findViewById(R.id.option1);
        radioButton2 = (RadioButton) convertView.findViewById(R.id.option2);
        radioButton3 = (RadioButton) convertView.findViewById(R.id.option3);
        radioButton4 = (RadioButton) convertView.findViewById(R.id.option4);
        cv = (LinearLayout) convertView.findViewById(R.id.card_info);

        tvQuesSt.setText(String.valueOf(position+1) + ". " + questions.get(quesOrder[position]).quesStatement);
        radioButton1.setText(questions.get(quesOrder[position]).option1);
        radioButton2.setText(questions.get(quesOrder[position]).option2);
        radioButton3.setText(questions.get(quesOrder[position]).option3);
        radioButton4.setText(questions.get(quesOrder[position]).option4);

        rg.clearCheck();

        radioButton1.setClickable(false);
        radioButton2.setClickable(false);
        radioButton3.setClickable(false);
        radioButton4.setClickable(false);

        if(answerMarked[position] == -1 || answerMarked[position] == 0) {
            //Do nothing
            cv.setBackgroundColor(Color.WHITE);
        }
        else if(answerMarked[position] == questions.get(quesOrder[position]).correctOption) {
            cv.setBackgroundColor(Color.argb(50,0,255,0));
            if(answerMarked[position] == 1) {
                System.out.println(position + " hi1");
                radioButton1.setChecked(true);
            }
            else if(answerMarked[position] == 2){
                System.out.println(position + " hi2");
                radioButton2.setChecked(true);
            }
            else if(answerMarked[position] == 3){
                System.out.println(position + " hi3");
                radioButton3.setChecked(true);
            }
            else {
                System.out.println(position + " hi4");
                radioButton4.setChecked(true);
            }
        }
        else {
            cv.setBackgroundColor(Color.argb(50,255,0,0));
            if(answerMarked[position] == 1) {
                radioButton1.setChecked(true);
            }
            else if(answerMarked[position] == 2){
                radioButton2.setChecked(true);
            }
            else if(answerMarked[position] == 3){
                radioButton3.setChecked(true);
            }
            else {
                radioButton4.setChecked(true);
            }
        }

        return convertView;
    }
}
