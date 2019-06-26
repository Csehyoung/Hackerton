package and.bfop.kftc.com.useorgsampleapprenewal.layout.authnewweb;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.ActionBarChangeEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseWebAuthInterface;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseWebFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.TokenRequestFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.util.BeanUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.Constants;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.StringUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.WebViewUtil;


/**
 * 사용자인증 개선버전에서 공통적으로 사용하는 WebView Fragment
 */
public class AuthNewWebCommonWebViewFragment extends BaseWebFragment implements BaseWebAuthInterface {

    private WebView webView;

    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webview_common, container, false);
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), true));

        Bundle args = this.getArguments();

        // 액션바 타이틀 교체
        EventBus.getDefault().post(new ActionBarChangeEvent(args.getString(Constants.ACTIONBAR_TITLE)));

        // Bundle 파라미터로 받은 url 풀셋
        String urlToLoad = StringUtil.defaultString(args.getString("urlToLoad"));

        // Bundle 파라미터로 받은 header Map
        Map<String, String> headerMap = (Map<String, String>)args.getSerializable("headerMap");
        if(headerMap != null){
            // 사용자이름 필드를 url encoding 한다 (G/W에서 디코딩 해 주는 설정 있음)
            headerMap.put("Kftc-Bfop-UserName", StringUtil.urlEncode(headerMap.get("Kftc-Bfop-UserName")));
        }

        // WebView로 url 호출
        WebViewUtil.loadUrlOnWebView(view, this, urlToLoad, headerMap);

        return view;
    }

    @Override
    public void onResume() {

//        MessageUtil.showToast(BeanUtil.getClassName(this)+".onResume() called!");
        super.onResume();

        // WebViewFragment 를 참조한 코드
        if(webView != null){ webView.onResume(); }
    }

    @Override
    public void onPause() {

//        MessageUtil.showToast(BeanUtil.getClassName(this)+".onPause() called!");
        super.onPause();

        // WebViewFragment 를 참조한 코드
        if(webView != null){ webView.onPause(); }
    }

    @Override
    public void onDestroy() {

//        MessageUtil.showToast(BeanUtil.getClassName(this)+".onDestroy() called!");
        super.onDestroy();

        // WebViewFragment 를 참조한 코드
        if(webView != null){
            webView.destroy();
            webView = null;
        }
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    @Override
    public void onAuthCodeResponse(Map<String, Object> pMap) {

        // TokenRequestFragment 페이지에서의 뒤로가기 분기를 위해서 추가
        pMap.put("invokerType", "WEB_AUTH_NEW");

        Log.d("##", BeanUtil.getClassName(this)+".onAuthCodeResponse() > pMap: "+pMap);

        // token 발급 요청 Fragment로 이동한다.
        BaseFragment tokenRequestFragment = FragmentUtil.newFragment(TokenRequestFragment.class);
        BeanUtil.putAllMapToBundle(tokenRequestFragment.getArguments(), pMap); // webview resposne 에서 추출한 authcode 등을 TokenRequestFragment 에 넣어준다.
        FragmentUtil.replaceFragment(tokenRequestFragment);
    }

    /**
     * 뒤로가기 버튼을 눌렀을 때의 동작
     */
    @Override
    public void onBackPressedForFragment() {

        FragmentUtil.replaceNewFragment(AuthNewWebMenuFragment.class);
    }
}
