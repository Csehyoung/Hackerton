package and.bfop.kftc.com.useorgsampleapprenewal.layout;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.ActionBarChangeEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.BackButtonPressedInMainEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentReplaceEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.GoPageInMainActivityEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.SMSReceiveEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.handler.BackPressCloseHandler;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.apicall.APICallMenuFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.authnewweb.AuthNewWebMenuFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.OSSLicensePageFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.settings.SettingsFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.MessageUtil;

/**
 * 메인 Activity 클래스
 *
 *  - 본 앱은 1 Activity - multi Fragment 형태로 구성되어 있음.
 *  - 각 Fragment 에서 MainActivity의 기능이 필요할 경우, 직접 호출하지 않고 EventBus를 사용하도록 설계함.(프로그램의 결합도를 낮추기 위해서)
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle drawerToggle;
    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    private BackPressCloseHandler backPressCloseHandler;

    private boolean toolbarNavigationListenerRegistered = false;

    ///////////////////////////////////// Activity Lifecycle Callbacks - start /////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // actionbar 좌측에 뒤로가기 화살표 표시

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 뒤로가기 버튼 종료 관련 핸들러
        backPressCloseHandler = new BackPressCloseHandler(this);

        //================================ fragment 추가 - start ================================
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = FragmentUtil.newFragment(MainFragment.class);
            fm.beginTransaction().add(R.id.fragment_container, fragment)
                    //.addToBackStack(null)
                    .commit();
        }
        //================================ fragment 추가 - end ==================================

        // 뒤로가기시 액션바 타이틀도 같이 변경해 주도록 OnBackStackChangedListener 추가
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                BaseFragment fragment = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if(fragment != null){ // 이 조건을 넣지 않으면 초기 기동시 NPE가 발생한다.
                    if(getSupportActionBar() != null){
                        getSupportActionBar().setTitle(fragment.getActionBarTitle());
                    }
                }
            }
        });


        // 레프트메뉴의 로고이미지에 이벤트 바인딩
        setNavLogoOnClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);   // EventBus 등록
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this); // EventBus 해지
    }
    ///////////////////////////////////// Activity Lifecycle Callbacks - end ///////////////////////////////////////

    /**
     * 네비게이션메뉴 선택 이벤트핸들러
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        goPage(item.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 뒤로가기 버튼이 눌렸을 때 최초로 호출되는 메서드
     */
    @Override
    public void onBackPressed() {

        // Navigation Drawer가 열려 있을 경우 닫는다.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        // Navigation Drawer가 닫혀 있을 경우
        } else {
            BaseFragment fragment = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                fragment.onBackPressedForFragment();
            }

        }
    }

    /**
     * 1 depth 메뉴 이동
     *
     *  - NavigationDrawer 에서도 호출하므로 Activity에 정의함.
     *
     * @param id
     */
    public void goPage(int id){

        // 종료 메뉴 선택시
        if(R.id.nav_off == id){
            offApp();
            return;
        }

        if(R.id.btnAPICallMenu == id){
            Intent intent = new Intent(MainActivity.this, SildingActivity.class);
            startActivity(intent);
        }

        Class<? extends BaseFragment> fragmentClass = null;
        switch(id){
            case R.id.btnAuthNewMenu:   // 메인페이지 버튼
            case R.id.nav_authNew:      // Navigation Drawer 메뉴
                fragmentClass = AuthNewWebMenuFragment.class;
                break;
            //case R.id.btnAPICallMenu:
            case R.id.nav_APICall:
                fragmentClass = APICallMenuFragment.class;
                break;
            case R.id.btnSettings:
            case R.id.nav_setting:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.nav_oss_license:
                fragmentClass = OSSLicensePageFragment.class;
            default:
                break;
        }
        FragmentUtil.replaceNewFragment(fragmentClass);
    }

    /**
     * MainActivity 위에 떠 있는 Fragment 교체
     *
     * @param fragment
     */
    private void replaceFragment(BaseFragment fragment) {

        if(fragment != null){

            // Fragment 교체
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            //ft.addToBackStack(null); // 뒤로가기 버튼 클릭시 이전 Fragment 스택을 불러올 수 있게 하기 위한 사전작업
            ft.commit();

            // 액션바 타이틀 교체
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle(fragment.getActionBarTitle());
            }
        }
    }

    /**
     * 액션바에 뒤로가기 화살표 표시 및 뒤로가기 기능 추가
     *
     * @param enable true일 경우에만 뒤로가기 기능 추가를 수행한다.
     */
    public void showBackArrowOnActionBar(boolean enable){

        if (enable) {
            drawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (! toolbarNavigationListenerRegistered) {
                drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                toolbarNavigationListenerRegistered = true;
            }

        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(null);
            toolbarNavigationListenerRegistered = false;
        }

        drawerToggle.syncState(); // 이걸 호출해 주지 않으면 햄버거 버튼이 없어져버리는 현상이 발생한다!!
    }

    /**
     * 앱 종료
     */
    private void offApp(){

        MessageUtil.getDialogBuilder("", "앱을 종료 하시겠습니까?", true, this)
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    // 종료 선택시
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backPressCloseHandler.off();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            // 취소 선택시
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        }).create().show();
    }

    /**
     * 메인페이지로 이동
     */
    public void goMain(){

        FragmentUtil.replaceNewFragment(MainFragment.class);
    }

    /**
     * 레프트메뉴의 로고이미지에 클릭 이벤트 바인딩 (ButterKnife로 구현하기에 어려워서 원래 방법대로 함)
     */
    private void setNavLogoOnClickListener() {

        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        ImageView logo = (ImageView) headerView.findViewById(R.id.logo_in_nav);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START); // 레프트메뉴 닫기
                goMain(); // 메인페이지로
            }
        });
    }

    //======================================== EventBus Subscriber - start ========================================
    /**
     * FragmentInitEvent 에 대한 EventBus Subscriber
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFragmentInitialized(FragmentInitEvent event){

        showBackArrowOnActionBar(event.isBackArrowOnActionBar());
    }

    /**
     * FragmentReplaceEvent 에 대한 EventBus Subscriber
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFragmentReplace(FragmentReplaceEvent event){

        replaceFragment(event.getFragment());
    }

    /**
     * ActionBarChangeEvent 에 대한 EventBus Subscriber
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActionBarChange(ActionBarChangeEvent event){

        String actionBarTitle = event.getActionBarTitle();
        Log.d("##", "ActionBarChangeEvent.getActionBarTitle(): "+actionBarTitle);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(actionBarTitle);
        }
    }

    /**
     * SMSReceiveEvent 에 대한 EventBuss Subscriber
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSMSReceive(SMSReceiveEvent event){

        String sender = event.getSender();
        String contents = event.getContents();
        // 한국모바일인증에서 보낸 본인인증 메시지일 경우
        if("0220338500".equals(sender)){
            String authNum = contents.replaceAll("(.*)(\\d{6})(.*)", "$2"); // 문자 내용 중 6자리 숫자(인증번호)만 추출한다.
//            MessageUtil.showToast("인증번호:["+authNum+"]");
            // 현재 활성화되어 있는 WebView Fragment를 찾아서 javascript injection 한다.
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);
            Log.d("##", "현재 활성화 상태의 Fragment: "+fragment);
            WebView webView = (WebView)fragment.getView().findViewById(R.id.webView); // 해당 Fragment 내에서 Webview를 찾는다.
            webView.loadUrl("javascript:(function(){ document.getElementById('certCode').value = '"+authNum+"' })()");
        }
    }

    /**
     * BackButtonPressedInMainEvent 에 대한 EventBus Subscriber
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackButtonPressedInMain(BackButtonPressedInMainEvent event){

        FragmentManager fm = getSupportFragmentManager();
        int backStackCnt = fm.getBackStackEntryCount();
        Log.d("", "## backStackCnt: "+backStackCnt);
        // backstack이 없을 경우 backPressCloseHandler 를 호출한다
        if (backStackCnt <= 1) {
            backPressCloseHandler.onBackPressed();
        }
    }

    /**
     * GoPageInMainActivityEvent 에 대한 EventBus Subscriber
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGoPageInMainActivity(GoPageInMainActivityEvent event){

        goPage(event.getId());
    }
    //======================================== EventBus Subscriber - end ==========================================

}
