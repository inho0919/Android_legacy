package com.example.part7;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Telephony;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SmsManager mSMSManager;
    EditText editBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSMSManager = SmsManager.getDefault();
        editBody = (EditText)findViewById(R.id.editBody);

        Button btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
    }


    public void sendSms(){
        //메시지
        String body = editBody.getText().toString();
        //160자 이하의 메시지 리스트로 만든다
        ArrayList<String> bodyList = mSMSManager.divideMessage(body);

        //송신 인텐트
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
        //수신 인텐트
        PendingIntent recvPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);

        registerReceiver(mSentReceiver, new IntentFilter("SMS_SENT"));
        registerReceiver(mRecvReceiver, new IntentFilter("SMS_DELIVERED"));

        //여러개의 SMS 메시지를 전송
        if(bodyList.size() > 1){
            ArrayList<PendingIntent> sentPIList = new ArrayList<>();
            ArrayList<PendingIntent> recvPIList = new ArrayList<>();
            for(int i=0; i<bodyList.size(); i++){
                sentPIList.add(sentPI);
                recvPIList.add(recvPI);
            }
            mSMSManager.sendMultipartTextMessage("[Phone Number]", null, bodyList, sentPIList, recvPIList);
        }
        //1개의 SMS 메시지를 전송
        else{
            mSMSManager.sendTextMessage("[Phone Number]", null, body, sentPI, recvPI);
        }
    }

    BroadcastReceiver mSentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()){
                case RESULT_OK:
                    Toast.makeText(MainActivity.this, "SMS Send", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(MainActivity.this, "ERROR_GENERIC_FAILURE", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(MainActivity.this, "ERROR_NO_SERVICE", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(MainActivity.this, "ERROR_NULL_PDU", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(MainActivity.this, "ERROR_RADIO_OFF", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    BroadcastReceiver mRecvReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()){
                case RESULT_OK:
                    Toast.makeText(MainActivity.this, "SMS Delivered", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(MainActivity.this, "SMS Delivered Fail", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}

