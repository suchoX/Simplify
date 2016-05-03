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
    private Date Date_today;
    private RealmList<Student> studentPresent;

    public Date getDate_today() {return Date_today;}
    public void setDate_today(Date Date_today) {this.Date_today = Date_today;}

    public RealmList<Student> getStudentPresent() {return studentPresent;}
    public void setStudentPresent(RealmList<Student> studentPresent) {this.studentPresent = studentPresent;}

    public int getDateID() {return dateID;}
    public void setDateID(int dateID) {this.dateID = dateID;}

}
