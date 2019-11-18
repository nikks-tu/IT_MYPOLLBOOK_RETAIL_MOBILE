/*
 * @category   ContusMessanger
 * @package    com.contusfly.utils
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */

package com.contusfly.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.contus.app.Constants;
import com.contus.chatlib.listeners.OnTaskCompleted;
import com.contusfly.MApplication;
import com.new_chanages.AndroidMultiPartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.zip.Compressor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;


/*class ImageUploadS3 */

/**
 * The Class ImageUploadS3.
 */
public class ImageUploadS3 {



    /** The task completed. */
    private OnTaskCompleted taskCompleted;

    /** The en code img. */
    private String bucketName, type, enCodeImg,folder;

    String Imagetype;
    long totalSize = 0;
    /** The length of file. */
    private int lengthOfFile;
    Context context;

    /**
     * Instantiates a new image upload s3.
     *
     */
    public ImageUploadS3(Context context) {

        this.context=context;

    }

    /**
     * Uploding callback.
     *
     * @param taskCompleted
     *            the task completed
     */
    public void uplodingCallback(OnTaskCompleted taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    /* Async tassk to upload images to s3 */

    /**
     * Execute upload.
     *
     * @param file
     *            the file
     * @param type
     *            the type
     * @param encodeImag
     *            the encode imag
     */
    public void executeUpload(File file, String type, String encodeImag,String folder) {
        this.type = type;
        this.folder=folder;
        this.enCodeImg = encodeImag;
        new S3PutObjectTask().execute(file);
    }



    /**
     * Intial setup.
     */


    /* class for S3PutObjectTask perform Async task for image uploading */

    /**
     * The Class S3PutObjectTask.
     */
    public class S3PutObjectTask extends AsyncTask<File, Integer, S3TaskResult> {

        /** The id value. */
        String idValue;
        String url="";

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {

            url=MApplication.getString(context,"image_upload_url");
            super.onPreExecute();
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            LogMessage.v("Image Upload::::", "::progress::" + values[0]);

        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        protected S3TaskResult doInBackground(File... params) {
            S3TaskResult result = null;
            try {
                result = new S3TaskResult();
                String uuid = UUID.randomUUID().toString();
                if ("image".equalsIgnoreCase(type)) {

                    java.util.Date dt = new java.util.Date();
                    String FOLDER_TYPE = folder ; //[folders are type POLLS, PROFILES]
                    String fileName =FOLDER_TYPE + Calendar.getInstance().get(Calendar.YEAR) +"" +
                            (Calendar.getInstance().get(Calendar.MONTH)+1) +"" +
                            (Calendar.getInstance().get(Calendar.DATE)) +"_" +
                            dt.getTime() + ".jpg";
                    idValue=fileName;



                    //idValue = (uuid + "_" + System.currentTimeMillis() / 1000)
                          //  + ".jpg";
                } else if ("audio".equalsIgnoreCase(type)) {
                    idValue = (uuid + "_" + System.currentTimeMillis() / 1000)
                            + ".aac";
                } else if ("video".equalsIgnoreCase(type)) {
                    idValue = (uuid + "_" + System.currentTimeMillis() / 1000)
                            + ".mp4";
                }


                String responseString = null;
                HttpParams params1 = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(params1, 3000);
                HttpConnectionParams.setSoTimeout(params1, 60000);
                HttpClient httpclient = new DefaultHttpClient(params1);
                HttpPost httppost = new HttpPost(url+"/api/v1/imageupload");


                try {
                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                            new AndroidMultiPartEntity.ProgressListener() {
                                @Override
                                public void transferred(long num) {
                                    publishProgress((int) ((num / (float) totalSize) * 100));
                                }
                            });
                    //   arrayList = getFilePaths();

                    if(params[0]!=null&&idValue!=null)
                    {
                        entity.addPart("image", new FileBody(params[0]));
                        entity.addPart("image_url", new StringBody(idValue));
                    }
                    entity.addPart("action", new StringBody("imageuploadapi"));



                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);
                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        responseString = EntityUtils.toString(r_entity);

                        JSONObject object = new JSONObject(responseString);


                        String url =object.optString("status_msg");
                        URL finalurl = new URL(url);
                        result.setUrl(finalurl);


                    } else {
                        responseString = "Error occurred! Http Status Code: " + statusCode;
                    }
                } catch (ClientProtocolException e) {
                    Log.e("Exception", "Client Protocol Exception in multipart entity: " + e.getLocalizedMessage());
                } catch (IOException e) {
                    Log.e("Exception", "IO Exception in multipart entity: " + e.getLocalizedMessage());
                }


            }
            catch (Exception ae)
            {
                ae.printStackTrace();
            }

            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @SuppressWarnings("unused")
        @Override
        protected void onPostExecute(S3TaskResult result) {
            super.onPostExecute(result);
            if (taskCompleted != null) {
                if (result.getUrl() != null) {
                    taskCompleted.onTaskCompleted(result.getUrl(), type,
                            enCodeImg, lengthOfFile);
                } else {
                    taskCompleted.onTaskCompleted(null, null, null, 0);
                }
            }
        }
    }

    /**
     * The Class S3TaskResult.
     */
    class S3TaskResult {

        /** The uri. */
        private URL uri = null;

        /*
         * method for get the url amazon s3
         */

        /**
         * Gets the url.
         *
         * @return the url
         */
        public URL getUrl() {
            return uri;
        }

        /*
         * method for set the url amazon s3
         */

        /**
         * Sets the url.
         *
         * @param uri
         *            the new url
         */
        public void setUrl(URL uri) {
            this.uri = uri;
        }

    }

}
