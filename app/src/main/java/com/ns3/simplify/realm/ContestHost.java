package com.ns3.simplify.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by ASUS on 12-May-16.
 */
public class ContestHost extends RealmObject {
    @PrimaryKey
    int cid;
    @Required
    String date;
    @Required
    String contestName;
    RealmList<Participant> participantList;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public RealmList<Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(RealmList<Participant> participantList) {
        this.participantList = participantList;
    }
}
