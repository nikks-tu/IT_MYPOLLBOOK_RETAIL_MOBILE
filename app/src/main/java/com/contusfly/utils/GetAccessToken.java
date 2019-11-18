/**
 * @category   ContusMessanger
 * @package    com.contusfly.utils
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class GetAccessToken.
 */
public class GetAccessToken {

    /**
     * Instantiates a new gets the access token.
     */
    public GetAccessToken() {
    }

    /** The j obj. */
    private JSONObject jObj = null;

    /** The json. */
    private String json = "";

    /** The params. */
    private List<NameValuePair> params = new ArrayList<NameValuePair>();

    /** The http client. */
    private DefaultHttpClient httpClient;

    /** The http post. */
    private HttpPost httpPost;

    /**
     * Gets the token.
     *
     * @param address
     *            the address
     * @param token
     *            the token
     * @param client_id
     *            the client_id
     * @param redirect_uri
     *            the redirect_uri
     * @param grant_type
     *            the grant_type
     * @return the token
     */
    public JSONObject gettoken(String address, String token, String client_id,
            String redirect_uri, String grant_type) {
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(address);
            params.add(new BasicNameValuePair("code", token));
            params.add(new BasicNameValuePair("client_id", client_id));
            params.add(new BasicNameValuePair("redirect_uri", redirect_uri));
            params.add(new BasicNameValuePair("grant_type", grant_type));
            httpPost.setHeader("Content-Type",
                    "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            json = EntityUtils.toString(httpClient.execute(httpPost)
                    .getEntity());
            jObj = new JSONObject(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jObj;
    }

}