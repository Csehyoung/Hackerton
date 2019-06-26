package and.bfop.kftc.com.useorgsampleapprenewal.layout;

import android.app.Activity;
import android.app.Fragment;
import android.app.Instrumentation;
import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import and.bfop.kftc.com.useorgsampleapprenewal.SlidingRootNav;
import and.bfop.kftc.com.useorgsampleapprenewal.SlidingRootNavBuilder;
import and.bfop.kftc.com.useorgsampleapprenewal.fragment.CenteredTextFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.apicall.APICallMenuFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.menu.DrawerAdapter;
import and.bfop.kftc.com.useorgsampleapprenewal.menu.DrawerItem;
import and.bfop.kftc.com.useorgsampleapprenewal.menu.SimpleItem;
import and.bfop.kftc.com.useorgsampleapprenewal.menu.SpaceItem;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;

import static and.bfop.kftc.com.useorgsampleapprenewal.layout.AppPreferences.post_cnt;

public class SildingActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener,AuthenticationListener {

    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_CART = 3;
    private static final int POS_LOGOUT = 5;

    private String token = null;
    private AppPreferences appPreferences = null;
    private AuthenticationDialog authenticationDialog = null;
    private Button button = null;
    private Button button2 = null;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    private FoldingCell fc1 = null;
    private FoldingCell fc2= null;
    private FoldingCell fc3= null;
    private FoldingCell fc4=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gogogo(v);
            }
        });
        button2 = (Button)findViewById(R.id.logout_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_logout(v);
            }
        });

        fc1 = (FoldingCell) findViewById(R.id.folding_cell1);
        fc2 = (FoldingCell) findViewById(R.id.folding_cell2);
        fc3 = (FoldingCell) findViewById(R.id.folding_cell3);
        fc4 = (FoldingCell) findViewById(R.id.folding_cell4);

        TextView sns_text = (TextView)findViewById(R.id.sns_text);
        TextView telecom_text = (TextView)findViewById(R.id.telecom_text);
        TextView bank_book_text = (TextView)findViewById(R.id.bank_book_text);
        TextView call_list_text = (TextView)findViewById(R.id.call_list_text);

        sns_text.setPaintFlags(sns_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        telecom_text.setPaintFlags(telecom_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        bank_book_text.setPaintFlags(bank_book_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        call_list_text.setPaintFlags(call_list_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        fc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc1.toggle(false);
            }
        });
        fc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc2.toggle(false);
            }
        });
        fc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc3.toggle(false);
            }
        });
        fc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc4.toggle(false);
            }
        });




        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_CART),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        final TextView textViewBtn = (TextView)findViewById(R.id.textViewBtn);
        textViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class<? extends BaseFragment> fragmentClass = null;
                fragmentClass = APICallMenuFragment.class;
                FragmentUtil.newFragment(fragmentClass);
            }
        });

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);

        appPreferences = new AppPreferences(this);

        //check already have access token
        token = appPreferences.getString(AppPreferences.TOKEN);
        if (token != null) {
            getUserInfoByAccessToken(token);
        }
        Point point = getScreenSize(this);
        LinearLayout sns_profile = (LinearLayout) findViewById(R.id.insta_profile);
        LinearLayout sns_login = (LinearLayout) findViewById(R.id.insta_login);
        int Status_height1 = getStatusBarHeight1();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(point.x,point.y-Status_height1-146);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(point.x,point.y-Status_height1-146);
        sns_profile.setLayoutParams(layoutParams);
        sns_login.setLayoutParams(layoutParams1);
        getFileStreamPath()
    }
    public int getStatusBarHeight1() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size;
    }

    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            finish();
        }
        slidingRootNav.closeMenu();

        Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        showFragment(selectedScreen);
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }


    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    public void login() {
        LinearLayout sns_login = (LinearLayout) findViewById(R.id.insta_login);
        sns_login.setVisibility(View.GONE);

        ImageView pic = (ImageView) findViewById(R.id.user_profile_pic);
        Picasso.with(this).load(appPreferences.getString(AppPreferences.PROFILE_PIC)).into(pic);
        TextView name = (TextView) findViewById(R.id.user_id);
        name.setText(appPreferences.getString(AppPreferences.USER_NAME));
        TextView Like_cnt = (TextView) findViewById(R.id.like_cnt);
        Like_cnt.setText(appPreferences.getString(AppPreferences.User_like_cnt));
        TextView Follower_cnt = (TextView) findViewById(R.id.follower_cnt);
        Follower_cnt.setText(appPreferences.getString(AppPreferences.Follows));
        TextView Followed_cnt = (TextView) findViewById(R.id.following_cnt);
        Followed_cnt.setText(appPreferences.getString(AppPreferences.Followed));
        TextView Photo_cnt = (TextView) findViewById(R.id.photo_cnt);
        Photo_cnt.setText(appPreferences.getString(post_cnt));
        TextView Video_cnt = (TextView) findViewById(R.id.video_cnt);
        Video_cnt.setText(appPreferences.getString(AppPreferences.video_cnt));
        TextView Comment_cnt = (TextView) findViewById(R.id.comment_cnt);
        Comment_cnt.setText(appPreferences.getString(AppPreferences.comments));
        TextView Follow_mod_followed = (TextView) findViewById(R.id.f_mod_f);
        Follow_mod_followed.setText(appPreferences.getString(AppPreferences.Following_Mod_Follweded));
        TextView Comment_mod_post = (TextView) findViewById(R.id.c_mod_p);
        Comment_mod_post.setText(appPreferences.getString(AppPreferences.Comment_Mod_Counts));
        TextView Like_mod_post = (TextView) findViewById(R.id.l_mod_p);
        Like_mod_post.setText(appPreferences.getString(AppPreferences.Likes_Mod_Counts));
        TextView Recent_post = (TextView) findViewById(R.id.Recent_post);
        Recent_post.setText(appPreferences.getString(AppPreferences.Recent_post_date));
        TextView Go_my_page = (TextView) findViewById(R.id.go_my_page);
        Go_my_page.setText("www.instagram.com/" + appPreferences.getString(AppPreferences.USER_NAME));
        Linkify.addLinks(Go_my_page,Linkify.WEB_URLS);
        LinearLayout sns_profile = (LinearLayout) findViewById(R.id.insta_profile);
        sns_profile.setVisibility(View.VISIBLE);

        Toast toast = Toast.makeText(getApplicationContext(),"인스타그램 로그인 완료",Toast.LENGTH_LONG);
        toast.show();
    }

    public void SetClick() {
        final Instrumentation inst = new Instrumentation();
        new Thread() {
            public void run() {
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 200, 500, 0);
                MotionEvent event1 = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 200, 500, 0);
                inst.sendPointerSync(event);
                inst.sendPointerSync(event1);

                this.interrupt();
            }
        }.start();
    }

    public void logout() {
        token = null;
        appPreferences.clear();
        Toast toast = Toast.makeText(getApplicationContext(),"인스타그램 로그아웃 완료",Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;
        appPreferences.putString(AppPreferences.TOKEN, auth_token);
        token = auth_token;
        getUserInfoByAccessToken(token);
    }

    public void gogogo(View view) {
            authenticationDialog = new AuthenticationDialog(this, this);
            authenticationDialog.setCancelable(true);
            authenticationDialog.show();
    }
    public void logout_logout(View view) {
        logout();
        LinearLayout sns_profile = (LinearLayout) findViewById(R.id.insta_profile);
        LinearLayout sns_login = (LinearLayout) findViewById(R.id.insta_login);
        sns_profile.setVisibility(View.GONE);
        sns_login.setVisibility(View.VISIBLE);
        SetClick();
    }
    private void getUserInfoByAccessToken(String token) {
        new RequestInstagramAPI().execute();
        new RequestInstagramAPI2().execute();
    }

    private class RequestInstagramAPI extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(getResources().getString(R.string.get_user_info_url) + token);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    JSONObject jsonData2 = jsonData.getJSONObject("counts");

                    if (jsonData.has("id")) {
                        appPreferences.putString(AppPreferences.USER_ID, jsonData.getString("id"));
                        appPreferences.putString(AppPreferences.USER_NAME, jsonData.getString("username"));
                        appPreferences.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"));
                        appPreferences.putString(AppPreferences.Follows,jsonData2.getString("follows"));
                        appPreferences.putString(AppPreferences.Followed,jsonData2.getString("followed_by"));
                        double following_mod_followed = jsonData2.getDouble("follows") / jsonData2.getDouble("followed_by");
                        following_mod_followed = Math.round(following_mod_followed*1000)/1000.0;
                        appPreferences.putString(AppPreferences.Following_Mod_Follweded,String.valueOf(following_mod_followed));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(),"Ошибка входа!",Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private class RequestInstagramAPI2 extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet= new HttpGet(getResources().getString(R.string.get_user_info_U) + token);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    int cnt = 0,video_cnt=0,post_cnt=0,user_like=0;
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonData2 = jsonObject.getJSONArray("data");

                    for(int i=0;i<jsonData2.length();i++) {
                        JSONObject jsondata3 = jsonData2.getJSONObject(i);
                        JSONObject jsondata4 = jsondata3.getJSONObject("comments");
                        JSONObject jsondata5 = jsondata3.getJSONObject("likes");

                        if (i == 0) {
                            String recent_post_date = jsondata3.getString("created_time");
                            long recent_post_date_long = Long.parseLong(recent_post_date)*1000;
                            Date date = new Date(recent_post_date_long);
                            DateFormat foramtter = new SimpleDateFormat("YYYY MM dd");
                            appPreferences.putString(AppPreferences.Recent_post_date,"recent post : " + foramtter.format(date));
                        }
                        cnt += jsondata4.getInt("count");
                        user_like += jsondata5.getInt("count");
                        if (jsondata3.has("videos")) video_cnt++;
                        else post_cnt++;
                    }

                    appPreferences.putString(AppPreferences.Likes_Mod_Counts,String.valueOf(Math.round(((double)user_like / (double)(post_cnt+video_cnt))*1000)/1000.0));
                    appPreferences.putString(AppPreferences.Comment_Mod_Counts,String.valueOf(Math.round(((double)cnt / (double)(post_cnt + video_cnt))*1000)/1000.0));
                    appPreferences.putString(AppPreferences.comments,String.valueOf(cnt));
                    appPreferences.putString(AppPreferences.post_cnt,String.valueOf(post_cnt));
                    appPreferences.putString(AppPreferences.video_cnt,String.valueOf(video_cnt));
                    appPreferences.putString(AppPreferences.User_like_cnt,String.valueOf(user_like));

                    login();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(),"Ошибка входа!",Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
