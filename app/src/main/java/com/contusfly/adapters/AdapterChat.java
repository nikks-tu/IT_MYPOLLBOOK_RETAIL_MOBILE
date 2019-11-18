/**
 * @category ContusMessanger
 * @package com.contusfly.adapters
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.contusfly.MApplication;
import com.contusfly.model.ChatMsg;
import com.contusfly.model.MDatabaseHelper;
import com.contusfly.model.Rosters;
import com.contusfly.utils.ChatMsgTime;
import com.contusfly.utils.Constants;
import com.contusfly.utils.ImageLoader;
import com.contusfly.utils.LogMessage;
import com.contusfly.utils.MediaController;
import com.contusfly.utils.Utils;
import com.contusfly.views.CustomTextView;
import com.contusfly.views.CustomToast;
import com.polls.polls.R;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * The Class AdapterChatData.
 */
public class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * The m media controller.
     */
    private static MediaController mMediaController;
    /**
     * The type contact.
     */
    private final int typeImage = 2, typeText = 1, typeLocation = 3, typeAudio = 4,
            typeContact = 5, typeFile = 6, typeVideo = 7, typeStatus = 8;
    /**
     * The inflater.
     */
    private LayoutInflater inflater;
    /**
     * The m chat data.
     */
    private List<ChatMsg> mChatData;
    /**
     * The m application.
     */
    private MApplication mApplication;
    /**
     * The listener.
     */
    private OnChatItemClickListener listener;
    /**
     * The chat type.
     */
    private String chatType;
    /**
     * The chat msg time.
     */
    private ChatMsgTime chatMsgTime;
    /**
     * The context.
     */
    private Context context;
    private String toUser;
    private String lastSelectedMessageId = "";

    /**
     * Instantiates a new adapter chat data.
     *
     * @param context the context
     * @param toUser
     */
    public AdapterChat(Context context, String toUser) {
        this.toUser = toUser;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mApplication = (MApplication) context.getApplicationContext();
        this.context = context;
        chatMsgTime = new ChatMsgTime();
    }

    /**
     * Open file.
     *
     * @param context  the context
     * @param filePath the file path
     * @throws IOException the io exception
     */
    public static void openFile(Context context, String filePath) throws IOException {

        Log.e("Filea path", filePath);

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void setmMediaController(MediaController mediaController) {
        this.mMediaController = mediaController;
    }

    public void setLastSelectedMessageId(String lastSelectedMessageId) {
        this.lastSelectedMessageId = lastSelectedMessageId;
    }

    /**
     * Sets the chat type.
     *
     * @param chatType the new chat type
     */
    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    @Override
    public int getItemCount() {
        return mChatData.size();
    }

    @Override
    public int getItemViewType(int position) {
        String msgType = mChatData.get(position).getMsgType();
        switch (msgType) {
            case Constants.MSG_TYPE_TEXT:
                return typeText;
            case Constants.MSG_TYPE_AUDIO:
                return typeAudio;
            case Constants.MSG_TYPE_IMAGE:
                return typeImage;
            case Constants.MSG_TYPE_VIDEO:
                return typeVideo;
            case Constants.MSG_TYPE_LOCATION:
                return typeLocation;
            case Constants.MSG_TYPE_CONTACT:
                return typeContact;
            case Constants.MSG_TYPE_FILE:
                return typeFile;
            case Constants.MSG_TYPE_STATUS:
                return typeStatus;
            default:
                return 0;
        }
    }

    /**
     * Sets the on download click listener.
     *
     * @param listener the new on download click listener
     */
    public void setOnDownloadClickListener(OnChatItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the data.
     *
     * @param position the position
     * @param chatData the chat data
     */
    public void setData(int position, ChatMsg chatData) {
        mChatData.set(position, chatData);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;
        switch (viewType) {

            case typeText:
                view = inflater.inflate(R.layout.row_chat_txt_item, parent, false);
                holder = new TxtViewHolder(view);
                break;
            case typeImage:
                view = inflater.inflate(R.layout.row_chat_img_item, parent, false);
                holder = new ImageViewHolder(view);
                break;
            case typeVideo:
                view = inflater.inflate(R.layout.row_chat_video_item, parent, false);
                holder = new VideoViewHolder(view);
                break;
            case typeLocation:
                view = inflater.inflate(R.layout.row_location, parent, false);
                holder = new LocationViewHolder(view);
                break;
            case typeContact:
                view = inflater.inflate(R.layout.row_msg_contact_item, parent, false);
                holder = new ContactViewHolder(view);
                break;
            case typeFile:
                view = inflater.inflate(R.layout.row_chat_file_item, parent, false);
                holder = new FileViewHolder(view);
                break;
            case typeStatus:
                view = inflater.inflate(R.layout.row_chat_view_status, parent, false);
                holder = new StatusViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.row_singlechat_audio_item, parent, false);
                holder = new AudioViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            ChatMsg item = mChatData.get(position);
            switch (holder.getItemViewType()) {
                case typeText:
                    getTextView(holder, item);
                    break;
                case typeImage:
                    getImageView(holder, item, position);
                    break;
                case typeVideo:
                    getVideoView(holder, item, position);
                    break;
                case typeLocation:
                    getLocationView(holder, item, position);
                    break;
                case typeAudio:
                    getAudioView(holder, item, position);
                    break;
                case typeContact:
                    getContactView(holder, item, position);
                    break;
                case typeFile:
                    getFileView(holder, item, position);
                    break;
                case typeStatus:
                    getStatusView(holder, item);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public List<ChatMsg> getData() {
        return mChatData;
    }

    /**
     * Sets the data.
     *
     * @param chatMsgs the new data
     */
    public void setData(List<ChatMsg> chatMsgs) {
        mChatData = chatMsgs;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Gets the text view.
     *
     * @param holder the holder
     * @param item   the item
     */
    private void getTextView(RecyclerView.ViewHolder holder, ChatMsg item) {
        TxtViewHolder txtViewHolder = (TxtViewHolder) holder;
        try {
            txtViewHolder.viewSender.setVisibility(View.GONE);
            txtViewHolder.viewReceiver.setVisibility(View.GONE);
            txtViewHolder.txtRevName.setVisibility(View.GONE);
            setRosterName(txtViewHolder.txtRevName, item);
            String time = mApplication.returnEmptyStringIfNull(item.getMsgTime());
            if (!time.isEmpty())
                time = chatMsgTime.getDaySentMsg(context, Long.parseLong(time));
            String msg = item.getMsgBody();
            if (item.getSender() == Constants.CHAT_FROM_SENDER) {
                txtViewHolder.viewSender.setVisibility(View.VISIBLE);
                txtViewHolder.txtChatSender.setText(msg);
                txtViewHolder.txtChatTime.setText(time);

                String receivers;
                if (chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_SINGLE))) {
                    receivers = toUser;
                } else {
                    receivers = item.getChatReceivers();
                }
                Utils.setGroupChatStatus(txtViewHolder.imgChatStatus, item.getMsgId(), receivers);

                alignTextView(msg.length(), txtViewHolder.viewSender);
            } else {
                txtViewHolder.viewReceiver.setVisibility(View.VISIBLE);
                txtViewHolder.txtChatReceiver.setText(msg);
                txtViewHolder.txtChatRevTime.setText(time);
                alignTextView(msg.length(), txtViewHolder.viewRevTxt);
            }
            setSelected(txtViewHolder.viewRowItem, item);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Align text view.
     *
     * @param length the length
     * @param view   the view
     */
    private void alignTextView(int length, LinearLayout view) {
        if (length > 20)
            view.setOrientation(LinearLayout.VERTICAL);
        else
            view.setOrientation(LinearLayout.HORIZONTAL);
    }

    /**
     * Gets the image view.
     *
     * @param holder   the holder
     * @param item     the item
     * @param position the position
     */
    private void getImageView(RecyclerView.ViewHolder holder, final ChatMsg item, int position) {
        try {
            final ImageViewHolder imgViewHolder = (ImageViewHolder) holder;
            imgViewHolder.viewSender.setVisibility(View.GONE);
            imgViewHolder.viewReceiver.setVisibility(View.GONE);
            imgViewHolder.progressRev.setVisibility(View.GONE);
            imgViewHolder.progressSender.setVisibility(View.GONE);
            imgViewHolder.viewDownload.setVisibility(View.GONE);
            imgViewHolder.imageLayer.setVisibility(View.GONE);
            imgViewHolder.txtRetry.setVisibility(View.GONE);
            imgViewHolder.imgRevPlay.setVisibility(View.GONE);
            imgViewHolder.imgSendPlay.setVisibility(View.GONE);
            imgViewHolder.txtRevName.setVisibility(View.GONE);
            setRosterName(imgViewHolder.txtRevName, item);
            String time = mApplication.returnEmptyStringIfNull(item.getMsgTime());
            if (!time.isEmpty())
                time = chatMsgTime.getDaySentMsg(context, Long.parseLong(time));
            String fileStatus = mApplication.returnEmptyStringIfNull(item.getMsgMediaIsDownloaded());
            String base64Img = mApplication.returnEmptyStringIfNull(item.getMsgMediaEncImage());
            String filePath = mApplication.returnEmptyStringIfNull(item.getMsgMediaLocalPath());
            if (item.getSender() == Constants.CHAT_FROM_SENDER) {
                imgViewHolder.viewSender.setVisibility(View.VISIBLE);
                imgViewHolder.txtSendTime.setText(time);

                ImageLoader.loadImageInView(imgViewHolder.imageSenderImg, filePath, base64Img);

                String receivers;

                if (chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_SINGLE))) {
                    receivers = toUser;
                } else {
                    receivers = item.getChatReceivers();
                }
                Utils.setGroupChatStatus(imgViewHolder.imgSenderStatus, item.getMsgId(), receivers);

                if (!fileStatus.isEmpty())
                    setMediaStatus(imgViewHolder, imgViewHolder.progressSender,
                            Integer.parseInt(fileStatus), item, imgViewHolder.imgSendPlay);
            } else {
                imgViewHolder.viewReceiver.setVisibility(View.VISIBLE);
                imgViewHolder.txtRevTime.setText(time);
                if (!fileStatus.isEmpty())
                    setMediaStatus(imgViewHolder, imgViewHolder.progressRev,
                            Integer.parseInt(fileStatus), item, imgViewHolder.imgSendPlay);

                String fileSize = mApplication.returnEmptyStringIfNull(item.getMsgMediaSize());
                if (!fileSize.isEmpty() && imgViewHolder.viewDownload.getVisibility() == View.VISIBLE) {
                    int size = 0;
                    String txtSize = context.getString(R.string.text_kb);
                    if (!fileSize.equalsIgnoreCase(String.valueOf(Constants.COUNT_ZERO))) {
                        size = Integer.parseInt(fileSize) / Constants.ONE_KB;
                        if (size >= Constants.ONE_KB) {
                            size = size / Constants.ONE_KB;
                            txtSize = context.getString(R.string.text_mb);
                        }
                    }
                    imgViewHolder.txtImgSize.setText(String.valueOf(size + " " + txtSize));
                }
                ImageLoader.loadImageInView(imgViewHolder.imgRevImage, filePath, base64Img);
            }
            imgViewHolder.viewDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onDownloadClicked(item);
                }
            });
            imgViewHolder.txtRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mApplication.isNetConnected(context)) {
                        CustomToast.showToast(context, context.getString(R.string.text_no_internet));
                    } else if (listener != null) {
                        listener.onRetryClicked(item);
                    }
                }
            });
            doOnClick(imgViewHolder.viewSender, true, item, position);
            doOnClick(imgViewHolder.viewReceiver, false, item, position);
            setSelected(imgViewHolder.viewRowItem, item);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Gets the image view.
     *
     * @param holder   the holder
     * @param item     the item
     * @param position the position
     */
    private void getVideoView(RecyclerView.ViewHolder holder, final ChatMsg item, int position) {
        try {
            final VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            videoViewHolder.viewSender.setVisibility(View.GONE);
            videoViewHolder.viewReceiver.setVisibility(View.GONE);
            videoViewHolder.progressRev.setVisibility(View.GONE);
            videoViewHolder.progressSender.setVisibility(View.GONE);
            videoViewHolder.viewDownload.setVisibility(View.GONE);
            videoViewHolder.txtRetry.setVisibility(View.GONE);
            videoViewHolder.videoRevPlay.setVisibility(View.GONE);
            videoViewHolder.videoSendPlay.setVisibility(View.GONE);
            videoViewHolder.txtRevName.setVisibility(View.GONE);
            setRosterName(videoViewHolder.txtRevName, item);
            String time = mApplication.returnEmptyStringIfNull(item.getMsgTime());
            if (!time.isEmpty())
                time = chatMsgTime.getDaySentMsg(context, Long.parseLong(time));
            String fileStatus = mApplication.returnEmptyStringIfNull(item.getMsgMediaIsDownloaded());
            String base64Img = mApplication.returnEmptyStringIfNull(item.getMsgMediaEncImage());
            String filePath = mApplication.returnEmptyStringIfNull(item.getMsgMediaLocalPath());
            if (item.getSender() == Constants.CHAT_FROM_SENDER) {
                videoViewHolder.viewSender.setVisibility(View.VISIBLE);
                videoViewHolder.txtSendTime.setText(time);
                videoViewHolder.videoSenderImg.setImageResource(R.drawable.abc_video_placeholder);
                ImageLoader.loadImageInView(videoViewHolder.videoSenderImg, filePath, base64Img);

                String receivers;
                if (chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_SINGLE))) {
                    receivers = toUser;
                } else {
                    receivers = item.getChatReceivers();
                }
                Utils.setGroupChatStatus(videoViewHolder.videoSenderStatus, item.getMsgId(), receivers);

                if (!fileStatus.isEmpty())
                    setMediaStatus(videoViewHolder, videoViewHolder.progressSender,
                            Integer.parseInt(fileStatus), item, videoViewHolder.videoSendPlay);
            } else {
                videoViewHolder.viewReceiver.setVisibility(View.VISIBLE);
                videoViewHolder.txtRevTime.setText(time);
                if (!fileStatus.isEmpty())
                    setMediaStatus(videoViewHolder, videoViewHolder.progressRev,
                            Integer.parseInt(fileStatus), item, videoViewHolder.videoRevPlay);
                String fileSize = mApplication.returnEmptyStringIfNull(item.getMsgMediaSize());
                if (!fileSize.isEmpty() && videoViewHolder.viewDownload.getVisibility() == View.VISIBLE) {
                    int size = 0;
                    String txtSize = context.getString(R.string.text_kb);
                    if (!fileSize.equalsIgnoreCase(String.valueOf(Constants.COUNT_ZERO))) {
                        size = Integer.parseInt(fileSize) / Constants.ONE_KB;
                        if (size >= Constants.ONE_KB) {
                            size = size / Constants.ONE_KB;
                            txtSize = context.getString(R.string.text_mb);
                        }
                    }
                    videoViewHolder.txtVideoSize.setText(String.valueOf(size + " " + txtSize));
                }
                videoViewHolder.videoRevImage.setImageResource(R.drawable.abc_video_placeholder);
                ImageLoader.loadImageInView(videoViewHolder.videoRevImage, filePath, base64Img);
            }
            videoViewHolder.viewDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onDownloadClicked(item);
                }
            });
            videoViewHolder.txtRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mApplication.isNetConnected(context)) {
                        CustomToast.showToast(context, context.getString(R.string.text_no_internet));
                    } else if (listener != null) {
                        listener.onRetryClicked(item);
                    }
                }
            });
            doOnClick(videoViewHolder.viewSender, true, item, position);
            doOnClick(videoViewHolder.viewReceiver, false, item, position);
            setSelected(videoViewHolder.viewRowItem, item);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Gets the audio view.
     *
     * @param holder   the holder
     * @param item     the item
     * @param position the position
     */
    private void getAudioView(RecyclerView.ViewHolder holder, final ChatMsg item, final int position) {
        try {
            final AudioViewHolder audioViewHolder = (AudioViewHolder) holder;
            audioViewHolder.viewSender.setVisibility(View.GONE);
            audioViewHolder.viewReceiver.setVisibility(View.GONE);
            audioViewHolder.progressRev.setVisibility(View.GONE);
            audioViewHolder.progressSender.setVisibility(View.GONE);
            audioViewHolder.viewDownload.setVisibility(View.GONE);
            audioViewHolder.imgSenderPlay.setVisibility(View.GONE);
            audioViewHolder.imgRevPlay.setVisibility(View.GONE);
            setRosterName(audioViewHolder.txtRevName, item);
            String time = mApplication.returnEmptyStringIfNull(item.getMsgTime());
            if (!time.isEmpty())
                time = chatMsgTime.getDaySentMsg(context, Long.parseLong(time));
            String fileStatus = mApplication.returnEmptyStringIfNull(item.getMsgMediaIsDownloaded());
            final String filePath = mApplication.returnEmptyStringIfNull(item.getMsgMediaLocalPath());
            if (item.getSender() == Constants.CHAT_FROM_SENDER) {
                audioViewHolder.viewSender.setVisibility(View.VISIBLE);
                audioViewHolder.txtSendTime.setText(time);

                String receivers;
                if (chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_SINGLE))) {
                    receivers = toUser;
                } else {
                    receivers = item.getChatReceivers();
                }
                Utils.setGroupChatStatus(audioViewHolder.imgSenderStatus, item.getMsgId(), receivers);

                if (!fileStatus.isEmpty())
                    setAudioStatus(audioViewHolder.viewDownload, audioViewHolder.progressSender,
                            Integer.parseInt(fileStatus), audioViewHolder.imgSenderPlay);
                if (!TextUtils.isEmpty(filePath)) {
                    MediaPlayer player = MediaPlayer.create(context, Uri.fromFile(new File(filePath)));
                    int duration;
                    if (player != null) {
                        duration = player.getDuration() / 1000;
                    } else {
                        duration = 0;
                    }
                    audioViewHolder.txtSendDuration.setText(MediaController.getFormattedTime(duration));
                }

                audioViewHolder.imgSenderPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMediaController.setMediaResource(filePath, audioViewHolder.imgSenderPlay, item.getMsgId());
                        mMediaController.setMediaSeekBar(audioViewHolder.audioSender);
                        mMediaController.setMediaTimer(audioViewHolder.txtSendDuration);
                        mMediaController.setPosition(position);
                        mMediaController.handlePlayer();
                    }
                });
                mMediaController.checkStateOfPlayer(audioViewHolder.imgSenderPlay, audioViewHolder.audioSender,
                        audioViewHolder.txtSendDuration, position);
            } else {
                audioViewHolder.viewReceiver.setVisibility(View.VISIBLE);
                audioViewHolder.txtRevTime.setText(time);
                if (!fileStatus.isEmpty())
                    setAudioStatus(audioViewHolder.viewDownload, audioViewHolder.progressRev,
                            Integer.parseInt(fileStatus), audioViewHolder.imgRevPlay);

                if (!TextUtils.isEmpty(filePath)) {
                    MediaPlayer player = MediaPlayer.create(context, Uri.fromFile(new File(filePath)));
                    int duration;
                    if (player != null) {
                        duration = player.getDuration() / 1000;
                    } else {
                        duration = 0;
                    }
                    audioViewHolder.txtRevDuration.setText(mMediaController.getFormattedTime(duration));
                }
                audioViewHolder.imgRevPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMediaController.setMediaResource(filePath, audioViewHolder.imgRevPlay, item.getMsgId());
                        mMediaController.setMediaSeekBar(audioViewHolder.audioRev);
                        mMediaController.setMediaTimer(audioViewHolder.txtRevDuration);
                        mMediaController.setPosition(position);
                        mMediaController.handlePlayer();
                    }
                });
                mMediaController.checkStateOfPlayer(audioViewHolder.imgRevPlay, audioViewHolder.audioRev,
                        audioViewHolder.txtRevDuration, position);
            }
            if (Integer.parseInt(item.getMsgMediaIsDownloaded()) == Constants.MEDIA_DOWNLADED)
                audioViewHolder.audioRev.setEnabled(true);
            else
                audioViewHolder.audioRev.setEnabled(false);
            audioViewHolder.viewDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onDownloadClicked(item);
                }
            });
            setSelected(audioViewHolder.viewRowItem, item);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Gets the location view.
     *
     * @param holder   the holder
     * @param item     the item
     * @param position the position
     */
    private void getLocationView(RecyclerView.ViewHolder holder, final ChatMsg item, int position) {
        try {
            LocationViewHolder locationHolder = (LocationViewHolder) holder;
            locationHolder.viewSender.setVisibility(View.GONE);
            locationHolder.viewReceiver.setVisibility(View.GONE);
            locationHolder.txtRevName.setVisibility(View.GONE);
            setRosterName(locationHolder.txtRevName, item);
            String locationStr = item.getMsgBody();
            JSONObject jsonObject = new JSONObject(locationStr);
            String latitude = jsonObject.getString("latitude");
            String longitude = jsonObject.getString("longitude");
            String url = "http://maps.google.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=15&size=200x200&sensor=false&markers=icon:https://foursquare.com/img/categories_v2/shops/financial_bg.png|" + latitude + "," + longitude;
            String time = mApplication.returnEmptyStringIfNull(item.getMsgTime());
            if (!time.isEmpty())
                time = chatMsgTime.getDaySentMsg(context, Long.parseLong(time));
            if (item.getSender() == Constants.CHAT_FROM_SENDER) {
                locationHolder.viewSender.setVisibility(View.VISIBLE);
                locationHolder.txtSendTime.setText(time);
                String receivers;
                if (chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_SINGLE))) {
                    receivers = toUser;
                } else {
                    receivers = item.getChatReceivers();
                }
                ImageLoader.loadImageWithGlide(context, url, locationHolder.imageSendLocation, R.drawable.ic_map_placeholder);
                Utils.setGroupChatStatus(locationHolder.imgSenderStatus, item.getMsgId(), receivers);

            } else {
                locationHolder.viewReceiver.setVisibility(View.VISIBLE);
                locationHolder.txtRevTime.setText(time);
                ImageLoader.loadImageWithGlide(context, url, locationHolder.imageReceiveLocation, R.drawable.ic_map_placeholder);
            }
            doOnClick(locationHolder.viewSender, true, item, position);
            doOnClick(locationHolder.viewReceiver, false, item, position);
            setSelected(locationHolder.viewRowItem, item);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Gets the location view.
     *
     * @param holder   the holder
     * @param item     the item
     * @param position the position
     */
    private void getContactView(RecyclerView.ViewHolder holder, final ChatMsg item, int position) {
        try {
            ContactViewHolder contactHolder = (ContactViewHolder) holder;
            contactHolder.viewSender.setVisibility(View.GONE);
            contactHolder.viewReceiver.setVisibility(View.GONE);
            contactHolder.txtRevName.setVisibility(View.GONE);
            setRosterName(contactHolder.txtRevName, item);
            String time = mApplication.returnEmptyStringIfNull(item.getMsgTime());
            if (!time.isEmpty())
                time = chatMsgTime.getDaySentMsg(context, Long.parseLong(time));
            String[] contactData = item.getMsgBody().split(",");
            String contactName = contactData[0].substring(0, 1);
            if (item.getSender() == Constants.CHAT_FROM_SENDER) {
                contactHolder.viewSender.setVisibility(View.VISIBLE);
                contactHolder.txtSendTime.setText(time);
                contactHolder.txtSendName.setText(contactData[0]);
                contactHolder.txtSendImg.setText(contactName);

                String receivers;
                if (chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_SINGLE))) {
                    receivers = toUser;
                } else {
                    receivers = item.getChatReceivers();
                }
                Utils.setGroupChatStatus(contactHolder.imgSenderStatus, item.getMsgId(), receivers);

            } else {
                contactHolder.viewReceiver.setVisibility(View.VISIBLE);
                contactHolder.txtRevTime.setText(time);
                contactHolder.txtRevContact.setText(contactData[0]);
                contactHolder.txtRevImg.setText(contactName);
            }
            doOnClick(contactHolder.viewSender, true, item, position);
            doOnClick(contactHolder.viewReceiver, false, item, position);
            setSelected(contactHolder.viewRowItem, item);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * @param holder
     * @param item
     * @param position
     */
    private void getFileView(RecyclerView.ViewHolder holder, final ChatMsg item, int position) {
        try {
            FileViewHolder fileViewHolder = (FileViewHolder) holder;
            fileViewHolder.viewSender.setVisibility(View.GONE);
            fileViewHolder.viewReceiver.setVisibility(View.GONE);
            fileViewHolder.txtRevName.setVisibility(View.GONE);
            fileViewHolder.progressRev.setVisibility(View.GONE);
            fileViewHolder.progressSend.setVisibility(View.GONE);
            fileViewHolder.imgSendDoc.setVisibility(View.VISIBLE);
            fileViewHolder.imgRevDoc.setVisibility(View.GONE);
            setRosterName(fileViewHolder.txtRevName, item);
            String time = mApplication.returnEmptyStringIfNull(item.getMsgTime());
            if (!time.isEmpty())
                time = chatMsgTime.getDaySentMsg(context, Long.parseLong(time));
            String fileStatus = mApplication.returnEmptyStringIfNull(item.getMsgMediaIsDownloaded());
            if (item.getSender() == Constants.CHAT_FROM_SENDER) {
                fileViewHolder.viewSender.setVisibility(View.VISIBLE);
                fileViewHolder.txtSendTime.setText(time);
                String fileName = Uri.parse(item.getFileName()).getLastPathSegment();
                fileViewHolder.txtSendName.setText(fileName);
                String receivers;
                if (chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_SINGLE))) {
                    receivers = toUser;
                } else {
                    receivers = item.getChatReceivers();
                }
                Utils.setGroupChatStatus(fileViewHolder.imgStatus, item.getMsgId(), receivers);

                if (!fileStatus.isEmpty())
                    setMediaStatus(Integer.parseInt(fileStatus), fileViewHolder.progressSend,
                            fileViewHolder.viewDownload);
                setFileImage(fileViewHolder.imgSendDoc, fileName);
            } else {
                fileViewHolder.viewReceiver.setVisibility(View.VISIBLE);
                fileViewHolder.imgRevDoc.setVisibility(View.VISIBLE);
                fileViewHolder.txtRevTime.setText(time);
                LogMessage.v("Chat::", "::nam::" + item.getFileName());
                String fileName = Uri.parse(item.getFileName()).getLastPathSegment();
                fileViewHolder.txtRevFileName.setText(fileName);
                if (!fileStatus.isEmpty()) {
                    setMediaStatus(Integer.parseInt(fileStatus), fileViewHolder.progressRev,
                            fileViewHolder.viewDownload);

                    setFileImage(fileViewHolder.imgRevDoc, fileName);
                }
            }
            fileViewHolder.viewDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onDownloadClicked(item);
                }
            });

            doOnClick(fileViewHolder.viewSender, true, item, position);
            doOnClick(fileViewHolder.viewReceiver, false, item, position);

            setSelected(fileViewHolder.viewRowItem, item);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }


    /**
     * show staus view for the group activity
     *
     * @param holder
     * @param item
     */
    private void getStatusView(RecyclerView.ViewHolder holder, ChatMsg item) {
        ((StatusViewHolder) holder).textStatus.setText(Utils.getDecodedString(item.getMsgBody()));
    }

    /**
     * Sets the roster name.
     *
     * @param txtView the txt view
     * @param item    the item
     */
    private void setRosterName(TextView txtView, ChatMsg item) {
        if (chatType.equalsIgnoreCase(String.valueOf(Constants.CHAT_TYPE_GROUP))) {
            txtView.setVisibility(View.GONE);
            String toUserName = mApplication.returnEmptyStringIfNull(item.getChatToUser());
            if (!toUserName.isEmpty()) {
                List<Rosters> mRosters = MDatabaseHelper.getRosterInfo(toUserName, Constants.EMPTY_STRING);
                if (!mRosters.isEmpty()) {
                    txtView.setVisibility(View.VISIBLE);
                    txtView.setText(Utils.getDecodedString(mRosters.get(0).getRosterName()));
                }
            }
        }
    }

    /**
     * Sets the media status.
     *
     * @param viewHolder  view holder of the item
     * @param progressBar the progress bar
     * @param status      the status
     * @param item        the item
     * @param imgPlay     the img play
     */
    private void setMediaStatus(ImageViewHolder viewHolder, ProgressBar progressBar, int status, ChatMsg item,
                                ImageView imgPlay) {
        switch (status) {
            case Constants.MEDIA_DOWNLADED:
                viewHolder.sentVideoDur.setVisibility(View.GONE);
                viewHolder.recVideoDur.setVisibility(View.GONE);
                viewHolder.viewDownload.setVisibility(View.GONE);
                if (item.getMsgType().equals(Constants.MSG_TYPE_VIDEO) || item.getMsgType().equals(Constants.MSG_TYPE_AUDIO))
                    loadVideoDuration(viewHolder.recVideoDur, item);
                hideProgressView(progressBar, item, imgPlay);
                break;
            case Constants.MEDIA_UPLOADED:
                if (item.getMsgType().equals(Constants.MSG_TYPE_VIDEO) || item.getMsgType().equals(Constants.MSG_TYPE_AUDIO))
                    loadVideoDuration(viewHolder.sentVideoDur, item);
                hideProgressView(progressBar, item, imgPlay);
                break;
            case Constants.MEDIA_DOWNLADING:
            case Constants.MEDIA_UPLOADING:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case Constants.MEDIA_NOT_DOWNLOADED:
                progressBar.setVisibility(View.GONE);
                viewHolder.viewDownload.setVisibility(View.VISIBLE);
                viewHolder.imageLayer.setVisibility(View.VISIBLE);
                break;
            case Constants.MEDIA_NOT_UPLOADED:
                viewHolder.txtRetry.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * Sets the media status.
     *
     * @param viewHolder  view holder of the item
     * @param progressBar the progress bar
     * @param status      the status
     * @param item        the item
     * @param imgPlay     the img play
     */
    private void setMediaStatus(VideoViewHolder viewHolder, ProgressBar progressBar, int status, ChatMsg item,
                                ImageView imgPlay) {
        switch (status) {
            case Constants.MEDIA_DOWNLADED:
                viewHolder.sentVideoDur.setVisibility(View.GONE);
                viewHolder.viewDownload.setVisibility(View.GONE);
                viewHolder.recVideoDur.setVisibility(View.GONE);
                if (item.getMsgType().equals(Constants.MSG_TYPE_VIDEO) || item.getMsgType().equals(Constants.MSG_TYPE_AUDIO))
                    loadVideoDuration(viewHolder.recVideoDur, item);
                hideProgressView(progressBar, item, imgPlay);
                break;
            case Constants.MEDIA_UPLOADED:
                if (item.getMsgType().equals(Constants.MSG_TYPE_VIDEO) || item.getMsgType().equals(Constants.MSG_TYPE_AUDIO))
                    loadVideoDuration(viewHolder.sentVideoDur, item);
                hideProgressView(progressBar, item, imgPlay);
                break;
            case Constants.MEDIA_DOWNLADING:
            case Constants.MEDIA_UPLOADING:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case Constants.MEDIA_NOT_DOWNLOADED:
                progressBar.setVisibility(View.GONE);
                viewHolder.viewDownload.setVisibility(View.VISIBLE);
                break;
            case Constants.MEDIA_NOT_UPLOADED:
                viewHolder.txtRetry.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void loadVideoDuration(TextView tvVideoDuration, ChatMsg item) {
        tvVideoDuration.setVisibility(View.VISIBLE);

        String filePath = mApplication.returnEmptyStringIfNull(item.getMsgMediaLocalPath());
        if (!TextUtils.isEmpty(filePath)) {
            MediaPlayer player = MediaPlayer.create(context, Uri.fromFile(new File(filePath)));
            int duration;
            if (player != null) {
                duration = player.getDuration() / 1000;
            } else {
                duration = 0;
            }
            tvVideoDuration.setText(MediaController.getFormattedTime(duration));
        }
    }

    private void hideProgressView(ProgressBar progressBar, ChatMsg item, ImageView imgPlay) {
        progressBar.setVisibility(View.GONE);
        if (item.getMsgType().equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)) {
            imgPlay.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets the media status.
     *
     * @param progressBar the progress bar
     * @param status      the status
     */
    private void setMediaStatus(int status, ProgressBar progressBar, ImageView imageView) {
        switch (status) {
            case Constants.MEDIA_DOWNLADED:
            case Constants.MEDIA_UPLOADED:
                imageView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                break;
            case Constants.MEDIA_DOWNLADING:
            case Constants.MEDIA_UPLOADING:
                imageView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case Constants.MEDIA_NOT_DOWNLOADED:
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_download);
                break;
            case Constants.MEDIA_NOT_UPLOADED:
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_download);
                imageView.setRotation(180);
                progressBar.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void setFileImage(ImageView fileViewHolder, String fileName) {

        if (fileName.contains(".pdf")) {
            fileViewHolder.setImageResource(R.drawable.ic_pdf);
        } else if (fileName.contains(".doc")) {
            fileViewHolder.setImageResource(R.drawable.ic_doc);
        } else if (fileName.contains(".docx")) {
            fileViewHolder.setImageResource(R.drawable.ic_docx);
        } else if (fileName.contains(".txt")) {
            fileViewHolder.setImageResource(R.drawable.ic_txt);
        } else if (fileName.contains(".xls")) {
            fileViewHolder.setImageResource(R.drawable.ic_xls);
        } else if (fileName.contains(".ppt")) {
            fileViewHolder.setImageResource(R.drawable.ic_ppt);
        } else if (fileName.contains(".zip")) {
            fileViewHolder.setImageResource(R.drawable.ic_zip);
        } else if (fileName.contains(".rar")) {
            fileViewHolder.setImageResource(R.drawable.ic_rar);
        }
    }

    /**
     * Sets the audio status.
     *
     * @param download    the download
     * @param progressBar the progress bar
     * @param status      the status
     * @param imgPlay     the img play
     */
    private void setAudioStatus(ImageView download, ProgressBar progressBar, int status, ImageView imgPlay) {
        switch (status) {
            case Constants.MEDIA_DOWNLADED:
            case Constants.MEDIA_UPLOADED:
                progressBar.setVisibility(View.GONE);
                download.setVisibility(View.GONE);
                imgPlay.setVisibility(View.VISIBLE);
                break;
            case Constants.MEDIA_DOWNLADING:
            case Constants.MEDIA_UPLOADING:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case Constants.MEDIA_NOT_DOWNLOADED:
                progressBar.setVisibility(View.GONE);
                download.setVisibility(View.VISIBLE);
                break;
            case Constants.MEDIA_NOT_UPLOADED:
                progressBar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * Stop player.
     */
    public void stopPlayer() {
        if (mMediaController != null)
            mMediaController.stopPlayer();
    }

    /**
     * Do on click.
     *
     * @param view     the view
     * @param isSender the is sender
     * @param item     the item
     * @param position the position
     */
    public void doOnClick(View view, final boolean isSender, final ChatMsg item, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (isSender)
                        listener.onSenderItemClicked(item);
                    else
                        listener.onReceiverItemClicked(item);
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    if (isSender)
                        listener.onSenderItemLongClick(item, position);
                    else
                        listener.onReceiverItemLongClick(item, position);
                }
                return true;
            }
        });
    }

    /**
     * Sets the selected.
     *
     * @param view the view
     * @param item the item
     */
    private void setSelected(View view, ChatMsg item) {
        if (item.getMsgId().equals(lastSelectedMessageId))
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.color_transparent_bg));
        else
            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

    /**
     * The listener interface for receiving onChatItemClick events. The class
     * that is interested in processing a onChatItemClick event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's <code>addOnChatItemClickListener
     * <code> method. When the onChatItemClick event occurs, that object's
     * appropriate method is invoked.
     */
    public interface OnChatItemClickListener {

        /**
         * On download clicked.
         *
         * @param item the item
         */
        void onDownloadClicked(ChatMsg item);

        /**
         * On retry clicked.
         *
         * @param item the item
         */
        void onRetryClicked(ChatMsg item);

        /**
         * On sender item clicked.
         *
         * @param item the item
         */
        void onSenderItemClicked(ChatMsg item);

        /**
         * On receiver item clicked.
         *
         * @param item the item
         */
        void onReceiverItemClicked(ChatMsg item);

        /**
         * On sender item long click.
         *
         * @param item     the item
         * @param position the position
         */
        void onSenderItemLongClick(ChatMsg item, int position);

        /**
         * On receiver item long click.
         *
         * @param item     the item
         * @param position the position
         */
        void onReceiverItemLongClick(ChatMsg item, int position);
    }

    /**
     * The Class TxtViewHolder.
     */
    public class TxtViewHolder extends RecyclerView.ViewHolder {

        /**
         * The view rev txt.
         */
        LinearLayout viewSender, /**
         * The View receiver.
         */
        viewReceiver, /**
         * The View rev txt.
         */
        viewRevTxt, /**
         * The View row item.
         */
        viewRowItem;
        /**
         * The txt chat receiver.
         */
        CustomTextView txtChatSender, /**
         * The Txt chat receiver.
         */
        txtChatReceiver;
        /**
         * The txt rev name.
         */
        TextView txtChatTime, /**
         * The Txt chat rev time.
         */
        txtChatRevTime, /**
         * The Txt rev name.
         */
        txtRevName;
        /**
         * The img chat status.
         */
        ImageView imgChatStatus;

        /**
         * Instantiates a new txt view holder.
         *
         * @param view the view
         */
        public TxtViewHolder(View view) {
            super(view);
            viewSender = (LinearLayout) view.findViewById(R.id.view_chat_sender_txt);
            viewReceiver = (LinearLayout) view.findViewById(R.id.view_chat_rev_txt);
            viewRevTxt = (LinearLayout) view.findViewById(R.id.view_chat_rev);
            txtChatSender = (CustomTextView) view.findViewById(R.id.txt_send_chat);
            txtChatReceiver = (CustomTextView) view.findViewById(R.id.txt_rev_chat);
            txtRevName = (TextView) view.findViewById(R.id.txt_rev_txt_name);
            txtChatTime = (TextView) view.findViewById(R.id.txt_send_txt_time);
            imgChatStatus = (ImageView) view.findViewById(R.id.img_txt_status);
            txtChatRevTime = (TextView) view.findViewById(R.id.txt_rev_txt_time);
            viewRowItem = (LinearLayout) view.findViewById(R.id.row_chat_text);
        }
    }

    /**
     * The Class ImageViewHolder.
     */
    public class ImageViewHolder extends RecyclerView.ViewHolder {

        /**
         * To load video duration of sent item
         */
        TextView sentVideoDur;
        /**
         * To load video duration of receive item
         */
        TextView recVideoDur;
        /**
         * The view rev img.
         */
        RelativeLayout viewSender, /**
         * The View receiver.
         */
        viewReceiver, /**
         * The View rev img.
         */
        viewRevImg;
        /**
         * The txt rev name.
         */
        TextView txtSendTime, /**
         * The Txt rev time.
         */
        txtRevTime, /**
         * The Txt img size.
         */
        txtImgSize, /**
         * The Txt retry.
         */
        txtRetry, /**
         * The Txt rev name.
         */
        txtRevName;
        /**
         * The img send play.
         */
        ImageView imgRevImage, /**
         * The Image sender img.
         */
        imageSenderImg, /**
         * The Img sender status.
         */
        imgSenderStatus, /**
         * The Img rev play.
         */
        imgRevPlay, /**
         * The Img send play.
         */
        imgSendPlay;
        /**
         * The progress sender.
         */
        ProgressBar progressRev, /**
         * The Progress sender.
         */
        progressSender;
        /**
         * The view download.
         */
        LinearLayout viewDownload, /**
         * The View row item.
         */
        viewRowItem;
        View imageLayer;

        /**
         * Instantiates a new image view holder.
         *
         * @param view the view
         */
        public ImageViewHolder(View view) {
            super(view);
            viewSender = (RelativeLayout) view.findViewById(R.id.view_chat_send_img);
            viewReceiver = (RelativeLayout) view.findViewById(R.id.view_chat_rev_img);
            viewRevImg = (RelativeLayout) view.findViewById(R.id.view_rev_img);
            txtRevTime = (TextView) view.findViewById(R.id.img_rev_time);
            txtRevName = (TextView) view.findViewById(R.id.txt_rev_img_name);
            txtSendTime = (TextView) view.findViewById(R.id.txt_send_img_time);
            txtImgSize = (TextView) view.findViewById(R.id.txt_file_size);
            txtRetry = (TextView) view.findViewById(R.id.txt_retry);
            imgRevImage = (ImageView) view.findViewById(R.id.img_rev_imgitem);
            imageSenderImg = (ImageView) view.findViewById(R.id.img_send_imgitem);
            imgSenderStatus = (ImageView) view.findViewById(R.id.img_send_status);
            progressRev = (ProgressBar) view.findViewById(R.id.img_rev_progress);
            progressSender = (ProgressBar) view.findViewById(R.id.img_send_progress);
            viewDownload = (LinearLayout) view.findViewById(R.id.img_rev_download);
            imgRevPlay = (ImageView) view.findViewById(R.id.img_rev_play);
            imgSendPlay = (ImageView) view.findViewById(R.id.img_send_play);
            viewRowItem = (LinearLayout) view.findViewById(R.id.row_chat_image);
            sentVideoDur = (TextView) view.findViewById(R.id.sent_video_dur);
            recVideoDur = (TextView) view.findViewById(R.id.rec_video_dur);
            imageLayer = view.findViewById(R.id.image_layer);
        }
    }

    /**
     * The Class VideoViewHolder.
     */
    public class VideoViewHolder extends RecyclerView.ViewHolder {

        /**
         * To load video duration of sent item
         */
        TextView sentVideoDur;
        /**
         * To load video duration of receive item
         */
        TextView recVideoDur;
        /**
         * The view rev img.
         */
        RelativeLayout viewSender, /**
         * The View receiver.
         */
        viewReceiver, /**
         * The View rev video.
         */
        viewRevImg;
        /**
         * The txt rev name.
         */
        TextView txtSendTime, /**
         * The Txt rev time.
         */
        txtRevTime, /**
         * The Txt video size.
         */
        txtVideoSize, /**
         * The Txt retry.
         */
        txtRetry, /**
         * The Txt rev name.
         */
        txtRevName;
        /**
         * The video send play.
         */
        ImageView videoRevImage, /**
         * The video sender img.
         */
        videoSenderImg, /**
         * The video sender status.
         */
        videoSenderStatus, /**
         * The video rev play.
         */
        videoRevPlay, /**
         * The video send play.
         */
        videoSendPlay;
        /**
         * The progress sender.
         */
        ProgressBar progressRev, /**
         * The Progress sender.
         */
        progressSender;
        /**
         * The view download.
         */
        LinearLayout viewDownload, /**
         * The View row item.
         */
        viewRowItem;

        /**
         * Instantiates a new video view holder.
         *
         * @param view the view
         */
        public VideoViewHolder(View view) {
            super(view);
            viewSender = (RelativeLayout) view.findViewById(R.id.view_chat_send_video);
            viewReceiver = (RelativeLayout) view.findViewById(R.id.view_chat_rev_video);
            viewRevImg = (RelativeLayout) view.findViewById(R.id.view_rev_video);
            txtRevTime = (TextView) view.findViewById(R.id.video_rev_time);
            txtRevName = (TextView) view.findViewById(R.id.txt_rev_video_name);
            txtSendTime = (TextView) view.findViewById(R.id.txt_send_video_time);
            txtVideoSize = (TextView) view.findViewById(R.id.txt_file_size);
            txtRetry = (TextView) view.findViewById(R.id.txt_retry);
            videoRevImage = (ImageView) view.findViewById(R.id.img_rev_videoitem);
            videoSenderImg = (ImageView) view.findViewById(R.id.img_send_videoitem);
            videoSenderStatus = (ImageView) view.findViewById(R.id.video_send_status);
            progressRev = (ProgressBar) view.findViewById(R.id.video_rev_progress);
            progressSender = (ProgressBar) view.findViewById(R.id.video_send_progress);
            viewDownload = (LinearLayout) view.findViewById(R.id.video_rev_download);
            videoRevPlay = (ImageView) view.findViewById(R.id.video_rev_play);
            videoSendPlay = (ImageView) view.findViewById(R.id.video_send_play);
            viewRowItem = (LinearLayout) view.findViewById(R.id.row_chat_video);
            sentVideoDur = (TextView) view.findViewById(R.id.sent_video_dur);
            recVideoDur = (TextView) view.findViewById(R.id.rec_video_dur);
        }
    }

    /**
     * The Class AudioViewHolder.
     */
    private class AudioViewHolder extends RecyclerView.ViewHolder {

        /**
         * The view rev bg.
         */
        LinearLayout viewSender, /**
         * The View receiver.
         */
        viewReceiver, /**
         * The View rev bg.
         */
        viewRevBg, /**
         * The View row item.
         */
        viewRowItem;
        /**
         * The txt rev duration.
         */
        TextView txtSendTime, /**
         * The Txt rev time.
         */
        txtRevTime, /**
         * The Txt rev name.
         */
        txtRevName, /**
         * The Txt send duration.
         */
        txtSendDuration, /**
         * The Txt rev duration.
         */
        txtRevDuration;
        /**
         * The progress rev.
         */
        ProgressBar progressSender, /**
         * The Progress rev.
         */
        progressRev;
        /**
         * The img rev play.
         */
        ImageView viewDownload, /**
         * The Img sender status.
         */
        imgSenderStatus, /**
         * The Img sender play.
         */
        imgSenderPlay, /**
         * The Img rev play.
         */
        imgRevPlay;
        /**
         * The audio rev.
         */
        SeekBar audioSender, /**
         * The Audio rev.
         */
        audioRev;

        /**
         * Instantiates a new audio view holder.
         *
         * @param view the view
         */
        public AudioViewHolder(View view) {
            super(view);
            viewSender = (LinearLayout) view.findViewById(R.id.view_chat_send_audio);
            viewReceiver = (LinearLayout) view.findViewById(R.id.view_chat_rev_audio);
            viewRevBg = (LinearLayout) view.findViewById(R.id.view_rev_audio);
            txtRevTime = (TextView) view.findViewById(R.id.audio_rev_time);
            txtRevName = (TextView) view.findViewById(R.id.txt_rev_audio_name);
            txtSendTime = (TextView) view.findViewById(R.id.audio_send_time);
            txtSendDuration = (TextView) view.findViewById(R.id.audio_send_duration);
            txtRevDuration = (TextView) view.findViewById(R.id.audio_rev_duration);
            viewDownload = (ImageView) view.findViewById(R.id.view_download);
            imgSenderPlay = (ImageView) view.findViewById(R.id.audio_sender_play);
            imgRevPlay = (ImageView) view.findViewById(R.id.audio_rev_play);
            imgSenderStatus = (ImageView) view.findViewById(R.id.img_send_status);
            progressRev = (ProgressBar) view.findViewById(R.id.audio_rev_progress);
            progressSender = (ProgressBar) view.findViewById(R.id.audio_send_progress);
            audioSender = (SeekBar) view.findViewById(R.id.sender_seek_bar);
            audioRev = (SeekBar) view.findViewById(R.id.rev_seek_bar);
            viewRowItem = (LinearLayout) view.findViewById(R.id.row_chat_audio);
        }
    }

    /**
     * The Class LocationViewHolder.
     */
    private class LocationViewHolder extends RecyclerView.ViewHolder {
        /**
         * Image view to showw the receiver and sender
         */
        ImageView imageSendLocation, imageReceiveLocation;
        /**
         * The view receiver.
         */
        LinearLayout viewSender, viewReceiver, viewRowItem;
        /**
         * The txt rev name.
         */
        TextView txtSendTime, txtRevTime, txtRevName;
        /**
         * The img sender status.
         */
        ImageView imgSenderStatus;

        /**
         * Instantiates a new location view holder.
         *
         * @param view the view
         */
        public LocationViewHolder(View view) {
            super(view);
            viewSender = (LinearLayout) view.findViewById(R.id.view_chat_send_loc);
            viewReceiver = (LinearLayout) view.findViewById(R.id.view_chat_rev_loc);
            txtRevTime = (TextView) view.findViewById(R.id.text_loc_rev_time);
            txtRevName = (TextView) view.findViewById(R.id.text_rev_loc_name);
            txtSendTime = (TextView) view.findViewById(R.id.text_send_loc_time);
            imgSenderStatus = (ImageView) view.findViewById(R.id.image_loc_send_status);
            viewRowItem = (LinearLayout) view.findViewById(R.id.row_chat_location);
            imageSendLocation = (ImageView) view.findViewById(R.id.image_location_send);
            imageReceiveLocation = (ImageView) view.findViewById(R.id.image_loc_rev);
        }
    }

    /**
     * The Class LocationViewHolder.
     */
    private class ContactViewHolder extends RecyclerView.ViewHolder {

        /**
         * The view receiver.
         */
        LinearLayout viewSender, /**
         * The View receiver.
         */
        viewReceiver, /**
         * The View row item.
         */
        viewRowItem;
        /**
         * The txt rev name.
         */
        TextView txtSendTime, /**
         * The Txt rev time.
         */
        txtRevTime, /**
         * The Txt send name.
         */
        txtSendName, /**
         * The Txt rev contact.
         */
        txtRevContact, /**
         * The Txt rev name.
         */
        txtRevName, /**
         * The Txt rev img.
         */
        txtRevImg, /**
         * The Txt send img.
         */
        txtSendImg;
        /**
         * The img sender status.
         */
        ImageView imgSenderStatus;

        /**
         * Instantiates a new contact view holder.
         *
         * @param view the view
         */
        public ContactViewHolder(View view) {
            super(view);
            viewSender = (LinearLayout) view.findViewById(R.id.view_send_contact);
            viewReceiver = (LinearLayout) view.findViewById(R.id.view_rev_contact);
            txtRevTime = (TextView) view.findViewById(R.id.txt_contact_rev_time);
            txtRevName = (TextView) view.findViewById(R.id.txt_contact_rev_name);
            txtRevContact = (TextView) view.findViewById(R.id.txt_contact_name);
            txtSendName = (TextView) view.findViewById(R.id.txt_contact_sendname);
            txtRevImg = (TextView) view.findViewById(R.id.txt_avator_rev_img);
            txtSendImg = (TextView) view.findViewById(R.id.txt_avator_send_img);
            txtSendTime = (TextView) view.findViewById(R.id.txt_contact_send_time);
            imgSenderStatus = (ImageView) view.findViewById(R.id.img_contact_status);
            viewRowItem = (LinearLayout) view.findViewById(R.id.row_chat_contact);
        }
    }

    /**
     * The Class LocationViewHolder.
     */
    private class FileViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Progress send.
         */
        ProgressBar progressSend, /**
         * The Progress rev.
         */
        progressRev;
        /**
         * The view receiver.
         */
        LinearLayout viewSender, /**
         * The View receiver.
         */
        viewReceiver, /**
         * The View row item.
         */
        viewRowItem;
        /**
         * The txt rev name.
         */
        TextView txtSendTime, /**
         * The Txt rev time.
         */
        txtRevTime, /**
         * The Txt send name.
         */
        txtSendName, /**
         * The Txt rev file name.
         */
        txtRevFileName, /**
         * The Txt rev name.
         */
        txtRevName;
        /**
         * The img sender status.
         */
        ImageView imgSendDoc, /**
         * The Img rev doc.
         */
        imgRevDoc, /**
         * The Img status.
         */
        imgStatus, /**
         * The View download.
         */
        viewDownload;

        /**
         * Instantiates a new contact view holder.
         *
         * @param view the view
         */
        public FileViewHolder(View view) {
            super(view);
            viewSender = (LinearLayout) view.findViewById(R.id.view_send_file_item);
            viewReceiver = (LinearLayout) view.findViewById(R.id.view_rev_file_item);
            txtRevTime = (TextView) view.findViewById(R.id.txt_rev_file_time);
            txtRevName = (TextView) view.findViewById(R.id.txt_rev_contact_name);
            txtRevFileName = (TextView) view.findViewById(R.id.txt_rev_file_name);
            txtSendName = (TextView) view.findViewById(R.id.txt_send_file_name);
            txtSendTime = (TextView) view.findViewById(R.id.txt_send_file_time);
            imgStatus = (ImageView) view.findViewById(R.id.file_send_status);
            imgSendDoc = (ImageView) view.findViewById(R.id.img_send_doc);
            imgRevDoc = (ImageView) view.findViewById(R.id.img_rev_doc);
            viewDownload = (ImageView) view.findViewById(R.id.view_download);

            progressSend = (ProgressBar) view.findViewById(R.id.file_send_progress);
            progressRev = (ProgressBar) view.findViewById(R.id.file_rev_progress);
            viewRowItem = (LinearLayout) view.findViewById(R.id.row_chat_file);
        }
    }

    /**
     * Showing status view for group activity
     */
    private class StatusViewHolder extends RecyclerView.ViewHolder {
        /**
         * Textview for status
         */
        private CustomTextView textStatus;

        public StatusViewHolder(View view) {
            super(view);

            textStatus = (CustomTextView) view.findViewById(R.id.text_chat_status);
        }
    }
}
