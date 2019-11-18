package com.contus.chatlib.parcels;

import com.contus.chatlib.enums.RosterType;


/**
 * Created by user on 29/9/16.
 */
public interface IRoster {
    RosterType getType();

    void setType(RosterType var1);

    Jid getJid();

    void setJid(Jid var1);
}
