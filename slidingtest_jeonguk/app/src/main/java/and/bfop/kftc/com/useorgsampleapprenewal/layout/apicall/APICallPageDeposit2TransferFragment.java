package and.bfop.kftc.com.useorgsampleapprenewal.layout.apicall;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
 * 입금이체(계좌번호) API호출 Fragment
 */
public class APICallPageDeposit2TransferFragment extends BaseFragment {

    /**
     * NameSpace
     *  - 입력폼의 값들을 SharedPreferences에 저장할 때, 각 업무별로 별도로 값을 저장하기 위해서 임의의 이름을 구분자로 줌
     */
    private static final String NS = "ADP2";

    EditText etToken; // 토큰
    EditText etWdPassPhrase; // 입금이체암호문구
    EditText etWdPrintContent; // 출금계좌인자내역
    EditText etTranDtime; // 거래일시

    EditText etBankCodeStd; // 입금은행표준코드
    EditText etAccountNum; // 입금계좌번호
    EditText etAccountHolderName; // 입금계좌예금주명
    EditText etPrintContent; // 입금계좌인자내역
    EditText etTranAmt; // 거래금액

    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apicall_deposit2_transfer, container, false);
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), true));

        etToken = (EditText)view.findViewById(R.id.etToken);
        etWdPassPhrase = (EditText)view.findViewById(R.id.etWdPassPhrase);
        etWdPrintContent = (EditText)view.findViewById(R.id.etWdPrintContent);
        etTranDtime = (EditText)view.findViewById(R.id.etTranDtime);

        etBankCodeStd = (EditText)view.findViewById(R.id.etBankCodeStd);
        etAccountNum = (EditText)view.findViewById(R.id.etAccountNum);
        etAccountHolderName = (EditText)view.findViewById(R.id.etAccountHolderName);
        etPrintContent = (EditText)view.findViewById(R.id.etPrintContent);
        etTranAmt = (EditText)view.findViewById(R.id.etTranAmt);

        loadInputValues();

        return view;
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    /**
     * 입금이체
     */
    private void transferDeposit2(){

        // 입금이체(계좌번호) 파라미터 형태
        /*
        {
            "wd_pass_phrase": "1111",
            "wd_print_content": "출금인자 01",
            "tran_dtime": "20170710141024",
            "req_cnt": "1",
            "req_list": [{
                "tran_no": 1,
                "bank_code_std": "097",
                "account_num": "112233",
                "account_holder_name": "김말똥",
                "print_content": "입금계좌인자01",
                "tran_amt": "1700"
                }
            ]
        }
        */

        String token = Constants.TOKEN_PREFIX + etToken.getText().toString();

        Map params = new LinkedHashMap<>();
        params.put("wd_pass_phrase", etWdPassPhrase.getText().toString());
        params.put("wd_print_content", etWdPrintContent.getText().toString());
        params.put("tran_dtime", etTranDtime.getText().toString());
        params.put("req_cnt", "1"); // 구현 시간 문제상 일단 한 건만 보내기로 함

        List<Map> innerMapList = new ArrayList<>();
        Map innerMap = new LinkedHashMap<>();
        innerMap.put("tran_no", "1");
        innerMap.put("bank_code_std", etBankCodeStd.getText().toString());
        innerMap.put("account_num", etAccountNum.getText().toString());
        innerMap.put("account_holder_name", etAccountHolderName.getText().toString());
        innerMap.put("print_content", etPrintContent.getText().toString());
        innerMap.put("tran_amt", etTranAmt.getText().toString());
        innerMapList.add(innerMap);
        params.put("req_list", innerMapList);

        RetrofitUtil.callAsync(RetrofitCustomAdapter.getInstance().transferDeposit2(token, params), getActivity()); // rest client 호출

        saveInputValues();
    }

    /**
     * 입력값을 SharedPreferences 에 저장
     */
    private void saveInputValues() {

        SharedPreferences.Editor editor = App.getPref().edit();
        String es = App.getEnvSuffix(App.getEnv());
        editor.putString(NS + "token" + es, etToken.getText().toString());
        editor.putString(NS + "wd_pass_phrase" + es, etWdPassPhrase.getText().toString());
        editor.putString(NS + "wd_print_content" + es, etWdPrintContent.getText().toString());
        editor.putString(NS + "tran_dtime" + es, etTranDtime.getText().toString());
        editor.putString(NS + "bank_code_std" + es, etBankCodeStd.getText().toString());
        editor.putString(NS + "account_num" + es, etAccountNum.getText().toString());
        editor.putString(NS + "account_holder_name" + es, etAccountHolderName.getText().toString());
        editor.putString(NS + "print_content" + es, etPrintContent.getText().toString());
        editor.putString(NS + "tran_amt" + es, etTranAmt.getText().toString());
        editor.apply();
    }

    /**
     * SharedPreferences 에 저장된 입력값들을 UI에 채워 넣음
     */
    private void loadInputValues(){

        String es = App.getEnvSuffix(App.getEnv());
        etToken.setText(StringUtil.getPropString(NS + "token" + es));
        etWdPassPhrase.setText(StringUtil.getPropString(NS + "wd_pass_phrase" + es));
        etWdPrintContent.setText(StringUtil.getPropString(NS + "wd_print_content" + es));
        etTranDtime.setText(StringUtil.getPropString(NS + "tran_dtime" + es));
        etBankCodeStd.setText(StringUtil.getPropString(NS + "bank_code_std" + es));
        etAccountNum.setText(StringUtil.getPropString(NS + "account_num" + es));
        etAccountHolderName.setText(StringUtil.getPropString(NS + "account_holder_name" + es));
        etPrintContent.setText(StringUtil.getPropString(NS + "print_content" + es));
        etTranAmt.setText(StringUtil.getPropString(NS + "tran_amt" + es));
    }

    /**
     * 버튼 onclick 이벤트핸들러
     *
     * @param v
     */
    @OnClick(R.id.btnTrnsDP2)
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnTrnsDP2:
                transferDeposit2();
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
