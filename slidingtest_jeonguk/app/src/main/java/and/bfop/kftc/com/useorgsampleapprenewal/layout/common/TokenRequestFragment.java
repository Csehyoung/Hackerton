package and.bfop.kftc.com.useorgsampleapprenewal.layout.common;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashMap;
import java.util.Map;

import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.authnewweb.AuthNewWebMenuFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.autholdweb.AuthOldWebMenuFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.restclient.RetrofitCustomAdapter;
import and.bfop.kftc.com.useorgsampleapprenewal.restclient.RetrofitUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.StringUtil;
import butterknife.OnClick;


/**
 * authcode 발급 이후 token 발급 요청을 할 수 있는 Fragment 페이지
 *
 *  - 업무수행에 있어서 필수적인 페이지는 아님. token 발급 과정을 보여주기 위해서 작성한 페이지
 */
public class TokenRequestFragment extends BaseFragment {

    private String authcode;
    private String scope;
    String invokerType;

    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_token_req_web, container, false);
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), true));

        // EditText 에 authcode 등을 바인딩한다.
        Bundle args = this.getArguments();
        authcode = StringUtil.defaultString(args.getString("authcode"));
        scope = StringUtil.urlDecode(StringUtil.defaultString(args.getString("scope"))); // 다중스코프에서 space가 +기호가 되지 않도록 URLDecode 처리
        invokerType = StringUtil.defaultString(args.getString("invokerType"));
        Log.d("##", "TokenRequestFragment > authcode:["+authcode+"], scope:["+scope+"], invokerType:["+invokerType+"]");

        EditText etAuthorizationCode = (EditText) view.findViewById(R.id.etAuthorizationCode);
        EditText etScope = (EditText) view.findViewById(R.id.etScope);
        etAuthorizationCode.setText(authcode);
        etScope.setText(scope);

        return view;
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    /**
     * 버튼 onclick 이벤트핸들러
     *
     * @param v
     */
    @OnClick(R.id.btnToken)
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnToken:
                getToken();
                break;
            default:
                break;
        }
    }

    /**
     * 토큰 발급
     */
    private void getToken(){

        String type = this.getArguments().getString("invokerType");
        String redirectUriKey = (StringUtil.isNotBlank(type) && "APP".equals(type)) ? "APP_CALLBACK_URL" : "WEB_CALLBACK_URL";

        Map params = new LinkedHashMap<>();
        params.put("code", authcode);
        params.put("client_id", StringUtil.getPropStringForEnv("APP_KEY"));
        params.put("client_secret", StringUtil.getPropStringForEnv("APP_SECRET"));
        params.put("redirect_uri", StringUtil.getPropStringForEnv(redirectUriKey));
        params.put("grant_type", "authorization_code");

        RetrofitUtil.callAsync(RetrofitCustomAdapter.getInstance().token(params), getActivity()); // rest client 호출
    }

    /**
     * 뒤로가기 버튼을 눌렀을 때의 동작
     */
    @Override
    public void onBackPressedForFragment() {

        Class<? extends BaseFragment> fragmentClass = null;
        switch(invokerType){
            case "WEB_AUTH_NEW": // 사용자인증 개선버전 에서 들어온 경우
                fragmentClass = AuthNewWebMenuFragment.class;
                break;
            default: // 사용자인증 기존버전 (웹 방식) 에서 들어온 경우
                fragmentClass = AuthOldWebMenuFragment.class;
                break;
        }
        // Fragment 교체
        FragmentUtil.replaceNewFragment(fragmentClass);
    }

}
