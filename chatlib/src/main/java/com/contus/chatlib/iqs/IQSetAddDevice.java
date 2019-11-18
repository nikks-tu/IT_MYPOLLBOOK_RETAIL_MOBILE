package com.contus.chatlib.iqs;

import com.contus.chatlib.utils.LibConstants;
import com.contus.chatlib.utils.LogMessage;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by user on 28/9/16.
 */
public class IQSetAddDevice extends IQ {

    private String device_token;


    public IQSetAddDevice(String device_token) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.device_token = device_token;

        this.setType(Type.set);
    }


    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        XmlStringBuilder contact = xml.halfOpenElement(LibConstants.CONTACT);
        contact.attribute(LibConstants.DEVICE_TOKEN, device_token);
        contact.attribute(LibConstants.DEVICE, "android");

        contact.attribute(LibConstants.STATUS, LibConstants.STATUS_SET_ADD_DEVICE);
        contact.closeEmptyElement();
        LogMessage.e("IQSetAddDevice", "IQSetAddDevice::" + xml.toString());
        return xml;
    }
}
