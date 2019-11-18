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
import android.text.format.DateFormat;

import com.polls.polls.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * The Class ChatMsgTime.
 */
public class ChatMsgTime {

    /**
     * The date hour format.
     */
    public static SimpleDateFormat dateFormat, dateHourFormat;

    /**
     * The time stamp.
     */
    public static Calendar timeStamp;

    /**
     * The diff.
     */
    private static long epochTime, diff;

    /**
     * The today.
     */
    private static Date today;

    /**
     * The day.
     */
    private static int dateDiff, sec, min, hour, day;

    /**
     * The result time.
     */
    private static String time, todayTime, resultTime;

    /**
     * Instantiates a new chat msg time.
     */
    public ChatMsgTime() {
        // Un used Constructor
    }

    /**
     * Gets the day sent msg.
     *
     * @param context    the context
     * @param epochetime the epochetime
     * @return the day sent msg
     */
    @SuppressWarnings("deprecation")
    public static String getDaySentMsg(Context context, long epochetime) {
        Date now = new Date(epochetime * 1000);
        int format = DateFormat.is24HourFormat(context) ? 24 : 12;
        if (format == 12) {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa",
                    Locale.getDefault());
            dateHourFormat = new SimpleDateFormat("hh:mm aa",
                    Locale.getDefault());
        } else if (format == 24) {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                    Locale.getDefault());
            dateHourFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
        timeStamp = Calendar.getInstance();
        epochTime = timeStamp.getTimeInMillis();
        today = new Date(epochTime);
        diff = today.getTime() - now.getTime();
        dateDiff = today.getDate() - now.getDate();
        time = dateFormat.format(now.getTime());
        todayTime = dateHourFormat.format(now.getTime());
        return calculateTime();
    }

    /**
     * Calculate time.
     *
     * @return the string
     */
    private static String calculateTime() {
        resultTime = time;
        sec = (int) (diff / 1000);
        if (sec < 60)
            resultTime = todayTime;
        min = sec / 60;
        if (min < 60 && min >= 1)
            resultTime = todayTime;
        hour = min / 60;
        if (hour < 24 && hour >= 1) {
            if (dateDiff == 0)
                resultTime = todayTime;
            else if (dateDiff == 1)
                resultTime = todayTime;
            else
                resultTime = time;
        }
        day = hour / 24;
        if ((day >= 1 && day < 2) || day > 2)
            resultTime = time;
        return resultTime;
    }

    /**
     * Chat view get day.
     *
     * @param context    the context
     * @param epochetime the epochetime
     * @param format     the format
     * @return the string
     */
    public static String chatViewGetDay(Context context, long epochetime,
                                        int format) {
        try {
            java.sql.Date now = new java.sql.Date(epochetime * 1000);
            SimpleDateFormat sdf = null, sdf1 = null;
            if (format == 12) {
                sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa",
                        Locale.getDefault());
                sdf1 = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            } else if (format == 24) {
                sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                        Locale.getDefault());
                sdf1 = new SimpleDateFormat("HH:mm", Locale.getDefault());
            }
            String lastSeen = context.getString(R.string.text_last_seen);
            String txtToday = context.getString(R.string.text_today);
            String txtYesterday = context.getString(R.string.text_yesterday);

            Calendar timeStamp = Calendar.getInstance();
            long epochTime = timeStamp.getTimeInMillis();
            java.sql.Date today = new java.sql.Date(epochTime);
            long diff = today.getTime() - now.getTime();
            @SuppressWarnings("deprecation")
            int dateDiff = today.getDate() - now.getDate();
            String time = sdf.format(now.getTime());
            String todayTime = sdf1.format(now.getTime());
            int sec = (int) (diff / 1000);
            if (sec < 60 && sec > 0) {
                return lastSeen + " " + txtToday + " " + todayTime;
            }
            int min = sec / 60;
            if (min < 60 && min >= 1) {
                return lastSeen + " " + txtToday + " " + todayTime;
            }

            int hour = (int) (min / 60);
            if (hour < 24 && hour >= 1) {
                if (dateDiff == 0) {
                    return lastSeen + " " + txtToday + " " + todayTime;
                } else if (dateDiff == 1) {
                    return lastSeen + " " + txtYesterday + " " + todayTime;
                } else {
                    return lastSeen + " " + time;
                }
            }
            int day = (int) (hour / 24);
            if (day >= 1 && day < 2) {
                return lastSeen + " " + txtYesterday + " " + time;
            }
            if (day > 2) {
                return lastSeen + " " + time;
            }
            return lastSeen + " " + time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
