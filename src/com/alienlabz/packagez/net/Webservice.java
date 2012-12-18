package com.alienlabz.packagez.net;

import org.json.JSONException;
import org.json.JSONObject;

final public class Webservice {

	public static JSONObject getTrackings(final String code) {
		JSONObject result = null;

		final String json = HttpUtil.doGet("http://services.encomendaz.net/tracking.json?id=" + code);
		try {
			result = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}

}
