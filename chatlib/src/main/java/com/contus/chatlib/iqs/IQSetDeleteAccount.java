package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;
import com.contus.chatlib.utils.LogMessage;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQSetDeleteAccount extends IQ {



    public IQSetDeleteAccount() {
        super(LibConstants.CONTACT, LibConstants.MODULE_CONTACT);
        this.setType(Type.set);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.STATUS, LibConstants.STATUS_SET_DELETE_ACCOUNT);
        contact.closeEmptyElement();
        LogMessage.e("IQSetDeleteAccount", "IQSetDeleteAccount::" + xml.toString());
        return xml;
    }
}
