package and.bfop.kftc.com.useorgsampleapprenewal.layout.apicall;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashMap;
import java.util.Map;

import and.bfop.kftc.com.useorgsampleapprenewal.App;
import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.restclient.RetrofitCustomAdapter;
import and.bfop.kftc.com.useorgsampleapprenewal.restclient.RetrofitUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.Constants;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.StringUtil;
import butterknife.OnClick;


/**
 * 계좌실명조회 API호출 Fragment
 */
public class APICallPageRealNameInquiryFragment extends BaseFragment {

    /**
     * NameSpace
     *  - 입력폼의 값들을 SharedPreferences에 저장할 때, 각 업무별로 별도로 값을 저장하기 위해서 임의의 이름을 구분자로 줌
     */
    private static final String NS = "ARN";

    EditText etToken; // 토큰
    EditText etBankCodeStd; // 은행코드
    EditText etAccountNum; // 계좌번호
    EditText etAccountHolderInfo; // 계좌주정보
    EditText etTranDtime; // 거래일시

    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apicall_realname_inquiry, container, false);
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), true));

        etToken = (EditText)view.findViewById(R.id.etToken);
        etBankCodeStd = (EditText)view.findViewById(R.id.etBankCodeStd);
        etAccountNum = (EditText)view.findViewById(R.id.etAccountNum);
        etAccountHolderInfo = (EditText)view.findViewById(R.id.etAccountHolderInfo);
        etTranDtime = (EditText)view.findViewById(R.id.etTranDtime);

        loadInputValues();

        return view;
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    /**
     * 계좌실명조회
     */
    private void inquiryRealName(){

        String token = Constants.TOKEN_PREFIX + etToken.getText().toString();

        Map params = new LinkedHashMap<>();
        params.put("bank_code_std", etBankCodeStd.getText().toString());
        params.put("account_num", etAccountNum.getText().toString());
        params.put("account_holder_info", etAccountHolderInfo.getText().toString());
        params.put("tran_dtime", etTranDtime.getText().toString());

        RetrofitUtil.callAsync(RetrofitCustomAdapter.getInstance().inquiryRealName(token, params), getActivity()); // rest client 호출

        saveInputValues();
    }

    /**
     * 입력값을 SharedPreferences 에 저장
     */
    private void saveInputValues() {

        SharedPreferences.Editor editor = App.getPref().edit();
        String es = App.getEnvSuffix(App.getEnv());
        editor.putString(NS + "token" + es, etToken.getText().toString());
        editor.putString(NS + "bank_code_std" + es, etBankCodeStd.getText().toString());
        editor.putString(NS + "account_num" + es, etAccountNum.getText().toString());
        editor.putString(NS + "account_holder_info" + es, etAccountHolderInfo.getText().toString());
        editor.putString(NS + "tran_dtime" + es, etTranDtime.getText().toString());
        editor.apply();
    }

    /**
     * SharedPreferences 에 저장된 입력값들을 UI에 채워 넣음
     */
    private void loadInputValues(){

        String es = App.getEnvSuffix(App.getEnv());
        etToken.setText(StringUtil.getPropString(NS + "token" + es));
        etBankCodeStd.setText(StringUtil.getPropString(NS + "bank_code_std" + es));
        etAccountNum.setText(StringUtil.getPropString(NS + "account_num" + es));
        etAccountHolderInfo.setText(StringUtil.getPropString(NS + "account_holder_info" + es));
        etTranDtime.setText(StringUtil.getPropString(NS + "tran_dtime" + es));
    }

    /**
     * 버튼 onclick 이벤트핸들러
     *
     * @param v
     */
    @OnClick(R.id.btnInqrRealName)
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnInqrRealName:
                inquiryRealName();
                break;
            default:
                break;
        }
    }

    /**
     * 뒤로가기 버튼을 눌렀을 때의 동작
     */
    @Override
    public void onBackPressedForFragment() {

        FragmentUtil.replaceNewFragment(APICallMenuFragment.class);
    }

}
