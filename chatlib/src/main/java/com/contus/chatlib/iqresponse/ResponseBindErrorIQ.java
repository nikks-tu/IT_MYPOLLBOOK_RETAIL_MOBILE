package com.contus.chatlib.iqresponse;

import com.contus.chatlib.utils.LibConstants;

import org.jivesoftware.smack.packet.IQ;
import org.json.JSONObject;

/**
 * Created by user on 29/9/16.
 */
public class ResponseBindErrorIQ extends IQ {

    private JSONObject jsonObject;

    public ResponseBindErrorIQ(JSONObject jsonObject) {
        super(IQ.QUERY_ELEMENT, LibConstants.MODULE_LOGIN);
        this.jsonObject = jsonObject;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        xml.element(LibConstants.RESULT, jsonObject.toString());
        return xml;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
