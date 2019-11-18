package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQUpdateBroadcast extends IQ {

    private String broadcastId;
    private String broadcastName;
    private String description;

    public IQUpdateBroadcast(String broadcastId, String broadcastName, String description) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.broadcastId = broadcastId;
        this.broadcastName = broadcastName;
        this.description = description;
        this.setType(Type.set);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.BROADCAST, broadcastId);
        contact.attribute(LibConstants.NAME, broadcastName);
        contact.attribute(LibConstants.DESCRIPTION, description);
        contact.attribute(LibConstants.STATUS, LibConstants.STATUS_SET_BROADCAST);
        contact.closeEmptyElement();
        return xml;
    }
}
