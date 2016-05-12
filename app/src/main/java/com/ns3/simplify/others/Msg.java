package com.ns3.simplify.others;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ASUS on 12-May-16.
 */
public class Msg implements Serializable {

    private String key = null;
    private String message = null;

    private ArrayList<Question> questionArray = null;

    public Msg(String key, String msg) {
        this.key = key;
        message = msg;
    }

    public Msg(String key, ArrayList<Question> q) {
        this.key = key;
        questionArray = q;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Question> getQuestionArray() {
        return questionArray;
    }
}
