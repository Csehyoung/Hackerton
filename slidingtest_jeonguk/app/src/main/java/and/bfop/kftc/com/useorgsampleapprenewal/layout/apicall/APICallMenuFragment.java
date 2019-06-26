package and.bfop.kftc.com.useorgsampleapprenewal.layout.apicall;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.MainFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import butterknife.OnClick;

/**
 * API거래기능 메뉴 Fragment
 */
public class APICallMenuFragment extends BaseFragment {

    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apicall_menu, container, false);
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), false)); // true/false 주의

        return view;
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    /**
     * 버튼 onclick 이벤트핸들러
     *
     * @param v
     */
    @OnClick({ R.id.btnInqrBlncPage, R.id.btnInqrTranRecPage, R.id.btnInqrRealNamePage, R.id.btnTrnsDP2Page, R.id.btnTrnsWDPage, R.id.btnInqrUserInfoPage})
    public void onClick(View v) {

        Class fragmentClass = null;
        switch(v.getId()){
            case R.id.btnInqrBlncPage:
                fragmentClass = APICallPageBalanceInquiryFragment.class;
                break;
            case R.id.btnInqrTranRecPage:
                fragmentClass = APICallPageTransactionRecordInquiryFragment.class;
                break;
            case R.id.btnInqrRealNamePage:
                fragmentClass = APICallPageRealNameInquiryFragment.class;
                break;
            case R.id.btnTrnsDP2Page:
                fragmentClass = APICallPageDeposit2TransferFragment.class;
                break;
            case R.id.btnTrnsWDPage:
                fragmentClass = APICallPageWithdrawTransferFragment.class;
                break;
            case R.id.btnInqrUserInfoPage:
                fragmentClass = APICallPageUserInfoInquiryFragment.class;
                break;
            default:
                break;
        }
        if(fragmentClass != null){
            FragmentUtil.replaceNewFragment(fragmentClass);
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
