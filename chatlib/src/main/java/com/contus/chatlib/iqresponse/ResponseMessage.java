package com.contus.chatlib.iqresponse;

import org.jivesoftware.smack.packet.ExtensionElement;

/**
 * Created by user on 28/6/16.
 */
public class ResponseMessage implements ExtensionElement {

    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    @Override
    public String getNamespace() {
        return "ack";
    }

    @Override
    public String getElementName() {
        return "result";
    }

    @Override
    public CharSequence toXML() {
        return "<result xmlns='ack'>" + message + "</result>";
    }
}
