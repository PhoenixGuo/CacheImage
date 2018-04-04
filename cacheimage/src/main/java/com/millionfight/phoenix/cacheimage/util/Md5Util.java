package com.millionfight.phoenix.cacheimage.util;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    public static String getMD5String(String value) {

        if (TextUtils.isEmpty(value)) {
            return "";
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        md.update(value.getBytes());
        byte[] b = md.digest();
        StringBuilder sb = new StringBuilder();
        int val = 0;
        for (int i = 0; i < b.length; i++) {
            val = b[i];
            if (val < 0) {
                val += 256;
            }
            if (val < 0x10) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }

        return sb.toString();
    }

    public static String encode(String key, String value) {
        StringBuffer buffer = new StringBuffer(value);
        int k = 0;
        int offset = (value.charAt(0) % 10) + 1;
        int step = offset % 3 == 0 ? 1 : offset % 3;
        for (int i = 0; i < key.length(); i++) {
            buffer.insert(i + offset + k, key.charAt(i));
            k += step;
        }
        buffer.append(step);
        return buffer.toString();
    }

    public static String[] decode(String value) {
        StringBuffer buffer = new StringBuffer(value);
        int k = 0;
        int offset = (value.charAt(0) % 10) + 1;
        int step = Integer.parseInt(buffer.charAt(buffer.length() - 1) + "") - 1;
        buffer.delete(buffer.length() - 1, buffer.length());
        StringBuffer key = new StringBuffer();
        for (int i = 0; i < 11; i++) {
            key.append(buffer.toString().charAt(i + offset + k));
            buffer.delete(i + offset + k, i + offset + k + 1);
            k += step;
        }
        return new String[]{key.toString(), buffer.toString()};
    }

}
