package com.iwobanas.mtapjacking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BRODCAST", "receive broadcast" + intent.getDataString());
    }
}
