package com.alienlabz.packagez;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {
	}

	@Override
	protected String[] getSenderIds(Context context) {
		return super.getSenderIds(context);
	}

	@Override
	protected void onError(Context context, String string) {
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
	}

	@Override
	protected void onRegistered(Context context, String string) {
	}

	@Override
	protected void onUnregistered(Context context, String string) {
	}

}
