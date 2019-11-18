package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQDeleteBroadcast extends IQ {

    private String broadcastId;

    public IQDeleteBroadcast(String broadcastId) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.broadcastId = broadcastId;
        this.setType(Type.set);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.BROADCAST, broadcastId);
        contact.attribute(LibConstants.STATUS, LibConstants.STATUS_SET_DELETE_BROADCAST);
        contact.closeEmptyElement();
        return xml;
    }
}
