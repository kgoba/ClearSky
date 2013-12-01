package com.kosmoss.clearsky.support;

import com.kosmoss.clearsky.core.LocalService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    Intent service = new Intent(context, LocalService.class);
	    context.startService(service);
	}

}
