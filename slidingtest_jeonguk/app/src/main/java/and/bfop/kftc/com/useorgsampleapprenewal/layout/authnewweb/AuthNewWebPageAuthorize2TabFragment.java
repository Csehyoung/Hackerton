package and.bfop.kftc.com.useorgsampleapprenewal.layout.authnewweb;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.util.HashMap;

import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import and.bfop.kftc.com.useorgsampleapprenewal.util.StringUtil;


/**
 * 사용자인증 개선버전 Fragment (탭 있는 부모 페이지)
 */
public class AuthNewWebPageAuthorize2TabFragment extends BaseFragment {

    private FragmentTabHost tabHost;

    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_authnewweb_authorize2, container, false); // createTabContent() 때문에 final 처리해 줌.
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // TabHost 초기화 작업
        tabHost = (FragmentTabHost)view.findViewById(android.R.id.tabhost);
        tabHost.setup(this.getActivity(), this.getFragmentManager(), android.R.id.tabcontent);

        // TabSpec을 생성하여 TabHost에 add 한다.
        createAndAddTabSpec(view, "tab1", "Case1");
        createAndAddTabSpec(view, "tab2", "Case2");
        createAndAddTabSpec(view, "tab3", "Case3");

        // OnTabChangeListener 설정
        TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                changeFragmentInTabContents(tabId);
            }
        };
        tabHost.setOnTabChangedListener(listener);

        // 컨텐츠영역의 내용을 첫번째 Fragment로 기본 설정
        changeFragmentInTabContents("tab1");

        return view;
    }

    /**
     * 컨텐츠 영역의 Fragment를 교체한다.
     *
     * @param tabId
     */
    private void changeFragmentInTabContents(String tabId) {

        // authorize2-authorize_account2간 UI 공유로 인해 추가된 코드
        String caseNo = StringUtil.EMPTY;

        Class<? extends BaseFragment> fragmentClass = null;
        switch(tabId){
            case "tab1":
                fragmentClass = AuthNewWebPageAuthorize2Case1Fragment.class;
                caseNo = "1";
                break;
            case "tab2":
                fragmentClass = AuthNewWebPageAuthorize2Case2Fragment.class;
                caseNo = "2";
                break;
            case "tab3":
                fragmentClass = AuthNewWebPageAuthorize2Case3Fragment.class;
                caseNo = "3";
                break;
            default:
                break;
        }

        // authorize2-authorize_account2간 UI 공유로 인해 추가된 코드
        HashMap<String, String> typeMap = (HashMap<String, String>)this.getArguments().getSerializable("TYPE_MAP");
        typeMap.put("CASE_NO", caseNo);
        Log.d("##", "authorize2와 authorize_account2의 분기점 > typeMap:["+typeMap+"]");

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        BaseFragment fragment = FragmentUtil.newFragment(fragmentClass);
        fragment.getArguments().putSerializable("TYPE_MAP", typeMap);
        ft.replace(android.R.id.tabcontent, fragment);
        ft.commit();
    }

    /**
     * 탭의 기본 구성요소인 TabSpec를 생성하여 TabHost에 add 한다.
     *
     * @param view
     * @param tag
     * @param indicator
     */
    private void createAndAddTabSpec(final View view, String tag, String indicator) {

        TabHost.TabSpec spec = tabHost.newTabSpec(tag);
        spec.setIndicator(indicator);
        spec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                return view.findViewById(android.R.id.tabcontent);
            }
        });
        tabHost.addTab(spec);
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    /**
     * 뒤로가기 버튼을 눌렀을 때의 동작
     */
    @Override
    public void onBackPressedForFragment() {

        FragmentUtil.replaceNewFragment(AuthNewWebMenuFragment.class);
    }

}
