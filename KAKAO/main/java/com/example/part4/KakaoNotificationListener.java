package com.example.part4;

import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class KakaoNotificationListener extends NotificationListenerService
{

    @Override
    public void onNotificationPosted(StatusBarNotification sbn)
    {
        final String packageName = sbn.getPackageName();
        super.onNotificationPosted(sbn);

        if(!TextUtils.isEmpty(packageName) && packageName.equals("com.kakao.talk"))
        {
            Bundle extras = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                extras = sbn.getNotification().extras;
            }
            String title = extras.getString(Notification.EXTRA_TITLE);
            CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
            CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);

            String content = sbn.getId() + " / " + sbn.getPostTime() + " / " + title +  " / " + text ;
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn)
    {
        Toast.makeText(this, "종료", Toast.LENGTH_SHORT).show();
    }
}