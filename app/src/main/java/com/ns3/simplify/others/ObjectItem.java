package com.ns3.simplify.others;

public class ObjectItem {

    public String batchID;
    public String Subject;
    public String SubjectCode;
    public int Batch;
    public int Semester;
    public String Stream;
    public String Section;
    public String Group;

    // constructor
    public ObjectItem(String batchID,String Subject,String SubjectCode, int Batch, int Semester,String Stream, String Section, String Group)
    {
        this.batchID = batchID;
        this.Subject = Subject;
        this.SubjectCode = SubjectCode;
        this.Batch = Batch;
        this.Semester = Semester;
        this.Stream = Stream;
        this.Section = Section;
        this.Group = Group;
    }

}
