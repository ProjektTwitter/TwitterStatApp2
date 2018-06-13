package com.sdplab.twitterstat.twitterapp4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TwittSaverServiceRestarterBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, TwittSaverService.class));
    }
}
