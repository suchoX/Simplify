package com.ns3.simplify.others;

import java.io.Serializable;

/**
 * Created by ASUS on 12-May-16.
 */
public class Question implements Serializable {
    public String quesStatement;
    public String option1;
    public String option2;
    public String option3;
    public String option4;
    public int correctOption;

    public Question (String q, String o1, String o2, String o3, String o4,int co){
        quesStatement = q;
        option1 = o1;
        option2 = o2;
        option3 = o3;
        option4 = o4;
        correctOption = co;
    }
}
