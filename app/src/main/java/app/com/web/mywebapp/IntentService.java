package app.com.web.mywebapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.fingerpush.android.FingerNotification;
import com.fingerpush.android.FingerPushFcmListener;

/**
 * Created by user on 12/7/2018.
 */

// 디바이스에서 푸시 수신 데이터(Payload)는 Bundle 타입으로 전달 받을 수 있으며, Bundle 데이터는 아래와 같습니다.

//data.msgTag : 메세지 번호
//        data.code : CD:1;IM:0;PT:DEFT
//        (CD:커스텀데이터 여부(0없음, 1있음), IM:이미지여부(0없음, 1있음) ,PT:메세지타입 (DEFT:일반, LNGT:롱푸시, STOS:타겟푸시)
//        data.time : 보낸시간
//        data.appTitle : 핑거푸시 앱이름
//        data.badge : 뱃지
//        data.sound : 사운드
//        data.title : 메세지 제목
//        data.message : 메세지내용
//        data.weblink : 웹링크 url
//        data.labelCode : 라벨코드
//        data.img : 이미지여부 (0:없음;1:있음)
//        data.imgUrl : 이미지url
//        data.cd1 : 커스텀 데이터
//        data.cd2 : 커스텀 데이터
//        data.cd3 : 커스텀 데이터

public class IntentService extends FingerPushFcmListener {

    @Override
    public void onMessage(Context context, Bundle data) {
        createNotificationChannel(data);
    }

    private void createNotificationChannel(Bundle data) {
        Intent intent = new Intent(IntentService.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pi = PendingIntent.getActivity(IntentService.this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        FingerNotification fingerNotification = new FingerNotification(this);
        fingerNotification.setNofiticaionIdentifier((int) System.currentTimeMillis());
        fingerNotification.setIcon(R.drawable.bar_icon); // Notification small icon
        fingerNotification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fingerNotification.setColor(Color.rgb(0, 114, 162));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fingerNotification.createChannel("channel_id", "channel_name");
        }
        fingerNotification.setVibrate(new long[]{0, 500, 600, 1000});
        fingerNotification.setLights(Color.parseColor("#ffff00ff"), 500, 500);
        fingerNotification.showNotification(data, pi);
    }
}