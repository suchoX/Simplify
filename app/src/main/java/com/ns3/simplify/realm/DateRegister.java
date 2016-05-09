package com.ns3.simplify.realm;


import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sucho on 14/2/16.
 */
public class DateRegister extends RealmObject
{

    @PrimaryKey
    private int dateID;

    int date;
    int month;
    int year;
    private int value;
    private RealmList<Student> studentPresent;

    public int getDate() { return date; }
    public void setDate(int date) { this.date = date; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public RealmList<Student> getStudentPresent() {return studentPresent;}
    public void setStudentPresent(RealmList<Student> studentPresent) {this.studentPresent = studentPresent;}

    public int getDateID() {return dateID;}
    public void setDateID(int dateID) {this.dateID = dateID;}

}
