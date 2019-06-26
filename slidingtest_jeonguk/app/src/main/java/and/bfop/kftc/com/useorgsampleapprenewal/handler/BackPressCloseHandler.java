package and.bfop.kftc.com.useorgsampleapprenewal.handler;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import and.bfop.kftc.com.useorgsampleapprenewal.App;


/**
 * 뒤로가기 버튼을 두 번 눌렀을 때 앱을 종료하게 하는 핸들러
 *
 * Created by LeeHyeonJae on 2017-04-20.
 */
public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private static final long checkInterval = 2000;
    private Toast toast;
    private Activity activity;

    private final static int appFlags = App.getAppContext().getApplicationInfo().flags;
    private final static boolean isDebug = (appFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + checkInterval) {

            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + checkInterval) {

            toast.cancel();
            off();
        }
    }

    /**
     * 앱 종료
     */
    public void off(){

        // 어느 Activity 에서든 모든 부모 Activity를 닫을 수 잇다.
        ActivityCompat.finishAffinity(activity);

        // 디버그모드가 아닐 경우에만 사용하자 (TODO: 실제로 이 분기가 적용되는지 테스트 해 보자)
        if(! isDebug){
            System.exit(0); // 혹은 android.os.Process.killProcess(android.os.Process.myPid()); 사용
        }
    }

    /**
     * 뒤로가기 종료 전 안내
     */
    public void showGuide() {

        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
