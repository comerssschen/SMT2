package com.sunmi.weipan.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ResultReceiver extends BroadcastReceiver {

    public static final String REQUEST_ACTION = "sunmi.payment.action.entry";
    public static final String RESPONSE_ACTION = "sunmi.payment.action.result";
    private static final String TAG = "test";

    private ResultCallback callback;

    public ResultReceiver(ResultCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (RESPONSE_ACTION.equals(intent.getAction())) {
            String jsonStr = intent.getStringExtra("response");
            Log.d(TAG, "jsonStr = " + jsonStr);
            callback.callback(jsonStr);
        }
    }

    public interface ResultCallback {
        void callback(String result);
    }
}
