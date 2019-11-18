package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;
import com.contus.chatlib.utils.LogMessage;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQGetContacts extends IQ {



    public IQGetContacts() {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.setType(Type.get);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.STATUS, LibConstants.STATUS_GET_CONTACT);
        contact.closeEmptyElement();
        LogMessage.e("IQGetContacts", "IQGetContacts::" + xml.toString());
        return xml;
    }
}
