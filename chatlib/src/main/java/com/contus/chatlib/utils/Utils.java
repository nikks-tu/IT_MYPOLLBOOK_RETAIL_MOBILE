package com.contus.chatlib.utils;

import android.util.Log;

import org.json.JSONObject;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by user on 29/9/16.
 */
public class Utils {

    /**
     * Parse contact sync string.
     *
     * @param xmlInput the xml input
     * @return the string
     */
    public static String parseResult(String xmlInput) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlInput));
            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("result");
            return getCharacterDataFromElement((Element) nodes.item(0));
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return null;
    }

    /**
     * Parse contact sync string.
     *
     * @param xmlInput the xml input
     * @return the string
     */
    public static String parseResponse(String xmlInput) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlInput));
            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("response");
            return getCharacterDataFromElement((Element) nodes.item(0));
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return null;
    }

    /**
     * getCharacterDataFromElement
     * <p/>
     * Get the particular character data from the element
     *
     * @param e Xml element to parse
     * @return string of element data
     */
    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public static void parseProfile(JSONObject jsonObject, XmlPullParser parser, String tagName) {
        try {
            if (LibConstants.PROFILE.equalsIgnoreCase(tagName)) {
                for (int i = 0, size = parser.getAttributeCount(); i < size; i++) {
                    jsonObject.put(parser.getAttributeName(i), parser.getAttributeValue(i));
                }
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public static void parseContacts(JSONObject jsonObject, XmlPullParser parser, String tagName) {
        try {
            if (LibConstants.CONTACT.equalsIgnoreCase(tagName) || LibConstants.GROUP.equalsIgnoreCase(tagName)) {
                for (int i = 0, size = parser.getAttributeCount(); i < size; i++) {
                    jsonObject.put(parser.getAttributeName(i), parser.getAttributeValue(i));
                }
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    public static String getType(XmlPullParser myParser) throws XmlPullParserException, IOException {
        int event = myParser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String name = myParser.getName();
            if (event == XmlPullParser.START_TAG && name.equals("query")) {
                String type = myParser.getAttributeValue(null, "type");
                Log.e("get type::", "type::" + type);
                return type;
            }
        }
        return null;
    }
}
