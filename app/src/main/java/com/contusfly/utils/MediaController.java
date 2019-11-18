/**
 * @category ContusMessanger
 * @package com.contusfly.utils
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.polls.polls.R;

import java.io.File;

/**
 * The Class MediaController.
 */
public class MediaController {

    /**
     * The media player.
     */
    private MediaPlayer mediaPlayer;
    /**
     * The context.
     */
    private Context context;
    /**
     * The file path.
     */
    private String lastUsedMedia = Constants.EMPTY_STRING;
    /**
     * The file path.
     */
    private String lastUsedMsgId = Constants.EMPTY_STRING;
    /**
     * The File path.
     */
    private String filePath;
    /**
     * The messageId
     */
    private String messageId;
    /**
     * The img play.
     */
    private ImageView imgLastUsed;
    /**
     * The Img play.
     */
    private ImageView imgPlay;
    /**
     * The seek bar.
     */
    private SeekBar seekBar;
    /**
     * The updated progress.
     */
    private int currentPosition;
    /**
     * The Time consumed.
     */
    private int timeConsumed;
    /**
     * The Updated progress.
     */
    private int updatedProgress;
    /**
     * The m handler.
     */
    private Handler mHandler;

    /**
     * The txt timer.
     */
    private TextView txtTimer;

    /**
     * The position.
     */
    private int position;
    /**
     * The song handler.
     */
    private Runnable songHandler = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(timeConsumed);
            txtTimer.setText(getFormattedTime());
            timeConsumed++;
            mHandler.postDelayed(songHandler, Constants.ONE_SECOND);
        }
    };
    /**
     * The listener.
     */
    private SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            updatedProgress = i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.e("seekbar", seekBar.toString());
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            timeConsumed = updatedProgress;
            if (mediaPlayer != null)
                mediaPlayer.seekTo((int) (timeConsumed * Constants.ONE_SECOND));
            seekBar.setProgress(timeConsumed);
        }
    };

    /**
     * Instantiates a new media controller.
     *
     * @param context the context
     */
    public MediaController(Context context) {
        this.context = context;
    }

    public static String getFormattedTime(int time) {
        String formattedTime;
        if (time < 60) {
            if (time < 10)
                formattedTime = "00:0" + time;
            else
                formattedTime = "00:" + time;
        } else {
            int sec = time % 60;
            int min = time / 60;
            if (min < 10 && sec < 10)
                formattedTime = "0" + min + ":0" + sec;
            else if (min < 10 && sec >= 10)
                formattedTime = "0" + min + ":" + sec;
            else if (min >= 10 && sec < 10)
                formattedTime = min + ":0" + sec;
            else
                formattedTime = min + ":" + sec;
        }
        return formattedTime;

    }

    /**
     * Sets the media resource.
     *  @param filePath  the file path
     * @param imgPlayer the img player
     * @param msgId message id
     */
    public void setMediaResource(String filePath, ImageView imgPlayer, String msgId) {
        this.filePath = filePath;
        this.imgPlay = imgPlayer;
        this.messageId = msgId;
    }

    /**
     * Sets the media seek bar.
     *
     * @param seekBar the new media seek bar
     */
    public void setMediaSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    /**
     * Sets the media timer.
     *
     * @param txtTimer the new media timer
     */
    public void setMediaTimer(TextView txtTimer) {
        this.txtTimer = txtTimer;
    }

    /**
     * Sets the position.
     *
     * @param position the new position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Handle player.
     */
    public void handlePlayer() {
        try {
            if (!filePath.isEmpty()) {
                File file = new File(filePath);
                if (file.exists()) {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying() && lastUsedMedia.equalsIgnoreCase(filePath)&& messageId.equalsIgnoreCase(lastUsedMsgId)) {
                            currentPosition = mediaPlayer.getCurrentPosition();
                            mediaPlayer.pause();
                            imgPlay.setImageResource(R.drawable.audio_play_btn);
                            mHandler.removeCallbacks(songHandler);
                        } else if (!mediaPlayer.isPlaying() && lastUsedMedia.equalsIgnoreCase(filePath)) {
                            mHandler.post(songHandler);
                            mediaPlayer.start();
                            mediaPlayer.seekTo(currentPosition);
                            imgPlay.setImageResource(R.drawable.audio_pause_btn);
                        } else {
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            timeConsumed = 0;
                        }
                    }
                    if (mediaPlayer == null) {
                        mediaPlayer = MediaPlayer.create(context, Uri.parse(filePath));
                        mHandler = new Handler();
                    }
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            lastUsedMedia = filePath;
                            lastUsedMsgId=messageId;
                            if (imgLastUsed != null)
                                imgLastUsed.setImageResource(R.drawable.ic_play);
                            mHandler.post(songHandler);
                            imgPlay.setImageResource(R.drawable.audio_pause_btn);
                            mp.start();
                            seekBar.setMax((int) (mediaPlayer.getDuration() / Constants.ONE_SECOND));
                            imgLastUsed = imgPlay;
                            timeConsumed = 0;
                            seekBar.setOnSeekBarChangeListener(listener);
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            lastUsedMedia = Constants.EMPTY_STRING;
                            timeConsumed = mediaPlayer.getDuration() / 1000;
                            mp.release();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            imgLastUsed = null;
                            imgPlay.setImageResource(R.drawable.ic_play);
                            seekBar.setProgress(0);
                            mHandler.removeCallbacks(songHandler);
                            txtTimer.setText(getFormattedTime());
                        }
                    });
                }
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Check state of player.
     *
     * @param imgPlay  the img play
     * @param seekBar  the seek bar
     * @param txtTimer the txt timer
     * @param position the position
     */
    public void checkStateOfPlayer(ImageView imgPlay, SeekBar seekBar, TextView txtTimer, int position) {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                if (position == this.position) {
                    this.imgPlay = imgPlay;
                    this.seekBar = seekBar;
                    this.txtTimer = txtTimer;
                    imgPlay.setImageResource(R.drawable.audio_pause_btn);
                    seekBar.setMax((int) (mediaPlayer.getDuration() / Constants.ONE_SECOND));
                    seekBar.setOnSeekBarChangeListener(listener);
                    if (mHandler != null && songHandler != null) {
                        mHandler.removeCallbacks(songHandler);
                        mHandler.post(songHandler);
                    }
                }
            }
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Stop player.
     */
    public void stopPlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (mHandler != null && songHandler != null)
                mHandler.removeCallbacks(songHandler);
        } catch (Exception e) {
            LogMessage.e(e);
        }
    }

    /**
     * Gets the formatted time.
     *
     * @return the formatted time
     */
    private String getFormattedTime() {
        return getFormattedTime(timeConsumed);
    }
}
