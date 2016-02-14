package com.ns3.simplify.realm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by sucho on 14/2/16.
 */
public class DateRegister extends RealmObject
{
    private DateRegister Date_today;
    private RealmList<Marking> Markings;

    public DateRegister getDate_today() {return Date_today;}
    public void setDate_today(DateRegister Date_today) {this.Date_today = Date_today;}

    public RealmList<Marking> getMarkings() {return Markings;}
    public void setMarkings(RealmList<Marking> Markings) {this.Markings = Markings;}
}
