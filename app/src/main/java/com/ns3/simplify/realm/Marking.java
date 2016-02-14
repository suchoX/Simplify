package com.ns3.simplify.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sucho on 14/2/16.
 */
public class Marking extends RealmObject
{
    @PrimaryKey
    private String Roll_number;
    private boolean Present;

    public String getRoll_number() {return Roll_number;}
    public void setRoll_number(String Roll_number) {this.Roll_number = Roll_number;}

    public boolean getPresent() {return Present;}
    public void setPresent(boolean Present) {this.Present = Present;}
}
