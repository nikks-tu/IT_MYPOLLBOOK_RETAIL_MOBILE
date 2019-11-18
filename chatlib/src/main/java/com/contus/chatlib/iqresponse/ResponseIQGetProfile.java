package com.contus.chatlib.iqresponse;

import android.text.TextUtils;

import com.contus.chatlib.utils.LibConstants;

import org.jivesoftware.smack.packet.IQ;
import org.json.JSONObject;

/**
 * Created by user on 29/9/16.
 */
public class ResponseIQGetProfile extends IQ {
    private JSONObject jsonObject;
    private String response;

    public ResponseIQGetProfile(JSONObject jsonObject, String response) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_PROFILE);
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
