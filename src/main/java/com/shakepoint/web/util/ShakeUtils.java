/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alberto Rubalcaba
 */
public class ShakeUtils {
    public static String encodePassword(String rawPassword) {
        String genPassword = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("sha-256");
            messageDigest.update(rawPassword.getBytes());
            byte[] data = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
            }
            genPassword = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
        }
        return genPassword;
    }

    public static String[] getDateRange(Date from, Date to) {
        //get day difference
        int days = Math.round((to.getTime() - from.getTime()) / 86400000); //convert to days
        final String[] range = new String[days];
        long time = from.getTime();
        String date = "";
        for (int i = 0; i < days; i++) {
            date = ShakeUtils.SIMPLE_DATE_FORMAT.format(new Date(time));
            //add the date to the range array
            range[i] = date;
            time = time + 86400000;
        }
        return range;
    }

    public static final String TMP_FOLDER = "C:/shakepoint-tmp/";
    public static final String QR_CODES_TMP_FOLDER = TMP_FOLDER + "qr-tmp/";
    public static final String QR_CODES_RESOURCES_FOLDER = TMP_FOLDER + "qr/";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy HH:mm");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
    public static final SimpleDateFormat SLASHES_SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
}
