package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;
import com.contus.chatlib.utils.LogMessage;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQGetSearchContacts extends IQ {

    private String term;

    public IQGetSearchContacts(String term) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.term = term;
        this.setType(Type.get);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.TERM, term);
        contact.attribute(LibConstants.STATUS, LibConstants.STATUS_SEARCH_CONTACT);
        contact.closeEmptyElement();
        LogMessage.e("IQGetSearchContacts", "IQGetSearchContacts::" + xml.toString());
        return xml;
    }
}
