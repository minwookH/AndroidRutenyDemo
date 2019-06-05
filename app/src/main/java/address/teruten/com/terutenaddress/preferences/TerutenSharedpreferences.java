package address.teruten.com.terutenaddress.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by teruten on 2017-06-22.
 */

public class TerutenSharedpreferences {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final String PREFERENCES_NAME = "teruten";

    public static final String USER_ID = "userId";
    public static final String USER_PW = "userPw";
    public static final String NOTIFICATION_USE_KEY = "isNotification";

    public static final String LOGIN_ID_SAVE = "isLoginIdSave";
    public static final String AUTO_LOGIN = "isAutoLogin";

    public static final String FCM_TOKEN = "FCM_TOKEN";
    //isNotification

    public TerutenSharedpreferences(Context context){
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        //sharedPreferences = context.getdef(PREFERENCES_NAME, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setStringPreferences(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public void setIntPreferences(String key, int value){
        editor.putInt(key, value);
        editor.commit();
    }

    public void setBooleanPreferences(String key, boolean value){
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getStringPreferences(String key){
        return sharedPreferences.getString(key, "");
    }

    public int getIntPreferences(String key){
        return sharedPreferences.getInt(key, -1);
    }

    public boolean getBooleanPreferences(String key){
        return sharedPreferences.getBoolean(key, false);
    }
}
