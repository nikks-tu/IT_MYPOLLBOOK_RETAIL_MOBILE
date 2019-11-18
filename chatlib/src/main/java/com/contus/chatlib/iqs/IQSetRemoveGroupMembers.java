package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;
import com.contus.chatlib.utils.LogMessage;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQSetRemoveGroupMembers extends IQ {

    private String groupid;
    private String members;

    public IQSetRemoveGroupMembers(String groupid,String members) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.groupid = groupid;
        this.members = members;
        this.setType(Type.set);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.GROUP, groupid);
        contact.attribute(LibConstants.MEMBERS, members);
        contact.attribute(LibConstants.STATUS, LibConstants.STATUS_SET_REMOVE_GROUP_MEMBERS);
        contact.closeEmptyElement();
        LogMessage.e("IQSetRemoveGroupMembers", "IQSetRemoveGroupMembers::" + xml.toString());
        return xml;
    }
}
