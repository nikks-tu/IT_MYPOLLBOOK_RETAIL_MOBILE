package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQDeleteMessage extends IQ {

    private String messageId;
    private String chatType;

    public IQDeleteMessage(String messageId, String chatType) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.messageId = messageId;
        this.chatType = chatType;
        this.setType(Type.set);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.MESSAGE_ID, messageId);
        contact.attribute(LibConstants.TYPE, Message.Type.fromString(chatType));
        contact.attribute(LibConstants.STATUS, LibConstants.STATUS_DELETE_MESSAGE);
        contact.closeEmptyElement();
        return xml;
    }
}
