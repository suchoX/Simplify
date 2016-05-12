package com.ns3.simplify.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by ASUS on 12-May-16.
 */
public class Participant extends RealmObject {
    @PrimaryKey
    String email;
    @Required
    String name;
    @Required
    String phNo;
    RealmList<Marks> marksList;

    public RealmList<Marks> getMarksList() {
        return marksList;
    }

    public void setMarksList(RealmList<Marks> marksList) {
        this.marksList = marksList;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}