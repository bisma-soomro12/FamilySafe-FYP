package com.example.familysafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class BetteryBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.ACTION_BATTERY_LOW.equals(intent.getAction())){
            Toast.makeText(context.getApplicationContext(), "Battery is running low ",Toast.LENGTH_SHORT).show();
            String phn="03173341418";
            String message="battery is running LOW";
            SmsManager smsManager= SmsManager.getDefault();
            smsManager.sendTextMessage(phn,null,message,null,null);
        }
    }
}