package io.forsta.ccsm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.spongycastle.util.encoders.Base64Encoder;
import org.thoughtcrime.securesms.util.Base64;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by jlewis on 1/6/17.
 */

public class ForstaPreferences {
    private static final String API_KEY = "api_key";
    private static final String API_LAST_LOGIN = "last_login";
    private static final String CCSM_DEBUG = "ccsm_debug";

    public static boolean isRegisteredForsta(Context context) {
        String key = ForstaPreferences.getStringPreference(context, API_KEY);
        if (key !=  "") {
            return true;
        }
        return false;
    }

    public static void setRegisteredForsta(Context context, String value) {
        setStringPreference(context, API_KEY, value);
    }

    public static String getRegisteredKey(Context context) {
        return getStringPreference(context, API_KEY);
    }

    public static Date getRegisteredExpireDate(Context context) {
        Date expireDate = new Date();
        String token = getStringPreference(context, API_KEY);
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length == 3) {
            try {
                byte[] payload = Base64.decodeWithoutPadding(tokenParts[1]);
                String payloadString = new String(payload, "UTF-8");
                JSONObject obj = new JSONObject(payloadString);
                if (obj.has("exp")) {
                    int expire = obj.getInt("exp");
                    Date dt = new Date(expire);

                    long expireTime = (long) expire * 1000;
                    expireDate = new Date(expireTime);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return expireDate;
    }

    public static void setCCSMDebug(Context context, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(CCSM_DEBUG, value).apply();
    }

    public static boolean isCCSMDebug(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(CCSM_DEBUG, false);
    }

    public static String getRegisteredDateTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(API_LAST_LOGIN, "");
    }

    public static void setRegisteredDateTime(Context context, String lastLogin) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(API_LAST_LOGIN, lastLogin).apply();
    }

    private static void setStringPreference(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, value).apply();
    }

    private static String getStringPreference(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
    }

}
