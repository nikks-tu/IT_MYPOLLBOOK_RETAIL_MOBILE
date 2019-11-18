package com.contus.chatlib.listeners;

import java.net.URL;

/**
 * Created by user on 4/10/16.
 */
public interface OnTaskCompleted {
    void onTaskCompleted(URL url, String type, String encodedImg, int fileSize);
}
