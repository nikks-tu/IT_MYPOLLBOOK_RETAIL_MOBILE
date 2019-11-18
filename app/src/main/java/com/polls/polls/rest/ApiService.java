/**
 * @category ContusMessanger
 * @package com.time.utils
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.polls.polls.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.contusfly.MApplication;
import com.contusfly.utils.LogMessage;
import com.contusfly.views.DoProgressDialog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * The Activity class which acts as the base class for all other Activity classes for this
 * application, All other activities must extend this class so that they can receive the callbacks
 * of the chat service through the overriding methods of other this class all the callbacks
 * will be received from the broadcast receiver of this class.
 *
 * @author ContusTeam <developers@contus.in>
 * @version 1.1
 */
public class ApiService extends AsyncTask<String, Void, String> {

    public METHOD requestMethod = METHOD.POST;
    /**
     * The progress dialog.
     */
    private DoProgressDialog progressDialog;
    /**
     * The context.
     */
    private Context context;
    /**
     * The listener.
     */
    private OnTaskCompleted listener;
    /**
     * The request body.
     */
    private RequestBody requestBody;
    /**
     * The status.
     */
    private boolean status = true;

    /**
     * Instantiates a new api service.
     *
     * @param context the context
     */
    public ApiService(Context context) {
        this.context = context;
    }

    /**
     * Instantiates a new api service.
     *
     * @param context the context
     */
    public ApiService(Context context, boolean progressStatus) {
        this.context = context;
        status = progressStatus;

    }

    /**
     * Sets the on task completion listener.
     *
     * @param listener the new on task completion listener
     */
    public void setOnTaskCompletionListener(OnTaskCompleted listener) {
        this.listener = listener;
    }

    /**
     * Sets the request body.
     *
     * @param requestBody the new request body
     */
    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (status) {
            progressDialog = new DoProgressDialog(context);
            progressDialog.showProgress();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Response response = postRequest(params[0]);
            String responseBody = response.body().string();
            LogMessage.e("GCM RESPONSE:::", "" + responseBody);
            return responseBody;
        } catch (Exception e) {
            LogMessage.e(e);
        }
        return null;
    }

    private Response postRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request.Builder request = new Request.Builder();
        request.url(url);
        String auth = ((MApplication) context.getApplicationContext()).getStringFromPreference("Authorization");
        if (!TextUtils.isEmpty(auth)) {
            request.addHeader("Authorization", auth);
        }
        Log.i("server call", "url::" + url);
        if (requestBody != null)
            request.post(requestBody);
        Response response = client.newCall(request.build()).execute();
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (listener != null)
            listener.onApiResponse(s);
    }

    public enum METHOD {GET, POST, DELETE, PUT}

    /**
     * The Interface OnTaskCompleted.
     */
    public interface OnTaskCompleted {

        /**
         * On api response.
         *
         * @param response the response
         */

        /**
         * Callback of the login request
         *
         * @param response Login response from the server.
         */
        void onApiResponse(String response);
    }
}
