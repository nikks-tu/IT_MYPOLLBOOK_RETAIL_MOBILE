package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQAddBroadcast extends IQ {

    private String broadcastName;
    private String description;

    public IQAddBroadcast(String broadcastName, String description) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.broadcastName = broadcastName;
        this.description = description;
        this.setType(Type.set);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.NAME, broadcastName);
        contact.attribute(LibConstants.DESCRIPTION, description);
        contact.attribute(LibConstants.STATUS, LibConstants.STATUS_ADD_BROADCAST);
        contact.closeEmptyElement();
        return xml;
    }
}
