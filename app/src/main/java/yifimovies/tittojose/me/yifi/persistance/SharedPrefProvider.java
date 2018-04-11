package yifimovies.tittojose.me.yifi.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class SharedPrefProvider {
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    public SharedPrefProvider(Context context) {
        this.sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public static SharedPrefProvider getInstance(Context context) {
        return new SharedPrefProvider(context);
    }

    public String getSharedString(String key, String defaultValue) {
        return sharedpreferences.getString(key, defaultValue);
    }

    public int getSharedInt(String key, int defaultValue) {
        return sharedpreferences.getInt(key, defaultValue);
    }


    public long getSharedLong(String key, long defaultValue) {
        return sharedpreferences.getLong(key, defaultValue);
    }

    public boolean writeSharedLongValue(String key, long value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(key, value);
        editor.apply();
        return true;
    }

    public boolean writeSharedIntValue(String key, int value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.apply();
        return true;
    }

    public boolean writeSharedStringValue(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
        return true;
    }

    public boolean getSharedBoolean(String key, boolean defaultValue) {
        return sharedpreferences.getLong(key, defaultValue ? 1 : 0) == 1;
    }

    public boolean writeSharedBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(key, value ? 1 : 0);
        editor.apply();
        return true;
    }


    public <T> T getSharedObjectValue(String key, Class<T> type) {

        String stringValue = sharedpreferences.getString(key, "");

        if (stringValue.isEmpty()) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(stringValue, type);
        }

    }

    public <T> boolean writeSharedObjectValue(String key, T value) {

        Gson gson = new Gson();
        String objectValue = gson.toJson(value);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, objectValue);
        editor.apply();
        return true;
    }
}
