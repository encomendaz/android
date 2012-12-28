package com.alienlabz.packagez.remote;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

final public class Webservice {
	private static final int MAX_ATTEMPTS = 5;
	private static final int BACKOFF_MILLI_SECONDS = 2000;
	private static final Random random = new Random();
	private static final String SERVER_URL = "http://packagez-alienlabs.rhcloud.com/";
	private static final String TAG = "WEBSERVICE";

	/**
	 * Contact our server to get tracking statuses.
	 * 
	 * @param code Tracking Code.
	 * @return Statuses.
	 */
	public static JSONObject getTrackings(final String code) {
		JSONObject result = null;

		final String json = ""; //HttpUtil.doGet("http://services.encomendaz.net/tracking.json?id=" + code);
		try {
			result = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Register this account/device pair within the server.
	 *
	 */
	public static void register(final Context context, String name, String email, final String regId)
			throws RuntimeException {
		Log.i(TAG, "registering device (regId = " + regId + ")");

		String serverUrl = "";
		try {
			serverUrl = SERVER_URL + "register/" + URLEncoder.encode(name, "UTF-8") + "/"
					+ URLEncoder.encode(email, "UTF-8") + "/" + URLEncoder.encode(regId, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {
				String result = "";//HttpUtil.doGet(serverUrl);
				if (result != null) {
					Log.d(TAG, result);
				}
				GCMRegistrar.setRegisteredOnServer(context, true);
				return;
			} catch (Exception e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	public static void unregister(final Context context, final String regId) {
		Log.i(TAG, "unregistering device (regId = " + regId + ")");
		String serverUrl = "";

		try {
			serverUrl = SERVER_URL + "unregister/" + URLEncoder.encode(regId, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		try {
			String result = "";//HttpUtil.doGet(serverUrl);
			Log.d(TAG, result);
			GCMRegistrar.setRegisteredOnServer(context, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
