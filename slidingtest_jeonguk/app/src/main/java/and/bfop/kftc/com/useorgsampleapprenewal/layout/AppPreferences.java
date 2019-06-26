package and.bfop.kftc.com.useorgsampleapprenewal.layout;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    public static final String APP_PREFERENCES_FILE_NAME = "userdata";
    public static final String USER_ID = "userID";
    public static final String TOKEN = "token";
    public static final String PROFILE_PIC = "profile_pic";
    public static final String USER_NAME = "username";
    public static final String User_like_cnt = "user_like_cnt";
    public static final String Follows = "follows";
    public static final String Followed = "followed";
    public static final String comments = "comments";
    public static final String post_cnt = "post_cnt";
    public static final String video_cnt = "video_cnt";
    public static final String Following_Mod_Follweded = "Following_Mod_Followed";
    public static final String Likes_Mod_Counts = "Likes_Mod_Counts";
    public static final String Comment_Mod_Counts = "Comment_Mod_Counts";
    public static final String Recent_post_date = "Recent_post_date";

    public SharedPreferences preferences;

    public AppPreferences(Context context) {
        this.preferences = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        return preferences.getString(key, null);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void clear()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}