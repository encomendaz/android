package com.alienlabz.packagez.ui.task;

import android.content.Context;
import android.os.AsyncTask;

import com.alienlabz.packagez.remote.Webservice;

/**
 * Task used to contact and register this device in our cloud server!
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class GCMRegisterTask extends AsyncTask<String, Void, Boolean> {
	protected Context mContext;

	public GCMRegisterTask(Context context) {
		this.mContext = context;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		boolean result = true;

		try {
			Webservice.register(mContext, params[0], params[1], params[2]);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

}
