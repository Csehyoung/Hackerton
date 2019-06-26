package and.bfop.kftc.com.useorgsampleapprenewal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import and.bfop.kftc.com.useorgsampleapprenewal.eventbus.SMSReceiveEvent;

/**
 * 사용자정의 BroadcaseReceiver
 *
 *  - SMS수신 처리 목적으로 생성
 *
 * Created by LeeHyeonJae on 2017-07-04.
 */
public class CustomReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // SMS 수신일 경우
        if("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())){

            // SMS 메시지를 파싱합니다.
            Bundle bundle = intent.getExtras();
            Object[] pdusObjs = (Object[])bundle.get("pdus");
            for(int i = 0; i < pdusObjs.length; i++) {
                SmsMessage curMsg = SmsMessage.createFromPdu((byte[])pdusObjs[i]); // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                String sender = curMsg.getDisplayOriginatingAddress(); // SMS 발신자번호
                String contents = curMsg.getDisplayMessageBody(); // SMS 내용
                Date receiveDate = new Date(curMsg.getTimestampMillis()); // SMS 전송일시
                Log.d("##", "SMS 수신 > 발신자:["+sender+"], 내용:["+contents+"]");

                // MainActivity로 메시지 전달
                EventBus.getDefault().post(new SMSReceiveEvent(sender, contents));
            }
        }

    }
}
