package com.contus.chatlib.iqresponse;

import android.text.TextUtils;

import com.contus.chatlib.utils.LibConstants;

import org.jivesoftware.smack.packet.IQ;
import org.json.JSONArray;

/**
 * Created by user on 29/9/16.
 */
public class ResponseIQGetContacts extends IQ {
    private JSONArray jsonObject;
    private String response;

    public ResponseIQGetContacts(JSONArray jsonObject, String response) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_CONTACT);
        this.jsonObject = jsonObject;
        this.response = response;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        xml.element(LibConstants.RESULT, jsonObject.toString());
        if (!TextUtils.isEmpty(response))
            xml.element(LibConstants.RESPONSE, response);
        else
            xml.element(LibConstants.RESPONSE, "");
        return xml;
    }
}
