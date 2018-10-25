package com.kitkat.android.tcpipsocketserver;

/**
 * Created by user on 2018-10-25.
 */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.app.Activity;
import android.util.Base64;

public class TestEncodingDecodingActivity extends Activity {

    /**
     * Base64 인코딩
     */
    public static String getBase64encode(String content){
        return Base64.encodeToString(content.getBytes(), 0);
    }

    /**
     * Base64 디코딩
     */
    public static String getBase64decode(String content){
        return new String(Base64.decode(content, 0));
    }

    /**
     * getURLEncode
     */
    public static String getURLEncode(String content){
        try {
//          return URLEncoder.encode(content, "utf-8");   // UTF-8
            return URLEncoder.encode(content, "euc-kr");  // EUC-KR
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getURLDecode
     */
    public static String getURLDecode(String content){
        try {
//          return URLDecoder.decode(content, "utf-8");   // UTF-8
            return URLDecoder.decode(content, "euc-kr");  // EUC-KR
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
