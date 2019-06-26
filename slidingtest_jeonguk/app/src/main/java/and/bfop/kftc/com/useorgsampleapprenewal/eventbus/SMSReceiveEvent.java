package and.bfop.kftc.com.useorgsampleapprenewal.eventbus;

/**
 * SMS 수신건을 MainActivity에 전달해 주기 위해 EventBus 파라미터 클래스 정의
 */
public class SMSReceiveEvent {

    private String sender;
    private String contents;

    public String getSender() {
        return sender;
    }

    public String getContents() {
        return contents;
    }

    public SMSReceiveEvent(String sender, String contents) {
        this.sender = sender;
        this.contents = contents;
    }
}
