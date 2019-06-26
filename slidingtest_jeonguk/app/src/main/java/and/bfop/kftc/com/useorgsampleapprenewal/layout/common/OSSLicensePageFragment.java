package and.bfop.kftc.com.useorgsampleapprenewal.layout.common;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.util.Scanner;

import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.FragmentInitEvent;
import and.bfop.kftc.com.useorgsampleapprenewal.layout.MainFragment;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;

/**
 * 오픈소스 라이선스 Fragment
 */
public class OSSLicensePageFragment extends BaseFragment {

    //===================================== Fragment Lifecycle Callbacks - start =====================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_oss_license, container, false);
        super.initBaseFragment(view); // BaseFragment 초기화 수행

        // Fragment 초기화 이벤트를 EventBus를 통해서 post (액션바 햄버거메뉴와 뒤로가기 화살표버튼을 상호 교체하기 위해서 수행)
        EventBus.getDefault().post(new FragmentInitEvent(this.getClass(), false)); // true/false 주의


        // oss_license.txt 파일을 읽어서 TextView 에 바인딩
        InputStream in = getResources().openRawResource(R.raw.oss_license);
        StringBuilder sb = new StringBuilder();
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine()).append("\n");
        }
        sc.close();
        String contents = sb.toString();
        Log.d("## in: ", (in != null) ? in.toString() : "파일을 찾을 수 없습니다.");
        Log.d("## content: ", contents);

        TextView tvLicense = (TextView) view.findViewById(R.id.tvLicense);
        tvLicense.setText(contents);

        return view;
    }
    //===================================== Fragment Lifecycle Callbacks - end =======================================

    /**
     * 뒤로가기 버튼을 눌렀을 때의 동작
     */
    @Override
    public void onBackPressedForFragment() {

        FragmentUtil.replaceNewFragment(MainFragment.class);
    }
}
