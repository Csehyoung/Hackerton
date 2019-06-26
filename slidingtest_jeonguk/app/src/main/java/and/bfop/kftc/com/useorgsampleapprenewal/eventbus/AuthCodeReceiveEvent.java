package and.bfop.kftc.com.useorgsampleapprenewal.eventbus;

/**
 * 발급받은 authcode를 전달할 목적의 EventBus 파라미터 클래스 정의
 */
public class AuthCodeReceiveEvent {

    private String authCode;
    private String invoker;

    public AuthCodeReceiveEvent(String authCode, String invoker) {
        this.authCode = authCode;
        this.invoker = invoker;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getInvoker() {
        return invoker;
    }
}
