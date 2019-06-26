package and.bfop.kftc.com.useorgsampleapprenewal.layout.authnewweb;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.ActionBarChangeEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.util.Constants;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.StringUtil;

/**
 ** 사용자인증 개선버전 부모 Fragment
 */
public class AuthNewWebPageBaseFragment extends BaseFragment {

    // authorize2-authorize_account2간 UI 공유로 인해 추가된 코드
    protected Map<String, String> typeMap;

    /**
     * Fragment를 초기화하여 View를 리턴한다.
     *
     * @param inflater
     * @param container
     * @param fragmentResourceId
     * @return
     */
    protected View initView(LayoutInflater inflater, ViewGroup container, int fragmentResourceId) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(fragmentResourceId, container, false);
        initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), true)); // true/false 주의

        // authorize2-authorize_account2간 UI 공유로 인해 추가된 코드
        typeMap = (Map<String, String>)this.getArguments().getSerializable("TYPE_MAP");
        ((Button)view.findViewWithTag("btnInvoke")).setText(typeMap.get("TITLE")); // 호출 버튼 라벨 교체 (ex: "사용자인증 개선버전 (Case1)")
        EventBus.getDefault().post(new ActionBarChangeEvent(typeMap.get("TITLE"))); // 액션바 타이틀 교체

        return view;
    }

    /**
     * 매개변수로 받은 URL을 WebView(사용자인증 개선버전용)로 호출한다.
     *
     * @param urlToLoad
     * @param headerMap
     */

    protected void callUrlUsingWebView(String urlToLoad, HashMap<String, String> headerMap) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        BaseFragment webViewFragment = FragmentUtil.newFragment(AuthNewWebCommonWebViewFragment.class);
        Bundle args = webViewFragment.getArguments();
        args.putString(Constants.ACTIONBAR_TITLE, this.getActionBarTitle()); // 액션바 타이틀 전달
        args.putString("urlToLoad", urlToLoad); // 호출 URL 전달
        args.putSerializable("headerMap", headerMap); // Map을 전달할 때는 putSerializable() 사용
        webViewFragment.setArguments(args);
        ft.replace(android.R.id.tabcontent, webViewFragment);
        ft.commit();
    }

    /**
     * 사용자인증 개선버전에서 사용하는 공통 파라미터 Map을 생성하여 리턴
     *
     * @param v
     * @return
     */
    protected Map<String, String> getDefaultParamMap(View v){

        Map<String, String> pMap = new LinkedHashMap<>();
        pMap.put("client_id", StringUtil.getPropStringForEnv("APP_KEY"));
        pMap.put("redirect_uri", StringUtil.getPropStringForEnv("WEB_CALLBACK_URL"));
        pMap.put("response_type", "code"); // 고정값
        pMap.put("scope", FragmentUtil.getEtVal(v, R.id.et_ANW_SCOPE));
        pMap.put("client_info", FragmentUtil.getEtVal(v, R.id.et_ANW_CLIENT_INFO));
        pMap.put("bg_color", FragmentUtil.getEtVal(v, R.id.et_ANW_BG_COLOR));
        pMap.put("txt_color", FragmentUtil.getEtVal(v, R.id.et_ANW_TXT_COLOR));
        pMap.put("btn1_color", FragmentUtil.getEtVal(v, R.id.et_ANW_BTN1_COLOR));
        pMap.put("btn2_color", FragmentUtil.getEtVal(v, R.id.et_ANW_BTN2_COLOR));
        return pMap;
    }

    /**
     * 뒤로가기 버튼을 눌렀을 때의 동작
     */
    public void onBackPressedForFragment() {

        FragmentUtil.replaceNewFragment(AuthNewWebMenuFragment.class);
    }

}
