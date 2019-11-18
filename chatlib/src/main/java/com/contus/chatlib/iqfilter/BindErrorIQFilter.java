package com.contus.chatlib.iqfilter;

import com.contus.chatlib.iqresponse.ResponseBindErrorIQ;
import com.contus.chatlib.utils.LogMessage;

import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by user on 29/9/16.
 */
public class BindErrorIQFilter implements StanzaFilter {
    @Override
    public boolean accept(Stanza stanza) {
        boolean CustomIQReceived = false;
        if (stanza instanceof ResponseBindErrorIQ) {
            LogMessage.v("BindErrorIQFilter", "called::Bind error::" + stanza.getFrom());
            CustomIQReceived = true;
        }
        return CustomIQReceived;
    }
}
