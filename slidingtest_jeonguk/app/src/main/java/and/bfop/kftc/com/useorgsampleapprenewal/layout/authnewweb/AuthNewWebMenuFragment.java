package and.bfop.kftc.com.useorgsampleapprenewal.layout.authnewweb;

import android.Manifest;
import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.MainFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.util.ActivityUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import butterknife.OnClick;

/**
 * 사용자인증 개선버전 메뉴 Fragment
 */
public class AuthNewWebMenuFragment extends BaseFragment {

    /**
     * authorize2-authorize_account2간 UI 공유로 인해 추가된 코드
     * authorize2-authorize_account2간 UI 구성이 하나라도 달라진다면 아예 Fragment를 분리하도록 하자.
     */
    public static final Map<String, HashMap<String, String>> ALL_TYPE_MAP = getAllTypeMap();

    /**
     * ALL_TYPE_MAP 초기화
     *
     * @return
     */
    private static Map<String, HashMap<String, String>> getAllTypeMap() {

        HashMap<String, String> auth2 = new HashMap<>();
        auth2.put("TYPE", "AUTH2");
        auth2.put("URI", "/oauth/2.0/authorize2");
        auth2.put("TITLE", "사용자인증");
        HashMap<String, String> authacnt2 = new HashMap<>();
        authacnt2.put("TYPE", "AUTHACNT2");
        authacnt2.put("URI", "/oauth/2.0/authorize_account2");
        authacnt2.put("TITLE", "계좌등록확인");
        Map<String, HashMap<String, String>> ret = new HashMap<>();
        ret.put("AUTH2", auth2);
        ret.put("AUTHACNT2", authacnt2);
        return ret;
    }

    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_authnew_menu, container, false);
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), false)); // true/false 주의

        // SMS 수신 권한 체크 (Android 6.0 이상에서는 권한설정 체크작업을 하지 않으면 해당 자원에 접근하는 것이 불가능하다.)
        ActivityUtil.checkAccountsPermission(Manifest.permission.RECEIVE_SMS, 100, "이용기관샘플앱에서 SMS수신을 할 수 있게 하려면 권한이 필요합니다.", getActivity());

        return view;
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    /**
     * 버튼 onclick 이벤트핸들러
     *
     * @param v
     */
    @OnClick({ R.id.btnAuthNewWebAuth2, R.id.btnAuthNewWebAuthAcnt2 })
    public void onClick(View v) {

        // authorize2-authorize_account2간 UI 공유로 인해 추가된 코드
        HashMap<String, String> typeMap = null;

        Class fragmentClass = null;
        switch(v.getId()){
            case R.id.btnAuthNewWebAuth2:
                fragmentClass = AuthNewWebPageAuthorize2TabFragment.class;
                typeMap = ALL_TYPE_MAP.get("AUTH2");
                break;
            case R.id.btnAuthNewWebAuthAcnt2:
                fragmentClass = AuthNewWebPageAuthorize2TabFragment.class;
                typeMap = ALL_TYPE_MAP.get("AUTHACNT2");
                break;
            default:
                break;
        }

        if(fragmentClass != null){

            BaseFragment fragment = FragmentUtil.newFragment(fragmentClass);
            fragment.getArguments().putSerializable("TYPE_MAP", typeMap);
            FragmentUtil.replaceFragment(fragment);
        }
    }

    /**
     * 뒤로가기 버튼을 눌렀을 때의 동작
     */
    @Override
    public void onBackPressedForFragment() {

        FragmentUtil.replaceNewFragment(MainFragment.class);
    }
}
