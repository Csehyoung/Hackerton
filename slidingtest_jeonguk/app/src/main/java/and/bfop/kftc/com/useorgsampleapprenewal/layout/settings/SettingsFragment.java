package and.bfop.kftc.com.useorgsampleapprenewal.layout.settings;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;

import and.bfop.kftc.com.useorgsampleapprenewal.App;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.MainFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.util.Constants;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.MessageUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.StringUtil;
import butterknife.OnClick;

/**
 * 설정 페이지 Fragment
 */
public class SettingsFragment extends BaseFragment {

    private RadioGroup rg_svr;
    private EditText m_etAppKey;
    private EditText m_etAppSecret;
    private EditText m_etWebCallbackUrl;
    private Spinner m_spScope;

    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), true)); // true/false 주의

        rg_svr = (RadioGroup) view.findViewById(R.id.rgSvr);
        m_etAppKey = (EditText) view.findViewById(R.id.etAppKey);
        m_etAppSecret = (EditText) view.findViewById(R.id.etAppSecret);
        m_etWebCallbackUrl = (EditText) view.findViewById(R.id.etWebCallbackUrl);
        m_spScope = (Spinner) view.findViewById(R.id.spScope);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.sp_scope_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spScope.setAdapter(adapter);

        loadPreferences(App.getEnv());

        return view;
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    /**
     * 버튼 onclick 이벤트핸들러
     *
     * @param v
     */
    @OnClick({ R.id.radioSvr_TEST, R.id.radioSvr_PRD, R.id.btnSaveSettings, R.id.btnResetSettings, R.id.btnRemoveSession })
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.radioSvr_TEST:
            case R.id.radioSvr_PRD:
                loadPreferences(getEnvFromRadioButton());
                break;
            case R.id.btnSaveSettings:
                save();
                break;
            case R.id.btnResetSettings:
                reset();
                break;
            case R.id.btnRemoveSession:
                removeSessionCookie();
                break;
            default:
                break;
        }
    }

    /**
     * 사용자 로그인 세션이 계속 남아있어서, 제거를 위해 추가한 메서드
     */
    public void removeSessionCookie() {

        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                Log.d("", "## 롤리팝 이상 버전의 removeSessionCookie() 호출 후");
                }
            });

        }else{
            cookieManager.removeSessionCookie();
            Log.d("", "## 롤리팝 미만 버전의 removeSessionCookie() 호출 후");
        }
        MessageUtil.showToast("세션정보가 삭제되었습니다.", 1500);
    }

    /**
     * 설정값을 SharedPreferences 에 저장
     */
    public void save() {

        SharedPreferences.Editor editor = App.getPref().edit();

        // 클릭된 호출서버 라디오버튼에서 선택한 환경값 획득
        String env = getEnvFromRadioButton();
        editor.putString("ENV", env);

        String es = App.getEnvSuffix(env);
        editor.putString("APP_KEY" + es, m_etAppKey.getText().toString());
        editor.putString("APP_SECRET" + es, m_etAppSecret.getText().toString());
        editor.putString("WEB_CALLBACK_URL" + es, m_etWebCallbackUrl.getText().toString());
        editor.putString("SCOPE" + es, m_spScope.getSelectedItem().toString());

        editor.apply();

        MessageUtil.showToast("저장되었습니다.");
    }

    /**
     * 현재 SharedPreferences 에 저장되어 있는 설정값들을, Constants의 기본값으로 overwrite 한다.
     */
    public void reset(){

        final String env = getEnvFromRadioButton();
        String envName = App.getEnvName(env);

        // Dialog를 사용하여 confirm창 처럼 활용한다.
        MessageUtil.getDialogBuilder("", envName + " 호출용 설정을 초기화 하시겠습니까?", true, this.getActivity())
                .setPositiveButton("초기화", new DialogInterface.OnClickListener() {

                    // 초기화 선택시
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String es = App.getEnvSuffix(env);
                        SharedPreferences.Editor editor = App.getPref().edit();

                        editor.putString("ENV", env);
                        editor.putString("APP_KEY" + es, StringUtil.getConstantsVal("APP_KEY" + es));
                        editor.putString("APP_SECRET" + es, StringUtil.getConstantsVal("APP_SECRET" + es));
                        editor.putString("WEB_CALLBACK_URL" + es, StringUtil.getConstantsVal("WEB_CALLBACK_URL" + es));
                        editor.putString("SCOPE" + es, StringUtil.getConstantsVal("SCOPE" + es));

                        editor.apply();

                        loadPreferences(env);

                        MessageUtil.showToast("초기화되었습니다.", 1500);
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
     * 현재 라디오버튼 선택상태의 호출서버 구분자를 리턴
     *
     * @return
     */
    private String getEnvFromRadioButton() {

        String radioSvrId = getResources().getResourceEntryName(rg_svr.getCheckedRadioButtonId());
        return radioSvrId.substring(radioSvrId.indexOf('_')+1);
    }

    /**
     * input form 에 SharedPreferences 값 로딩
     */
    private void loadPreferences(String env) {

        setRadioSvrCheckedFromPref(env);

        String es = App.getEnvSuffix(env);

        m_etAppKey.setText(StringUtil.getPropString("APP_KEY" + es));
        m_etAppSecret.setText(StringUtil.getPropString("APP_SECRET" + es));
        m_etWebCallbackUrl.setText(StringUtil.getPropString("WEB_CALLBACK_URL" + es));
        m_spScope.setSelection(StringUtil.getPropString("SCOPE" + es).equals("inquiry") ? 0 : 1);
    }

    /**
     * 매개변수로 호출서버 구분자를 받아서 라디오버튼 클릭상태 반영
     */
    private void setRadioSvrCheckedFromPref(String env){

        int id = 0;
        switch(env){
            case Constants.ENV_TEST: id = R.id.radioSvr_TEST; break;
            case Constants.ENV_PRD: id = R.id.radioSvr_PRD; break;
        }
        rg_svr.check(id);
    }

    /**
     * 뒤로가기 버튼을 눌렀을 때의 동작
     */
    @Override
    public void onBackPressedForFragment() {

        FragmentUtil.replaceNewFragment(MainFragment.class);
    }
}
