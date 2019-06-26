package and.bfop.kftc.com.useorgsampleapprenewal.layout.autholdweb;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashMap;
import java.util.Map;

import and.bfop.kftc.com.useorgsampleapprenewal.App;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseWebAuthInterface;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseWebFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.TokenRequestFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.util.BeanUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.StringUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.WebViewUtil;


/**
 * 계좌등록 기존버전 (웹 방식) Fragment
 */
public class AuthOldWebPageRegisterAccountFragment extends BaseWebFragment implements BaseWebAuthInterface {

    private static String URI = "/oauth/2.0/register_account";

    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webview_common, container, false);
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), true));

        // querystring을 만들기 위한 Map
        Map<String, String> pMap = new LinkedHashMap<>();
        pMap.put("response_type", "code");
        pMap.put("client_id", StringUtil.getPropStringForEnv("APP_KEY"));
        pMap.put("redirect_uri", StringUtil.getPropStringForEnv("WEB_CALLBACK_URL"));
        pMap.put("scope", StringUtil.getPropStringForEnv("SCOPE"));
        pMap.put("client_info", "whatever_you_want");

        // 호출 URL (querystring 포함)
        String urlToLoad = (App.getApiBaseUrl() + URI) + "?" + StringUtil.convertMapToQuerystring(pMap);

        // WebView로 url 호출
        WebViewUtil.loadUrlOnWebView(view, this, urlToLoad, null);

        return view;
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    @Override
    public void onAuthCodeResponse(Map<String, Object> pMap) {

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

        FragmentUtil.replaceNewFragment(AuthOldWebMenuFragment.class);
    }
}
