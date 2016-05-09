package com.ns3.simplify.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sucho on 14/2/16.
 */
public class Register extends RealmObject
{
    @PrimaryKey
    private String BatchID;

    private String Batch;
    private String Subject;
    private int year;
    private RealmList<Student> Students;
    private RealmList<DateRegister> Record;

    public String getBatchID() {return BatchID;}
    public void setBatchID(String BatchID) {this.BatchID = BatchID;}

    public String getBatch() {return Batch;}
    public void setBatch(String Batch) {this.Batch = Batch;}

    public String getSubject() {return Subject;}
    public void setSubject(String Subject) {this.Subject = Subject;}

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public RealmList<Student> getStudents() {return Students;}
    public void setStudents(RealmList<Student> Students) {this.Students = Students;}

    public RealmList<DateRegister> getRecord() {return Record;}
    public void setRecord(RealmList<DateRegister> Record) {this.Record = Record;}
}
