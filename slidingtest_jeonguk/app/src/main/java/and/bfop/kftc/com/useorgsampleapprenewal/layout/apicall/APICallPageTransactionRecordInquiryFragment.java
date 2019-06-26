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
 * 거래내역조회 API호출 Fragment
 */
public class APICallPageTransactionRecordInquiryFragment extends BaseFragment {

    /**
     * NameSpace
     *  - 입력폼의 값들을 SharedPreferences에 저장할 때, 각 업무별로 별도로 값을 저장하기 위해서 임의의 이름을 구분자로 줌
     */
    private static final String NS = "ATR";

    EditText etToken; // 토큰
    EditText etFintechUseNum; // 핀테크이용번호
    EditText etInquiryType; // 조회구분코드
    EditText etFromDate; // 조회시작일자
    EditText etToDate; // 조회종료일자
    EditText etSortOrder; // 정렬순서
    EditText etPageIndex; // 페이지번호
    EditText etTranDtime; // 거래일시
    EditText etBeforInquiryTraceInfo; // 직전조회추적정보


    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apicall_transaction_inquiry, container, false);
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), true));

        etToken = (EditText)view.findViewById(R.id.etToken);
        etFintechUseNum = (EditText)view.findViewById(R.id.etFintechUseNum);
        etInquiryType = (EditText)view.findViewById(R.id.etInquiryType);
        etFromDate = (EditText)view.findViewById(R.id.etFromDate);
        etToDate = (EditText)view.findViewById(R.id.etToDate);
        etSortOrder = (EditText)view.findViewById(R.id.etSortOrder);
        etPageIndex = (EditText)view.findViewById(R.id.etPageIndex);
        etTranDtime = (EditText)view.findViewById(R.id.etTranDtime);
        etBeforInquiryTraceInfo = (EditText)view.findViewById(R.id.etBeforInquiryTraceInfo);

        loadInputValues();

        return view;
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    /**
     * 거래내역조회
     */
    private void accountTrasactionList(){

        String token = Constants.TOKEN_PREFIX + etToken.getText().toString();

        Map params = new LinkedHashMap<>();
        params.put("fintech_use_num", etFintechUseNum.getText().toString());
        params.put("inquiry_type", etInquiryType.getText().toString());
        params.put("from_date", etFromDate.getText().toString());
        params.put("to_date", etToDate.getText().toString());
        params.put("sort_order", etSortOrder.getText().toString());
        params.put("page_index", etPageIndex.getText().toString());
        params.put("tran_dtime", etTranDtime.getText().toString());
        params.put("befor_inquiry_trace_info", etBeforInquiryTraceInfo.getText().toString());

        RetrofitUtil.callAsync(RetrofitCustomAdapter.getInstance().accountTrasactionList(token, params), getActivity()); // rest client 호출

        saveInputValues();
    }

    /**
     * 입력값을 SharedPreferences 에 저장
     */
    private void saveInputValues() {

        SharedPreferences.Editor editor = App.getPref().edit();
        String es = App.getEnvSuffix(App.getEnv());
        editor.putString(NS + "token" + es, etToken.getText().toString());
        editor.putString(NS + "fintech_use_num" + es, etFintechUseNum.getText().toString());
        editor.putString(NS + "inquiry_type" + es, etInquiryType.getText().toString());
        editor.putString(NS + "from_date" + es, etFromDate.getText().toString());
        editor.putString(NS + "to_date" + es, etToDate.getText().toString());
        editor.putString(NS + "sort_order" + es, etSortOrder.getText().toString());
        editor.putString(NS + "page_index" + es, etPageIndex.getText().toString());
        editor.putString(NS + "tran_dtime" + es, etTranDtime.getText().toString());
        editor.putString(NS + "befor_inquiry_trace_info" + es, etBeforInquiryTraceInfo.getText().toString());
        editor.apply();
    }

    /**
     * SharedPreferences 에 저장된 입력값들을 UI에 채워 넣음
     */
    private void loadInputValues(){

        String es = App.getEnvSuffix(App.getEnv());
        etToken.setText(StringUtil.getPropString(NS + "token" + es));
        etFintechUseNum.setText(StringUtil.getPropString(NS + "fintech_use_num" + es));
        etInquiryType.setText(StringUtil.getPropString(NS + "inquiry_type" + es));
        etFromDate.setText(StringUtil.getPropString(NS + "from_date" + es));
        etToDate.setText(StringUtil.getPropString(NS + "to_date" + es));
        etSortOrder.setText(StringUtil.getPropString(NS + "sort_order" + es));
        etPageIndex.setText(StringUtil.getPropString(NS + "page_index" + es));
        etTranDtime.setText(StringUtil.getPropString(NS + "tran_dtime" + es));
        etBeforInquiryTraceInfo.setText(StringUtil.getPropString(NS + "befor_inquiry_trace_info" + es));
    }

    /**
     * 버튼 onclick 이벤트핸들러
     *
     * @param v
     */
    @OnClick(R.id.btnInqrTranRec)
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnInqrTranRec:
                accountTrasactionList();
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
