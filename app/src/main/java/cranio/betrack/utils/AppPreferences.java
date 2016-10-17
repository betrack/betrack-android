package cranio.betrack.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by MariaSol on 13/10/2016.
 */
public class AppPreferences {
    private static final String KEY_SESSIONOPEN = "session_open";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_USERMAIL = "user_email";
    private static final String KEY_BARRELINFO = "barrel_info";
    private static final String KEY_BARRELFOUND = "barrel_found";

    private static AppPreferences mInstance;
   // private final Gson gson;
    private SharedPreferences sharedPrefs;

    public static AppPreferences instance(Application app) {
        if (mInstance == null) {
            mInstance = new AppPreferences(app);
        }
        return mInstance;
    }

    private AppPreferences(Application app) {
      //  this.gson = new Gson();
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(app);
    }

    public void saveSessionOpen(Boolean sessionOpen) {
        sharedPrefs.edit().putBoolean(KEY_SESSIONOPEN, sessionOpen).apply();
    }

    public boolean getSessionOpen() {
        return sharedPrefs.getBoolean(KEY_SESSIONOPEN, false);
    }

    public void saveBarrelFound(Boolean barrelFound) {
        sharedPrefs.edit().putBoolean(KEY_BARRELFOUND, barrelFound).apply();
    }

    public boolean getBarrelFound() {
        return sharedPrefs.getBoolean(KEY_BARRELFOUND, false);
    }



    public void saveUsername(String name) {
        sharedPrefs.edit().putString(KEY_USERNAME, name).apply();
    }

    public void saveUserMail(String email) {
        sharedPrefs.edit().putString(KEY_USERMAIL, email).apply();
    }



    public String getUsername() {
        return sharedPrefs.getString(KEY_USERNAME, "");
    }

    public String getUserEmail() {
        return sharedPrefs.getString(KEY_USERMAIL, "");
    }

    public void saveBarrelInfo(String barrelinfo) {
        sharedPrefs.edit().putString(KEY_BARRELINFO, barrelinfo).apply();
    }



    public String getBarrelInfo() {
        return sharedPrefs.getString(KEY_BARRELINFO, "");
    }

}

