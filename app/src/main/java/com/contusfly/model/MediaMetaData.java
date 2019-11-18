/**
 * @category   ContusMessanger
 * @package    com.contusfly.model
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class MediaMetaData.
 */
public class MediaMetaData {

    /**
     * The media type.
     */
    private String mediaName, /**
     * The Media path.
     */
    mediaPath, /**
     * The Media thumb.
     */
    mediaThumb, /**
     * The Media size.
     */
    mediaSize, /**
     * The Media type.
     */
    mediaType;
    private String messageId;

    /**
     * Converts MediaMetaData list to json string
     *
     * @param mediaMetaData MediaMetaData list
     * @return String: json representation of MediaMetaData list
     */
    public static String convertMediaMetaDataListToJsonString(List<MediaMetaData> mediaMetaData) {
        Type type = new TypeToken<List<MediaMetaData>>() {
        }.getType();
        if (mediaMetaData != null)
            return new Gson().toJson(mediaMetaData, type);
        return "";
    }

    /**
     * Converts MediaMetaData list json string representation to MediaMetaData list
     *
     * @param mediaMetaDataListJson String: json representation of MediaMetaData list
     * @return result MediaMetaData list
     */
    public static List<MediaMetaData> convertJsonStringToMediaMetaDataLis(String mediaMetaDataListJson) {
        Type type = new TypeToken<List<MediaMetaData>>() {
        }.getType();
        if (!TextUtils.isEmpty(mediaMetaDataListJson))
            return new Gson().fromJson(mediaMetaDataListJson, type);
        return new ArrayList<>();
    }

    /**
     * Converts MediaMetaData to json string representation
     *
     * @param mediaMetaData MediaMetaData object
     * @return String:  json string representation of MediaMetaData
     */
    public static String convertMediaMetaDataToJsonString(MediaMetaData mediaMetaData) {
        if (mediaMetaData != null)
            return new Gson().toJson(mediaMetaData);
        return "";
    }

    /**
     * Converts json representation of MediaMetaData to MediaMetaData object
     *
     * @param mediaMetaDataJson String: json representation of MediaMetaData
     * @return MediaMetaData object
     */
    public static MediaMetaData convertJsonStringToMediaMetaData(String mediaMetaDataJson) {
        if (!TextUtils.isEmpty(mediaMetaDataJson))
            return new Gson().fromJson(mediaMetaDataJson, MediaMetaData.class);
        return new MediaMetaData();
    }

    /**
     * Gets the media name.
     *
     * @return the media name
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     * Sets the media name.
     *
     * @param mediaName the new media name
     */
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    /**
     * Gets the media path.
     *
     * @return the media path
     */
    public String getMediaPath() {
        return mediaPath;
    }

    /**
     * Sets the media path.
     *
     * @param mediaPath the new media path
     */
    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    /**
     * Gets the media thumb.
     *
     * @return the media thumb
     */
    public String getMediaThumb() {
        return mediaThumb;
    }

    /**
     * Sets the media thumb.
     *
     * @param mediamediaThumb the new media thumb
     */
    public void setMediaThumb(String mediamediaThumb) {
        this.mediaThumb = mediamediaThumb;
    }

    /**
     * Gets the media size.
     *
     * @return the media size
     */
    public String getMediaSize() {
        return mediaSize;
    }

    /**
     * Sets the media size.
     *
     * @param mediaSize the new media size
     */
    public void setMediaSize(String mediaSize) {
        this.mediaSize = mediaSize;
    }

    /**
     * Gets the media type.
     *
     * @return the media type
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * Sets the media type.
     *
     * @param mediaType the new media type
     */
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Checks if is media available.
     *
     * @return true, if is media available
     */
    public boolean isMediaAvailable() {
        if (mediaPath != null && !mediaPath.isEmpty()) {
            File file = new File(mediaPath);
            return file.exists();
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final MediaMetaData other = (MediaMetaData) obj;
        return this.getMessageId() != null && this.getMessageId().equals(other.getMessageId());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = getHash(hash, mediaName);
        hash = getHash(hash, mediaPath);
        hash = getHash(hash, mediaThumb);
        hash = getHash(hash, mediaSize);
        hash = getHash(hash, mediaType);
        hash = getHash(hash, messageId);
        return hash;
    }

    /**
     * Appends the hash code based on the filed and return the hascode
     *
     * @param hash  initial hash
     * @param field field to calculate hash
     * @return result hash
     */
    private int getHash(int hash, String field) {
        if (!TextUtils.isEmpty(field))
            return 7 * hash + field.hashCode();
        return hash;
    }
}