package and.bfop.kftc.com.useorgsampleapprenewal.eventbus;

/**
 * ActionBar의 속성을 교체할 목적으로 EventBus 파라미터 클래스 정의
 *
 * Created by LeeHyeonJae on 2017-06-30.
 */
public class ActionBarChangeEvent {

    private String actionBarTitle;

    public String getActionBarTitle() {
        return actionBarTitle;
    }

    public ActionBarChangeEvent(String actionBarTitle) {
        this.actionBarTitle = actionBarTitle;
    }
}
