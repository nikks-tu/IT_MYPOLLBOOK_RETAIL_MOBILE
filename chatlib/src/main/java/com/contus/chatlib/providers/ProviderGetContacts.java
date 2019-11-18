package com.contus.chatlib.providers;

import com.contus.chatlib.iqresponse.ResponseIQGetContacts;
import com.contus.chatlib.utils.LibConstants;
import com.contus.chatlib.utils.LogMessage;
import com.contus.chatlib.utils.Utils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.IQProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by user on 29/9/16.
 */
public class ProviderGetContacts extends IQProvider<ResponseIQGetContacts> {
    @Override
    public ResponseIQGetContacts parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {

        JSONArray jsonArray = new JSONArray();
        String response = null;
        String type;
        try {
            type = Utils.getType(parser);
            aa:
            while (true) {
                int eventType = parser.next();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (LibConstants.MODULE_CONTACT.equalsIgnoreCase(parser.getNamespace())) {
                            if ("get_contacts".equals(type) || "get_groups".equals(type) || "get_group".equals(type)) {
                                JSONObject jsonObject = new JSONObject();
                                Utils.parseContacts(jsonObject, parser, parser.getName());
                                jsonArray.put(jsonObject);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getDepth() == initialDepth)
                            break aa;
                    case XmlPullParser.TEXT:
                        response = parser.getText();
                        break;

                    default:
                        break;
                }
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return new ResponseIQGetContacts(jsonArray, response);
    }
}
