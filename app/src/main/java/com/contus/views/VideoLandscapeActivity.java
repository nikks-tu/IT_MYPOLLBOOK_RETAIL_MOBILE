package com.contus.views;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.polls.polls.R;

/**
 * Created by user on 9/14/2015.
 */
public class VideoLandscapeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
   //video url
    private String videoUrl;
    //video id
    private String videoId;
    //Interface definition for callbacks that are invoked when video playback events occur.
    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
            //Called when buffering starts or ends.
        }
        @Override
        public void onPaused() {
            //Called when playback is paused, either due to pause() or user action.
        }
        @Override
        public void onPlaying() {
            //Called when playback starts, either due to play() or user action.
        }
        @Override
        public void onSeekTo(int arg0) {
            //Called when a jump in playback position occurs, either due to the user scrubbing or a seek method being called
        }
        @Override
        public void onStopped() {
            //Called when playback stops for a reason other than being paused, such as the video ending or a playback error.
        }
    };
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
           //Called when playback of an advertisement starts.
        }
        @Override
        public void onLoaded(String arg0) {
            //Called when a video has finished loading.
        }
        @Override
        public void onLoading() {
            //Called when the player begins loading a video and is not ready to accept     // commands affecting playback (such as play() or pause()).
        }
        @Override
        public void onVideoEnded() {
            //Called when the video reaches its end.
        }
        @Override
        public void onVideoStarted() {
            //Called when playback of the video starts.
        }
        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
            //Error
            }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        /**View are creating when the activity is started**/
        initUI();

    }
    /**
     * Instantiate the method
     */
    private void initUI() {
        //A view for displaying YouTube videos. Using this View directly is an alternative to using the YouTubePlayerFragment.
        // If you choose to use this view directly, your activity needs to extend YouTubeBaseActivity.
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        //Initialize a YouTubePlayer, which can be used to play videos and control video playback.
        youTubePlayerView.initialize(Constants.YT_KEY, VideoLandscapeActivity.this);
        //Get the url from the prefernce
        videoUrl = MApplication.getString(VideoLandscapeActivity.this, Constants.YOUTUBE_URL);
        //extract the video id
        videoId = MApplication.extractYTId(videoUrl);
        //Setting the status bar color for version more than l;ollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Setting the status bar color
            MApplication.settingStatusBarColor(VideoLandscapeActivity.this, getResources().getColor(android.R.color.black));
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        //Registers a YouTubePlayer.PlayerStateChangeListener.
        player.setPlayerStateChangeListener(playerStateChangeListener);
        //Registers a YouTubePlayer.PlaybackEventListener.
        player.setPlaybackEventListener(playbackEventListener);
        //setPlaybackEventListener
        player.setPlaybackEventListener(playbackEventListener);
        /** Start buffering **/
        if (!wasRestored) {
            //Loads the specified video's thumbnail and prepares the player to play the video,
            // but does not download any of the video stream until play() is called.
            player.cueVideo(videoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        //The reason for this failure, along with potential resolutions to this failure.
        if (youTubeInitializationResult == YouTubeInitializationResult.DEVELOPER_KEY_INVALID)
            Toast.makeText(this, "Initialization Fail- key invalid", Toast.LENGTH_LONG).show();
        else if (youTubeInitializationResult == YouTubeInitializationResult.SERVICE_INVALID)
            Toast.makeText(this, "Initialization Fail- Service invalid", Toast.LENGTH_LONG).show();
        else if (youTubeInitializationResult == YouTubeInitializationResult.INVALID_APPLICATION_SIGNATURE)
            Toast.makeText(this, "Initialization Fail- invalid application", Toast.LENGTH_LONG).show();
    }
}
