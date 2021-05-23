package com.example.part3;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.telephony.SmsMessage;
import android.widget.Toast;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSReceiver extends BroadcastReceiver
{
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        String str = "";
        if (bundle != null)
        {
            Object [] pdus = (Object[])bundle.get("pdus");
            SmsMessage [] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++)
            {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str = str + msgs[i].getOriginatingAddress() + "에게 문자왔음, " + msgs[i].getMessageBody().toString() +"\n";
            }
            Date receivedDate = new Date(msgs[0].getTimestampMillis());
            str = str + "시간 : " + receivedDate.toString();
            Toast.makeText(context,str, Toast.LENGTH_LONG).show();
        }
    }
}


