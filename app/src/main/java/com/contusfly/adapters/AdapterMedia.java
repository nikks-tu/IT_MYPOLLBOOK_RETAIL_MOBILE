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
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.contusfly.model.MediaMetaData;
import com.contusfly.utils.Constants;
import com.contusfly.utils.ImageLoader;
import com.polls.polls.R;

import java.util.List;


/**
 * The Class AdapterMedia.
 */
public class AdapterMedia extends RecyclerView.Adapter<AdapterMedia.ViewHolder> {

    /**
     * The media list.
     */
    private List<MediaMetaData> mediaList;

    /**
     * The context.
     */
    private Context context;

    /**
     * Instantiates a new adapter media.
     *
     * @param context the context
     */
    public AdapterMedia(Context context) {
        this.context = context;
    }

    /**
     * Sets the data.
     *
     * @param mediaList the new data
     */
    public void setData(List<MediaMetaData> mediaList) {
        this.mediaList = mediaList;
    }

    @Override
    public AdapterMedia.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolder(inflater.inflate(R.layout.row_media_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AdapterMedia.ViewHolder holder, final int position) {
        MediaMetaData item = mediaList.get(position);
        String mediaType = item.getMediaType();
        loadImageInView(holder.imgMedia, item);
        if (mediaType.equalsIgnoreCase(Constants.MSG_TYPE_VIDEO)
                || mediaType.equalsIgnoreCase(Constants.MSG_TYPE_AUDIO))
            holder.imgPlay.setVisibility(View.VISIBLE);
        else
            holder.imgPlay.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    /**
     * Load image in view.
     *
     * @param imageView the image view
     * @param item      the item
     */
    private void loadImageInView(ImageView imageView, MediaMetaData item) {
        int placeHolder = R.drawable.placeholder_image;
        if (item.getMediaType().equalsIgnoreCase(Constants.MSG_TYPE_AUDIO))
            placeHolder = R.drawable.abc_audio_placeholder;
        else if (item.getMediaType().equalsIgnoreCase(Constants.MSG_TYPE_VIDEO))
            placeHolder = R.drawable.abc_video_placeholder;
        ImageLoader.loadImageInView(imageView, item.getMediaPath(), item.getMediaThumb(), placeHolder);
    }

    /**
     * The Class ViewHolder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The img play.
         */
        ImageView imgMedia, /**
         * The Img play.
         */
        imgPlay;

        /**
         * Instantiates a new view holder.
         *
         * @param view the view
         */
        public ViewHolder(View view) {
            super(view);
            imgMedia = (ImageView) view.findViewById(R.id.row_img_item);
            imgPlay = (ImageView) view.findViewById(R.id.img_play);
        }
    }
}
