/**
 * @category ContusMessanger
 * @package com.contusfly.utils
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.contus.chatlib.utils.ContusConstantValues;
import com.contus.residemenu.MenuActivity;
import com.contusfly.MApplication;
import com.contusfly.emoji.Emojicon;
import com.contusfly.emoji.EmojiconGridView;
import com.contusfly.emoji.EmojiconsPopup;
import com.contusfly.model.ChatMsg;
import com.contusfly.model.Contact;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.PendingNotification;
import com.contusfly.service.ChatService;
import com.contusfly.views.CustomTextView;
import com.contusfly.views.DoProgressDialog;
import com.contusfly.views.RoundedCornersTransformation;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.netcompss.loader.LoadJNI;
import com.polls.polls.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import eu.janmuller.android.simplecropimage.CropImage;

/**
 * The Class Utils.
 */
public class Utils {

    private static MediaScannerConnection msc;

    /**
     * The typeface.
     */
    private Typeface typeface;

    static String CountryZipCode="";

    /**
     * The listener.
     */
    private OnOptionSelection listener;

    /**
     * The contact result.
     */
    private static ContactResult contactResult;
    private NotificationCompat.Builder notificationBuilder;

    /**
     * The compression listener.
     */
    private static VideoCompression compressionListener;

    /**
     * The Interface OnOptionSelection.
     */
    public interface OnOptionSelection {

        /**
         * On option selected.
         *
         * @param type     the type
         * @param position the position
         */
        void onOptionSelected(String type, int position);
    }

    /**
     * The Interface ContactResult.
     */
    public interface ContactResult {

        /**
         * On contact result.
         *
         * @param mobileNos the mobile nos
         */
        void OnContactResult(String mobileNos);
    }

    /**
     * Sets the on option listener.
     *
     * @param listener the new on option listener
     */
    public void setOnOptionListener(OnOptionSelection listener) {
        this.listener = listener;
    }


    /**
     * The Interface VideoCompression.
     */
    public interface VideoCompression {

        /**
         * On compression completed.
         *
         * @param path the path
         */
        void onCompressionCompleted(String path);
    }


    /**
     * Inits the.
     *
     * @param txtView the txt view
     * @param context the context
     * @param attrs   the attrs
     */
    public void init(TextView txtView, Context context, AttributeSet attrs) {
        try {
            typeface = getCustomFont(context, attrs);
            if (typeface != null)
                txtView.setTypeface(typeface);
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    /**
     * Inits the.
     *
     * @param edtText the edt text
     * @param context the context
     * @param attrs   the attrs
     */
    public void init(EditText edtText, Context context, AttributeSet attrs) {
        try {
            typeface = getCustomFont(context, attrs);
            if (typeface != null)
                edtText.setTypeface(typeface);
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    /**
     * Gets the custom font.
     *
     * @param context the context
     * @param attrs   the attrs
     * @return the custom font
     */
    public Typeface getCustomFont(Context context, AttributeSet attrs) {
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.CustomWidget);
            int arrayCount = typedArray.getIndexCount();
            for (int i = 0; i < arrayCount; i++) {
                int attribute = typedArray.getIndex(i);
                if (attribute == R.styleable.CustomWidget_font_name) {
                    typeface = Typeface.createFromAsset(context.getResources()
                            .getAssets(), typedArray.getString(attribute));
                }
            }
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
        return typeface;
    }

    /**
     * Generate random string for the Chat Resource
     *
     * @param mApplication
     * @return the string
     */
    public static String getResource(MApplication mApplication) {
        String key = mApplication.getStringFromPreference(Constants.CHAT_RESOURCE);
        if (TextUtils.isEmpty(key)) {
            Random random = new Random();
            key = "Mobile-" + Math.abs(random.nextInt());
            MApplication.storeStringInPreference(Constants.CHAT_RESOURCE, key);
        }
        return key;
    }

    /**
     * Gets encoded string.
     *
     * @param message the message
     * @return the encoded string
     */
    public static String getEncodedString(String message) {
        String working = "";
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (!Character.isUnicodeIdentifierPart(c)) {
                working += String.format("\\u%04x", (int) message.charAt(i));
            } else {
                working += Character.toString(c);
            }
        }
        return working;
    }

    /**
     * Gets decoded string.
     *
     * @param message the message
     * @return the decoded string
     */
    public static String getDecodedString(String message) {
        String working = message == null ? "" : message;
        int index;
        index = working.indexOf("\\u");
        while (index > -1) {
            int length = working.length();
            if (index > (length - 6))
                break;
            int numStart = index + 2;
            int numFinish = numStart + 4;
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring, 16);
            String stringStart = working.substring(0, index);
            String stringEnd = working.substring(numFinish);
            working = stringStart + ((char) number) + stringEnd;
            index = working.indexOf("\\u");
        }
        return working;
    }

    public static boolean checkIsEmpty(String strValue) {
        return strValue == null || strValue.isEmpty();
    }

    public static void scanMedia(Context context, final String file) {
        msc = new MediaScannerConnection(context, new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onScanCompleted(String path, Uri uri) {
                msc.disconnect();
            }

            public void onMediaScannerConnected() {
                if (msc != null && msc.isConnected())
                    msc.scanFile(file, getMimeType(file));
            }
        });
        msc.connect();
    }



    public  static String getContactName(final String phoneNumber, Context context)
    {
        Uri uri= Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getUserFromJid(String jid) {
        return TextUtils.split(jid, "@")[0];
    }

    /**
     * Check file size boolean.
     *
     * @param context  the context
     * @param fileName the file name
     * @return the boolean
     */
    public static boolean checkFileSize(Context context, String fileName) {
        MApplication mApplication = (MApplication) context.getApplicationContext();
        String maxSize = mApplication.returnEmptyStringIfNull(mApplication.getStringFromPreference(Constants.MAX_UPLOAD_SIZE));
        if (!maxSize.isEmpty()) {
            File file = new File(fileName);
            int fileInKb = (int) file.length() / 1024;
            int fileInMb = fileInKb / 1024;
            Log.e("File length", String.valueOf(fileInMb));
            return Integer.parseInt(maxSize) > fileInMb;
        }
        return true;
    }

    /**
     * Checks if is contact available.
     *
     * @param activity the activity
     * @param number   the number
     * @return the contact
     */
    public static Contact isContactAvailable(Activity activity, String number) {
        Contact contact = new Contact();
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = activity.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            contact.setSaved(true);
            contact.setContactName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            cur.close();
        }
        return contact;
    }

    /**
     * Open the add-contact screen with pre-filled info.
     *
     * @param context  Activity context
     * @param name     the name
     * @param mobileNo the mobile no
     */
    public static void addContactInMobile(final Activity context, String name, String mobileNo) {
        try {
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, mobileNo);
            intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
            context.startActivityForResult(intent, Constants.CONTACT_REQ_CODE);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Sets the group chat status.
     *
     * @param imageView the image view
     * @param msgId     the msg id
     */
    public static void setGroupChatStatus(ImageView imageView, String msgId, String strReceivers) {
        try {
            String[] receivers = TextUtils.split(strReceivers, ",");
            int seenCount = MDatabaseHelper.getGroupMsgStatus(msgId, true, strReceivers);
            int totalUsers = receivers.length;
            if (seenCount == totalUsers)
                setChatStatus(imageView, Constants.CHAT_SEEN_BY_RECEIVER);
            else {
                int deliveryCount = MDatabaseHelper.getGroupMsgStatus(msgId, false, strReceivers);
                if (deliveryCount == totalUsers)
                    setChatStatus(imageView, Constants.CHAT_REACHED_TO_RECEIVER);
                else
                    setStatus(imageView, msgId);
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    private static void setStatus(ImageView imageView, String msgId) {
        String status = MDatabaseHelper.getGrpMsgStatus(msgId);
        if (status != null && !status.isEmpty())
            setChatStatus(imageView, Integer.parseInt(status));
        else
            setChatStatus(imageView, Constants.CHAT_SENT_FROM_SENDER);
    }


    /**
     * Sets the chat status.
     *
     * @param imageView the image view
     * @param status    the status
     */
    public static void setChatStatus(ImageView imageView, int status) {
        switch (status) {
            case Constants.CHAT_SENT_FROM_SENDER:
                imageView.setImageResource(R.drawable.ic_timerchat);
                break;
            case Constants.CHAT_REACHED_TO_SERVER:
                imageView.setImageResource(R.drawable.single_tick);
                break;
            case Constants.CHAT_REACHED_TO_RECEIVER:
                imageView.setImageResource(R.drawable.double_tick_seen);
                break;
            case Constants.CHAT_SEEN_BY_RECEIVER:
                imageView.setImageResource(R.drawable.double_tick);
                break;
            default:
                break;
        }
    }

    /**
     * Sets the recent chat status.
     *
     * @param imageView the image view
     * @param status    the status
     */
    public static void setRecentChatStatus(ImageView imageView, int status) {
        imageView.setVisibility(View.VISIBLE);
        switch (status) {
            case Constants.CHAT_SENT_FROM_SENDER:
                imageView.setImageResource(R.drawable.ic_timerchat);
                break;
            case Constants.CHAT_REACHED_TO_SERVER:
                imageView.setImageResource(R.drawable.single_tick);
                break;
            case Constants.CHAT_REACHED_TO_RECEIVER:
                imageView.setImageResource(R.drawable.double_tick_seen);
                break;
            case Constants.CHAT_SEEN_BY_RECEIVER:
                imageView.setImageResource(R.drawable.double_tick);
                break;
            default:
                break;
        }
    }

    /**
     * Show image option dialog.
     *
     * @param context the context
     * @param options the options
     * @param type    the type
     */
    public void showImageOptionDialog(Context context, String[] options,
                                      final String type) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(true);
        alertDialog.setTitle(context.getString(R.string.txt_choose_option));

        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                if (listener != null)
                    listener.onOptionSelected(type, item);
            }
        });
        alertDialog.create().show();
    }

    /**
     * Load image with glide.
     *
     * @param context  the context
     * @param imgUrl   the img url
     * @param imgView  the img view
     * @param errorImg the error img
     */
    public static void loadImageWithGlide(Context context, String imgUrl,
                                          ImageView imgView, int errorImg) {
        if (imgUrl != null && !imgUrl.isEmpty())
            Glide.with(context).load(imgUrl.replaceAll(" ", "%20")).diskCacheStrategy(DiskCacheStrategy.ALL).error(errorImg)
                    .placeholder(errorImg).into(imgView);
        else
            imgView.setImageResource(errorImg);
    }

    /**
     * Load image with glide.
     *
     * @param context  the context
     * @param imgUrl   the img url
     * @param imgView  the img view
     * @param errorImg the error img
     */
    public static void loadImageWithGlideProfileRounderCorner(Context context, String imgUrl, ImageView imgView, int errorImg) {

        Log.d("likeimage"," 514imgUrl="+imgUrl.toString()+"imgView="+ imgView.toString()+"");

        if (imgUrl != null && !imgUrl.isEmpty()) {


            Glide.with(context).load(Uri.parse(imgUrl.replaceAll(" ", "%20"))).diskCacheStrategy(DiskCacheStrategy.ALL).override(80, 100).error(errorImg).fitCenter().bitmapTransform(new RoundedCornersTransformation(context, 20, 50, 0,
                    RoundedCornersTransformation.CornerType.ALL))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .placeholder(errorImg).into(imgView);
            //Log.d("likeimageimgUrl != null","img");

        }
        else {
            imgView.setImageResource(errorImg);
            //Log.d("likeimageimgUrl==null","img");
        }
    }

    /**
     * Load image with glide.
     *
     * @param context  the context
     * @param imgUrl   the img url
     * @param imgView  the img view
     * @param errorImg the error img
     */
    public static void loadImageWithGlideRounderCorner(Context context, String imgUrl,
                                                       ImageView imgView, int errorImg) {
        if (imgUrl != null && !imgUrl.isEmpty())
            Glide.with(context).load(Uri.parse(imgUrl.replaceAll(" ", "%20"))).diskCacheStrategy(DiskCacheStrategy.ALL).error(errorImg).fitCenter().bitmapTransform(new RoundedCornersTransformation(context, 10, 20, 0,
                    RoundedCornersTransformation.CornerType.ALL))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .placeholder(errorImg).into(imgView);
        else
            imgView.setImageResource(errorImg);
    }

    /**
     * Load image with glide.
     *
     * @param context  the context
     * @param imgUrl   the img url
     * @param imgView  the img view
     * @param errorImg the error img
     */
    public static void loadImageWithGlideSingleImageRounderCorner(Context context, String imgUrl,
                                                                  ImageView imgView, int errorImg) {
        if (imgUrl != null && !imgUrl.isEmpty())
            Glide.with(context).load(Uri.parse(imgUrl.replaceAll(" ", "%20"))).diskCacheStrategy(DiskCacheStrategy.ALL).error(errorImg).fitCenter().bitmapTransform(new RoundedCornersTransformation(context, 20, 60, 0,
                    RoundedCornersTransformation.CornerType.ALL))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .placeholder(errorImg).into(imgView);
        else
            imgView.setImageResource(errorImg);
    }

    /**
     * Sets the emoji text.
     *
     * @param textView the text view
     * @param text     the text
     */
    public static void setEmojiText(CustomTextView textView, String text) {
        try {
            textView.setText(URLDecoder.decode(text, "UTF-8").replaceAll("%", "%25"));
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
    }

    /**
     * Load image with glide.
     *
     * @param context  the context
     * @param imgFile  the img file
     * @param imgView  the img view
     * @param errorImg the error img
     */
    public static void loadImageWithGlide(Context context, File imgFile,
                                          ImageView imgView, int errorImg) {
        Glide.with(context).load(imgFile).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).error(errorImg)
                .placeholder(errorImg).into(imgView);
    }

    /**
     * Send email.
     *
     * @param context   the context
     * @param subject   the subject
     * @param msg       the msg
     * @param toAddress the to address
     */
    public static void sendEmail(Context context, String subject, String msg,
                                 String[] toAddress) {
        Intent mIntent = new Intent(Intent.ACTION_SEND);
        mIntent.setType("message/rfc822");
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        mIntent.putExtra(Intent.EXTRA_TEXT, msg);
        mIntent.putExtra(Intent.EXTRA_EMAIL, toAddress);
        context.startActivity(mIntent);
    }

    /**
     * Sets the up keyboard.
     *
     * @param emojiconsPopup the emojicons popup
     * @param imgSmiley      the img smiley
     * @param editText       the edit text
     */
    public static void setUpKeyboard(final EmojiconsPopup emojiconsPopup,
                                     final ImageView imgSmiley, final EditText editText) {
        try {
            emojiconsPopup.setSizeForSoftKeyboard();
            emojiconsPopup
                    .setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {
                        @Override
                        public void onEmojiconClicked(Emojicon emojicon) {
                            editText.append(emojicon.getEmoji());
                        }
                    });
            emojiconsPopup
                    .setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

                        @Override
                        public void onEmojiconBackspaceClicked(View v) {
                            KeyEvent event = new KeyEvent(0, 0, 0,
                                    KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                                    KeyEvent.KEYCODE_ENDCALL);
                            editText.dispatchKeyEvent(event);
                        }
                    });
            /*
             * emojiconsPopup.setOnDismissListener(new
             * PopupWindow.OnDismissListener() {
             * 
             * @Override public void onDismiss() {
             * imgSmiley.setImageResource(R.drawable.ic_smiley); } });
             */
            emojiconsPopup
                    .setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

                        @Override
                        public void onKeyboardOpen(int keyBoardHeight) {
                        }

                        @Override
                        public void onKeyboardClose() {
                            if (emojiconsPopup.isShowing())
                                emojiconsPopup.dismiss();
                        }
                    });
            // On emoji clicked, add it to edittext
            emojiconsPopup
                    .setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {
                        @Override
                        public void onEmojiconClicked(Emojicon emojicon) {
                            editText.append(emojicon.getEmoji());
                        }
                    });
            // On backspace clicked, emulate the KEYCODE_DEL key event
            emojiconsPopup
                    .setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {
                        @Override
                        public void onEmojiconBackspaceClicked(View v) {
                            KeyEvent event = new KeyEvent(0, 0, 0,
                                    KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                                    KeyEvent.KEYCODE_ENDCALL);
                            editText.dispatchKeyEvent(event);
                        }
                    });

        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    /**
     * Send pending messages.
     *
     * @param context the context
     */
    public static void sendPendingMessages(Context context) {
        new MsgTask(context).execute();
    }

    public interface OnOptionSeletction {
    }

    /**
     * The Class MsgTask.
     */
    private static class MsgTask extends AsyncTask<Void, Void, Void> {

        /**
         * The context.
         */
        Context context;

        /**
         * Instantiates a new msg task.
         *
         * @param context the context
         */
        public MsgTask(Context context) {
            this.context = context;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        @Override
        protected Void doInBackground(Void... params) {
            try {
                /*
                 * The m pending msgs.
                 */
                List<ChatMsg> mPendingMsgs;
                mPendingMsgs = MDatabaseHelper.getPendingMsg();
                int pendingMsgCount;
                if (mPendingMsgs != null
                        && (pendingMsgCount = mPendingMsgs.size()) > 0) {
                    for (int i = 0; i < pendingMsgCount; i++) {
                        sndPending(i, context, mPendingMsgs);
                    }
                }
            } catch (Exception e) {
                LogMessage.e(Constants.TAG, e + "");
            }
            return null;
        }
    }

    /**
     * Snd pending.
     *
     * @param index        the index
     * @param context      the context
     * @param mPendingMsgs the m pending msgs
     */
    private static void sndPending(int index, Context context,
                                   List<ChatMsg> mPendingMsgs) {
        try {
            ChatMsg mChatMsg = mPendingMsgs.get(index);
            if (mChatMsg.getMsgType().equalsIgnoreCase(Constants.MSG_TYPE_IMAGE)
                    || mChatMsg.getMsgType().equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)
                    || mChatMsg.getMsgType().equalsIgnoreCase(Constants.MSG_TYPE_AUDIO)) {
                context.startService(new Intent(context, ChatService.class).setAction(
                        Constants.ACTION_MEIDA_UPLOAD).putExtra(
                        Constants.CHAT_MESSAGE, mChatMsg));
            } else {
                Intent mIntent = new Intent(context, ChatService.class);
                mIntent.putExtra("to", mChatMsg.getMsgTo());
                mIntent.putExtra("mid", mChatMsg.getMsgId());
                mIntent.putExtra("msg", mChatMsg.getMsgBody());
                mIntent.putExtra("from", mChatMsg.getMsgFrom());
                mIntent.putExtra(Constants.CHAT_MSG_TYPE, mChatMsg.getMsgType());
                mIntent.setAction(ContusConstantValues.CONTUS_XMPP_ACTION_CHAT_REQUEST);
                context.startService(mIntent);
            }
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    /**
     * Sets the up tool bar.
     *
     * @param context   the context
     * @param toolbar   the toolbar
     * @param actionBar the action bar
     * @param title     the title
     */
    public static void setUpToolBar(final Activity context, Toolbar toolbar,
                                    ActionBar actionBar, String title) {
        try {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(title);
            toolbar.getNavigationIcon().setColorFilter(
                    context.getResources().getColor(android.R.color.white),
                    PorterDuff.Mode.SRC_IN);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.finish();
                }
            });
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    /**
     * Crop image.
     *
     * @param context the context
     * @param file    the file
     */
    public static void cropImage(Activity context, File file) {
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 5);
        intent.putExtra(CropImage.ASPECT_Y, 5);
        Log.e("file.getPath()", file.getPath() + "");
        context.startActivityForResult(intent, Constants.CROP_IMAGE);
    }

    /**
     * Copy stream.
     *
     * @param input  the input
     * @param output the output
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    /**
     * Send email conversation.
     *
     * @param context     the context
     * @param rosterID    the roster id
     * @param contactName the contact name
     * @return true, if successful
     */
    public static boolean sendEmailConversation(Context context,
                                                String rosterID, String contactName) {
        try {
            if (rosterID != null && !rosterID.isEmpty()) {
                MApplication mApplication = (MApplication) context
                        .getApplicationContext();
                List<ChatMsg> chatHistory = MDatabaseHelper.getChatHistory(
                        mApplication
                                .getStringFromPreference(Constants.USERNAME),
                        rosterID);
                if (chatHistory.isEmpty()) {
                    return false;
                } else {
                    return new EmailTask(context, contactName).execute(
                            chatHistory).get();
                }
            } else
                return false;
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
        return false;
    }

    /**
     * The Class EmailTask.
     */
    private static class EmailTask extends
            AsyncTask<List<ChatMsg>, Void, Boolean> {

        /*
         * The context.
         */
        @SuppressLint("StaticFieldLeak")
        private Context context;

        /**
         * The contact name.
         */
        private String emailContent = "", contactName;

        /**
         * The progress dialog.
         */
        private DoProgressDialog progressDialog;

        /**
         * Instantiates a new email task.
         *
         * @param context     the context
         * @param contactName the contact name
         */
        EmailTask(Context context, String contactName) {
            this.context = context;
            this.contactName = contactName;
            progressDialog = new DoProgressDialog(context);
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.showProgress();
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        @Override
        protected Boolean doInBackground(List<ChatMsg>... params) {
            List<ChatMsg> item = params[0];
            emailContent = prepareMsg(item);
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            sendEmail(context, context.getString(R.string.app_name) + " "
                    + context.getString(R.string.text_msg_conversation_with)
                    + " " + contactName, emailContent, new String[]{});
        }
    }

    /**
     * Prepare msg.
     *
     * @param item the item
     * @return the string
     */
    private static String prepareMsg(List<ChatMsg> item) {
        String emailContent = "";
        try {
            int length = item.size();
            for (int i = 0; i < length; i++) {
                ChatMsg rowItem = item.get(i);
                String content = "", name = "", msg = "";
                int isSender = rowItem.getSender();
                if (isSender == Constants.CHAT_FROM_SENDER) {
                    name = rowItem.getMsgFrom();
                } else
                    name = rowItem.getMsgTo();
                Date dateObj = new Date();
                dateObj.setTime(Long.parseLong(rowItem.getMsgTime()) * 1000);
                String msgTime = new SimpleDateFormat("dd-MM-yyyy hh:mm")
                        .format(dateObj).toString();
                String msgType = rowItem.getMsgType();
                if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_TEXT)
                        || msgType
                        .equalsIgnoreCase(Constants.MSG_TYPE_LOCATION)) {
                    msg = rowItem.getMsgBody();
                } else
                    msg = "Media";
                msg = URLDecoder.decode(msg, "UTF-8");
                content = msgTime + " - " + name + " : " + msg + "\n";
                emailContent += content;
            }
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
        return emailContent;
    }

    public static void showEmojiKeyboard(Context context, final EditText edtMsg, final EmojiconsPopup popup) {

        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();

        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (edtMsg == null || emojicon == null) {
                    return;
                }
                int start = edtMsg.getSelectionStart();
                int end = edtMsg.getSelectionEnd();
                if (start < 0) {
                    edtMsg.append(emojicon.getEmoji());
                } else {
                    edtMsg.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                edtMsg.dispatchKeyEvent(event);
            }
        });
        if (!popup.isShowing()) {
            edtMsg.setFocusableInTouchMode(true);
            edtMsg.requestFocus();
            ((InputMethodManager) edtMsg.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edtMsg, InputMethodManager.SHOW_IMPLICIT);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    popup.showAtBottomPending();
                }
            }, 150);
        } else {
            popup.dismiss();
        }
    }

    /**
     * Show emoji keyboard.
     *
     * @param context        the context
     * @param emojiconsPopup the emojicons popup
     */
    public static void dismissKeyboard(Context context,
                                       final EmojiconsPopup emojiconsPopup) {
        emojiconsPopup.dismiss();
    }

    /**
     * Show custom tones.
     *
     * @param context     the context
     * @param reqCode     the req code
     * @param ringtoneUri
     */
    public static void showCustomTones(Activity context, int reqCode, String ringtoneUri) {
        Uri notificationUri;
        if (TextUtils.isEmpty(ringtoneUri)) {
            notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        } else {
            notificationUri = Uri.parse(ringtoneUri);
        }
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, context.getString(R.string.text_notification));
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, notificationUri);
        context.startActivityForResult(intent, reqCode);
    }

    /**
     * Update roster tone.
     *
     * @param fileUri  the file uri
     * @param rosterId the roster id
     */
    public static void updateRosterTone(String fileUri, String rosterId) {
        try {
            ContentValues mValues = new ContentValues();
            mValues.put(Constants.ROSTER_CUSTOM_TONE, fileUri);
            MDatabaseHelper.updateValues(Constants.TABLE_ROSTER, mValues,
                    Constants.ROSTER_USER_ID + "='" + rosterId + "'");
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    /**
     * Open media file.
     *
     * @param context   the context
     * @param filePath  the file path
     * @param mediaType the media type
     */
    public static void openMediaFile(Context context, String filePath,
                                     String mediaType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + filePath), mediaType + "/*");
        context.startActivity(intent);
    }

    /**
     * Gets the mobile contacts.
     *
     * @param context           the context
     * @param listener          the listener
     * @param deviceCountryCode
     * @return the mobile contacts
     */
    public static void getMobileContacts(Context context, ContactResult listener, String deviceCountryCode) {
        contactResult = listener;
        new ContactSync(context, deviceCountryCode).execute();
    }

    /**
     * The Class ContactSync.
     */
    public static class ContactSync extends AsyncTask<Void, Void, String> {

        private final String deviceCountryCode;
        private final PhoneNumberUtil phoneUtil;
        private String phoneCode;
        /*
         * The context.
         */
        @SuppressLint("StaticFieldLeak")
        private Context context;
        private String mobileNos;

        /**
         * The m application.
         */
        private MApplication mApplication;


        /**
         * Instantiates a new contact sync.
         *
         * @param context           the context
         * @param deviceCountryCode
         */
        public ContactSync(Context context, String deviceCountryCode) {
            this.context = context;
            phoneCode = MApplication.getString(context, com.contus.app.Constants.PHONE_NUMBER_CODE);
            mApplication = (MApplication) context.getApplicationContext();
            this.deviceCountryCode = deviceCountryCode;
            phoneUtil = PhoneNumberUtil.getInstance();
        }


        /*
                 * (non-Javadoc)
                 *
                 * @see android.os.AsyncTask#doInBackground(Params[])
                 */
        @Override
        protected String doInBackground(Void... voids) {
            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            String mobileNos = Constants.EMPTY_STRING;
            String phonedata=Constants.EMPTY_STRING;
            if (cur != null && cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(
                            cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (pCur != null) {
                            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                            TelephonyManager mManager = (TelephonyManager) context
                                    .getSystemService(Context.TELEPHONY_SERVICE);
                            String countryCode = mManager.getNetworkCountryIso().toUpperCase();
                            if(countryCode.isEmpty())
                            {
                                countryCode="91";
                            }
                            GetCountryZipCode(countryCode,context);

                            if (CountryZipCode.isEmpty())
                                countryCode = MApplication.getString(context, Constants.PHONE_NUMBER_CODE);
                            if (!CountryZipCode.isEmpty()) {
                                while (pCur.moveToNext()) {
                                    String phoneNo = pCur.getString(
                                            pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    phoneNo = phoneNo.replaceAll("\\*", "").replaceAll("#", "");
                                    if (phoneNo.length() >= 6) {
                                        Phonenumber.PhoneNumber phoneNumber;
                                        phoneNo = phoneNo.replace("+", "").replace("-", "").replaceAll("\\(","").replaceAll("\\)","").replaceAll("\\s", "");
                                        /*try {
                                            // phone must begin with '+'
                                            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNo, "");

                                            int phoneCountryCode = numberProto.getCountryCode();
                                             phonedata= String.valueOf(numberProto);
                                            Log.e("countrycode", String.valueOf(phonedata));
                                        } catch (NumberParseException e) {
                                            System.err.println("NumberParseException was thrown: " + e.toString());
                                        }*/




                                            // String numStr = "(123) 456-7890";

                                      /*  try {
                                            phoneNumber = phoneUtil.parse(phoneNo, countryCode);
                                            phoneNo = phoneUtil.format(phoneNumber,
                                                    PhoneNumberUtil.PhoneNumberFormat.E164);
                                        } catch (NumberParseException e) {
                                            LogMessage.e(Constants.TAG, e);
                                        }
*/
                                        String phoneSub = phoneNo.substring(0,2);
                                        String phonesub2=phoneNo.substring(0,3);
                                        String phonesub3=phoneNo.substring(0,4);

                                        if(CountryZipCode.length()==2) {
                                            if (!phoneSub.contains(CountryZipCode))
                                                phoneNo = CountryZipCode+phoneNo;
                                        }
                                        else if(CountryZipCode.length()==3)
                                        {
                                            if (!phonesub2.contains(CountryZipCode))
                                                phoneNo = CountryZipCode+ phoneNo;
                                        }
                                        else if(CountryZipCode.length()==4)
                                        {
                                            if (!phonesub3.contains(CountryZipCode))
                                                phoneNo = CountryZipCode+ phoneNo;
                                        }
                                        mobileNos = mobileNos + "," + phoneNo;
                                        if (mobileNos.startsWith(","))
                                            mobileNos = mobileNos.substring(1, mobileNos.length());
                                        insertContacts(name, phoneNo);
                                    }
                                }
                            }
                            pCur.close();
                        }
                    }
                }
                cur.close();
            }
            Log.e("Contscts::", "Contacts::" + mobileNos);
            return mobileNos;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("s", s + "");
            if (contactResult != null)
                contactResult.OnContactResult(s);
        }
    }



    public static String GetCountryZipCode(String countrycode,Context con){

        String[] rl=con.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");

            if(g[1].trim().equals(countrycode.trim())){
                CountryZipCode=g[0];
                break;
            }
            else
            {
                CountryZipCode=countrycode;
            }
        }
        Log.e("countrycode",CountryZipCode);
        return CountryZipCode;
    }
    /**
     * Insert contacts.
     *
     * @param name     the name
     * @param mobileNo the mobile no
     */
    private static void insertContacts(String name, String mobileNo) {
        try {
            ContentValues mValues = new ContentValues();
            mValues.put(Constants.ROSTER_NAME, name);
            if (MDatabaseHelper.checkIDStatus(mobileNo, Constants.TABLE_ROSTER, Constants.ROSTER_USER_ID)) {
                String where = Constants.ROSTER_USER_ID + "='" + mobileNo + "'";
                MDatabaseHelper.updateValues(Constants.TABLE_ROSTER, mValues, where);
            } else {
                mValues.put(Constants.ROSTER_USER_ID, mobileNo);
                mValues.put(Constants.ROSTER_AVAILABILITY, Constants.ROSTER_NOT_ACTIVE);
                mValues.put(Constants.ROSTER_IMAGE, Constants.EMPTY_STRING);
                mValues.put(Constants.ROSTER_STATUS, Constants.EMPTY_STRING);
                mValues.put(Constants.ROSTER_ADMINS, Constants.EMPTY_STRING);
                mValues.put(Constants.ROSTER_IS_BLOCKED, Constants.EMPTY_STRING);
                mValues.put(Constants.ROSTER_TYPE, String.valueOf(Constants.COUNT_ZERO));
                MDatabaseHelper.insertValues(Constants.TABLE_ROSTER, mValues);
            }
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e);
        }
    }

    /**
     * Sets the sound and vib.
     *
     * @param mApplication the m application
     * @param builder      the builder
     * @param toUserId     the to user id
     */
    public static void setSoundAndVib(MApplication mApplication,
                                      NotificationCompat.Builder builder, String toUserId) {
        try {
            String notificationURI = MApplication
                    .returnEmptyStringIfNull(MDatabaseHelper
                            .getRosterCustomTone(toUserId));
            if (notificationURI.isEmpty())
                notificationURI = MApplication
                        .returnEmptyStringIfNull(mApplication
                                .getStringFromPreference(Constants.NOTIFICATION_URI));
            if (notificationURI.isEmpty())
                builder.setDefaults(Notification.DEFAULT_SOUND);
            else
                builder.setSound(Uri.parse(notificationURI));

            String vibrationType = mApplication
                    .returnEmptyStringIfNull(mApplication
                            .getStringFromPreference(Constants.VIBRATION_TYPE));
            long[] vibPattern = new long[]{Constants.ONE_SECOND,
                    Constants.ONE_SECOND, Constants.ONE_SECOND};
            if (!vibrationType.isEmpty()) {
                switch (Integer.parseInt(vibrationType)) {
                    case Constants.VIB_OFF:
                        vibPattern = new long[]{};
                        break;
                    case Constants.VIB_LONG:
                        vibPattern = new long[]{Constants.ONE_SECOND,
                                Constants.ONE_SECOND, Constants.ONE_SECOND,
                                Constants.ONE_SECOND, Constants.ONE_SECOND};
                        break;
                    case Constants.VIB_SHORT:
                        vibPattern = new long[]{Constants.ONE_SECOND,
                                Constants.ONE_SECOND};
                        break;
                    default:
                        break;
                }
            }
            builder.setVibrate(vibPattern);
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    /**
     * Creates the notification.
     *
     * @param toUserId the to user id
     */
    public void createNotification(MApplication mApplication, Context context, String title,
                                   String msgType, String msgBody, String toUserId) {
        try {
            int dataLength;
            Intent notificationIntent = null;
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            /*NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(
                    context);
            notBuilder.setSmallIcon(R.drawable.app_icon);
            notBuilder.setContentTitle(title);*/
            Log.e("title", title + "");
            String getBatchCount = mApplication.getStringFromPreference(com.contus.app.Constants.GET_MESSAGE_UNREAD_COUNT);
            if (TextUtils.isEmpty(getBatchCount)) {
                mApplication.storeStringInPreference(com.contus.app.Constants.GET_MESSAGE_UNREAD_COUNT, "0");
            }
            int messageCounts = Integer.parseInt(mApplication.getStringFromPreference(com.contus.app.Constants.GET_MESSAGE_UNREAD_COUNT)) + 1;
            mApplication.storeStringInPreference(com.contus.app.Constants.GET_MESSAGE_UNREAD_COUNT, String.valueOf(messageCounts));
            LocalBroadcastManager.getInstance(mApplication).sendBroadcast(new Intent("get_chat_unread_count"));
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_new);

            if (Build.VERSION.SDK_INT >= 21) {
                notificationBuilder = new NotificationCompat.Builder(context)
                        .setLargeIcon(largeIcon)
                        .setSmallIcon(R.drawable.ic_notification_icon1)
                        .setContentTitle(title);
            } else {
                notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher_new)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentTitle(title);
            }
            if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_TEXT))
                notificationBuilder.setContentText(msgBody);
            else if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_IMAGE))
                notificationBuilder.setContentText(context.getString(R.string.txt_image));
            else if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_LOCATION))
                notificationBuilder.setContentText(context.getString(R.string.txt_location));
            else if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO))
                notificationBuilder.setContentText(context.getString(R.string.text_video));
            else notificationBuilder.setContentText(context.getString(R.string.text_audio));

            List<PendingNotification> pendingMsg = MDatabaseHelper
                    .getPendingNotifications();
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            if (pendingMsg != null && !pendingMsg.isEmpty()) {

                dataLength = pendingMsg.size();

                for (int i = 0; i < dataLength; i++) {
                    PendingNotification item = pendingMsg.get(i);
                    String type = item.getPendingChatType();
                    String msg = getDecodedString(item.getPendingChatMsg());
                    String name = getDecodedString(item.getPendingChatName());

                    msg = getMsgFormat(context, msg, type);
                    if (!name.equals("")) {
                        inboxStyle.addLine(name + " : " + msg);
                    } else {
                        inboxStyle.addLine(toUserId + " : " + msg);
                    }
                }
                notificationBuilder.setStyle(inboxStyle.setSummaryText(
                        dataLength + " " + context.getString(R.string.text_un_msg))
                        .setBigContentTitle(context.getString(R.string.app_name)));
            } else {
                insertContacts("", title);
            }
            Utils.setSoundAndVib(mApplication, notificationBuilder, toUserId);
            notificationBuilder.setAutoCancel(true);
            MApplication.setBoolean(context, "notification click", true);
            notificationIntent = new Intent(context, MenuActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notificationIntent.putExtra(Constants.IS_FROM_NOTIFICATION, true);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    Constants.NOTIFICATION_ID, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(Constants.NOTIFICATION_ID,
                    notificationBuilder.build());
        } catch (Exception e) {
            LogMessage.e(Constants.TAG, e + "");
        }
    }

    private String getMsgFormat(Context context, String msg, String msgType) {
        String formattedMsg = msg;
        if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_IMAGE))
            formattedMsg = context.getString(R.string.txt_image);
        else if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO))
            formattedMsg = context.getString(R.string.text_video);
        else if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_LOCATION))
            formattedMsg = context.getString(R.string.txt_location);
        else if (msgType.equalsIgnoreCase(Constants.MSG_TYPE_AUDIO))
            formattedMsg = context.getString(R.string.text_audio);
        return formattedMsg;
    }

    public static void sendMsgToContact(Context context, String rosterNo) {
        String mobileNo = "+" + rosterNo;
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(mobileNo, "");
            mobileNo = String.valueOf(phoneNumber.getNationalNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri uri = Uri.parse("smsto:" + mobileNo);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra("sms_body", context.getString(R.string.text_email_subject));
        context.startActivity(intent);
    }

    /**
     * Insert unknown user.
     *
     * @param userId the user id
     */
    public static void insertUnknownUser(String userId) {
        ContentValues mValues = new ContentValues();
        mValues.put(Constants.ROSTER_USER_ID, userId);
        mValues.put(Constants.ROSTER_TYPE, Constants.CHAT_TYPE_UNKNOWN);
        mValues.put(Constants.ROSTER_NAME, userId);
        MDatabaseHelper.insertValues(Constants.TABLE_ROSTER, mValues);
    }

    /**
     * Start compressing.
     *
     * @param sourcePath the source path
     * @param context    the context
     * @param listener   the listener
     */
    public static void startCompressing(String sourcePath, Context context, VideoCompression listener) {
        compressionListener = listener;
        new CompressTask(context).execute(sourcePath);
    }

    /**
     * The Class CompressTask.
     */
    private static class CompressTask extends AsyncTask<String, Void, String> {

        /**
         * The context.
         */
        Context context;

        /**
         * Instantiates a new compress task.
         *
         * @param context the context
         */
        public CompressTask(Context context) {
            this.context = context;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected String doInBackground(String... strings) {
            LoadJNI vk = new LoadJNI();
            String mediaPath = strings[0];
            try {
                MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
                metadataRetriever.setDataSource(mediaPath);
                String newRes = (Integer.parseInt(
                        metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)) / 4)
                        + "x"
                        + (Integer.parseInt(
                        metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT))
                        / 4);
                String tempStr;
                File tempFolder = new File(Environment.getExternalStorageDirectory() + "/"
                        + context.getString(R.string.app_name) + "/.temp");
                if (!tempFolder.exists())
                    tempFolder.mkdirs();
                tempStr = tempFolder.getAbsolutePath() + "/tempvideo.mp4";
                String workFolder = context.getFilesDir().getAbsolutePath();
                String[] complexCommand = {"ffmpeg", "-y", "-i", mediaPath, "-strict", "experimental", "-s", newRes,
                        "-r", "30", "-b", "15496k", "-vcodec", "mpeg4", "-ab", "48000", "-ac", "2", "-ar", "22050",
                        tempStr};
                vk.run(complexCommand, workFolder, context);
                File tempFile = new File(tempStr), oldFile;
                oldFile = new File(mediaPath);
                if (oldFile.delete() && tempFile.renameTo(new File(mediaPath)))
                    return mediaPath;
            } catch (Exception e) {
                LogMessage.e(Constants.TAG, e);
            }
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (compressionListener != null)
                compressionListener.onCompressionCompleted(s);
        }
    }

}
