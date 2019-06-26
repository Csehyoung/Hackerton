package and.bfop.kftc.com.useorgsampleapprenewal.eventbus;

import and.bfop.kftc.com.useorgsampleapprenewal.layout.common.BaseFragment;

/**
 * MainActivity 위에 떠 있는 Fragment를 교체할 목적으로 EventBus 파라미터 클래스 정의
 *
 * Created by LeeHyeonJae on 2017-06-19.
 */
public class FragmentReplaceEvent {

    private BaseFragment fragment;

    public BaseFragment getFragment() {
        return fragment;
    }

    public FragmentReplaceEvent(BaseFragment fragment) {
        this.fragment = fragment;
    }
}
