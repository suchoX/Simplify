package com.ns3.simplify.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sucho on 14/2/16.
 */
public class Student extends RealmObject
{
    @PrimaryKey
    private String Roll_number;

    private String Phone_no;
    private String Student_name;
    private String Mac_ID1;
    private String Mac_ID2;

    public String getRoll_number() {return Roll_number;}
    public String getPhone_no() {return Phone_no;}
    public String getStudent_name() {return Student_name;}
    public String getMac_ID1() {return Mac_ID1;}
    public String getMac_ID2() {return Mac_ID2;}

    public void setRoll_number(String Roll_number) {this.Roll_number = Roll_number;}
    public void setPhone_no(String Phone_no) {this.Phone_no = Phone_no;}
    public void setStudent_name(String Student_name) {this.Student_name = Student_name;}
    public void setMac_ID1(String Mac_ID1) {this.Mac_ID1 = Mac_ID1;}
    public void setMac_ID2(String Mac_ID2) {this.Mac_ID2 = Mac_ID2;}

}
