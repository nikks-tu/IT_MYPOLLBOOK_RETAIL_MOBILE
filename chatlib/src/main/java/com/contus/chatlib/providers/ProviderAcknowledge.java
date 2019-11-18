package com.contus.chatlib.providers;

import com.contus.chatlib.iqresponse.ResponseMessage;
import com.contus.chatlib.utils.LogMessage;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by user on 29/6/16.
 */
public class ProviderAcknowledge extends ExtensionElementProvider<ResponseMessage> {


    @Override
    public ResponseMessage parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
        try {
            String type = parser.getAttributeValue(null, "type");
            if (Message.Type.fromString(type).equals(Message.Type.receipt)) {
                JSONObject jsonObject = new JSONObject();
                for (int i = 0, size = parser.getAttributeCount(); i < size; i++) {
                    jsonObject.put(parser.getAttributeName(i), parser.getAttributeValue(i));
                }
                return new ResponseMessage(jsonObject.toString());
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return null;
    }
}
