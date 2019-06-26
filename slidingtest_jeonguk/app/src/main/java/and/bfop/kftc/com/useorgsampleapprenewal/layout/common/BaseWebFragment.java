package and.bfop.kftc.com.useorgsampleapprenewal.layout.common;

import android.bfop.kftc.com.useorgsampleapprenewal.R;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import and.bfop.kftc.com.useorgsampleapprenewal.util.Constants;
import and.bfop.kftc.com.useorgsampleapprenewal.util.FragmentUtil;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * WebView를 탑재한 Fragment들의 공통 부모 클래스
 */
public abstract class BaseWebFragment extends BaseFragment {

    @Override
    public void initBaseFragment(View view){

        Log.d("##", "BaseWebFragment.initBaseFragment() invoked!");
        super.initBaseFragment(view); // BaseFragment의 동일 메서드 선 호출
    }

    /**
     * URL 펼침/접음 버튼의 버튼명/기능 토글
     *
     * @param v
     */
    @OnClick(R.id.btnFold)
    public void onClick(View v) {

        EditText edt = (EditText) this.getView().findViewById(R.id.etUrl); // URL 표시부
        Button btn = (Button) this.getView().findViewById(R.id.btnFold); // 펼침/접음 버튼

        if(Constants.BTN_NAME_UNFOLD.equals(btn.getText())){
            edt.setSingleLine(false);
            btn.setText(Constants.BTN_NAME_FOLD);
        }else{
            edt.setSingleLine(true);
            btn.setText(Constants.BTN_NAME_UNFOLD);
        }
    }

    /**
     * 버튼 onTouch 이벤트 핸들러
     *
     * @param v
     * @param event
     * @return
     */
    @OnTouch(R.id.btnFold)
    public boolean onTouch(View v, MotionEvent event) {
        return FragmentUtil.onTouchSetColorFilter(v, event);
    }

}
