package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQClearChat extends IQ {

    private String username;
    private String chatType;

    public IQClearChat(String username, String chatType) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.username = username;
        this.chatType = chatType;
        this.setType(Type.set);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.USER, username);
        contact.attribute(LibConstants.TYPE, Message.Type.fromString(chatType));
        contact.attribute(LibConstants.STATUS, LibConstants.STATUS_CLEAR_MESSAGE);
        contact.closeEmptyElement();
        return xml;
    }
}
