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
    private String Batch;
    private RealmList<Student> Students;
    private RealmList<DateRegister> Record;

    public String getBatch() {return Batch;}
    public void setBatch(String Batch) {this.Batch = Batch;}

    public RealmList<Student> getStudents() {return Students;}
    public void setStudents(RealmList<Student> Students) {this.Students = Students;}

    public RealmList<DateRegister> getRecord() {return Record;}
    public void setRecord(RealmList<DateRegister> Record) {this.Record = Record;}
}
