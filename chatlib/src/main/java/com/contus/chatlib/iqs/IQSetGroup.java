package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;
import com.contus.chatlib.utils.LogMessage;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQSetGroup extends IQ {

    private String groupname;
    private String description;
    private String image;
    private String groupid;

    public IQSetGroup(String groupname, String description, String image, String groupid) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.groupname = groupname;
        this.description = description;
        this.image = image;
        this.groupid = groupid;
        this.setType(Type.set);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.NAME, groupname);
        contact.attribute(LibConstants.DESCRIPTION, description);
        contact.attribute(LibConstants.IMAGE, image);
        contact.attribute(LibConstants.GROUP, groupid);
        contact.attribute(LibConstants.STATUS, LibConstants.STATUS_SET_SET_GROUP);
        contact.closeEmptyElement();
        LogMessage.e("IQSetCreateGroup", "IQSetCreateGroup::" + xml.toString());
        return xml;
    }
}
