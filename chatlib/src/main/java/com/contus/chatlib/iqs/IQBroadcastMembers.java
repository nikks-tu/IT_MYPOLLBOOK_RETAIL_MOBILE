package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;
import com.contus.chatlib.utils.LogMessage;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQBroadcastMembers extends IQ {

    private String broadcastId;
    private String members;
    private String status;

    public IQBroadcastMembers(String broadcastId, String members, String status) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.broadcastId = broadcastId;
        this.members = members;
        this.status = status;
        this.setType(Type.set);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.BROADCAST, broadcastId);
        contact.attribute(LibConstants.MEMBERS, members);
        contact.attribute(LibConstants.STATUS, status);
        contact.closeEmptyElement();
        LogMessage.e("IQSetRemoveGroupMembers", "IQSetRemoveGroupMembers::" + xml.toString());
        return xml;
    }
}
