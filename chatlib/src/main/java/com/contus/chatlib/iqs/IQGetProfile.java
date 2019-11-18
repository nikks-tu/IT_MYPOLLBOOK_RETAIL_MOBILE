package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;
import com.contus.chatlib.utils.LogMessage;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQGetProfile extends IQ {

    private String username;

    public IQGetProfile(String username) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_PROFILE);
        this.username = username;
        this.setType(Type.get);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder profile = xml.halfOpenElement(LibConstants.PROFILE);
        profile.attribute(LibConstants.USER, username);
        profile.attribute(LibConstants.STATUS, LibConstants.STATUS_GET_PROFILE);
        profile.closeEmptyElement();
        LogMessage.e("profile", "profile::" + xml.toString());
        return xml;
    }
}
