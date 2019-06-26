package and.bfop.kftc.com.useorgsampleapprenewal.eventbus;

/**
 * 메인화면에서 Fragment를 이동할 때 사용할 목적으로 EventBus 파라미터 클래스 정의
 *
 * Created by LeeHyeonJae on 2017-06-30.
 */
public class GoPageInMainActivityEvent {

    private int id;

    public int getId() {
        return id;
    }

    public GoPageInMainActivityEvent(int id) {
        this.id = id;
    }
}
