package com.otl.tarangplus.BroadcastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.telephony.SmsMessage;

/**
 * Created by VPotadar on 21/02/18.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static SmsListener mListener;
    Boolean b;
    String OtpText;

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Bundle data = intent.getExtras();
            Object[] pdus = (Object[]) data.get("pdus");
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String sender = smsMessage.getDisplayOriginatingAddress();
                b = sender.endsWith("tfctor") || sender.endsWith("TFCTOR");  //specify the ending name
                String messageBody = smsMessage.getMessageBody();

                OtpText = messageBody.replaceAll("[^0-9]", "");   // here OtpText contains otp


                Intent myIntent = new Intent("otp");
                myIntent.putExtra("message", OtpText);
                LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);

                if (b == true) {
                    mListener.messageReceived(OtpText);  // attach value to interface
                } else {
                }
            }
        } catch (Exception e) {

        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

    public interface SmsListener {
         void messageReceived(String messageText);
    }

}
