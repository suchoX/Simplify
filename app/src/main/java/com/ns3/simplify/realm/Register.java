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

    private String Subject;
    private String SubjectCode;
    private int Batch;
    private int Semester;
    private String Stream;
    private String Section;
    private String Group;
    private RealmList<Student> Students;
    private RealmList<DateRegister> Record;

    public String getBatchID() {return BatchID;}
    public void setBatchID(String BatchID) {this.BatchID = BatchID;}

    public String getSubject() {return Subject;}
    public void setSubject(String Subject) {this.Subject = Subject;}

    public String getSubjectCode() { return SubjectCode; }
    public void setSubjectCode(String subjectCode) { SubjectCode = subjectCode; }

    public int getBatch() {return Batch;}

    public void setBatch(int batch) { this.Batch = batch; }

    public int getSemester() { return Semester; }
    public void setSemester(int semester) { Semester = semester; }

    public String getStream() { return Stream; }
    public void setStream(String stream) { Stream = stream; }

    public String getSection() { return Section; }
    public void setSection(String section) { Section = section; }

    public String getGroup() { return Group; }
    public void setGroup(String group) { Group = group;}

    public RealmList<Student> getStudents() {return Students;}
    public void setStudents(RealmList<Student> Students) {this.Students = Students;}

    public RealmList<DateRegister> getRecord() {return Record;}
    public void setRecord(RealmList<DateRegister> Record) {this.Record = Record;}
}
