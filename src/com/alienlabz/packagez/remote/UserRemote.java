package com.alienlabz.packagez.remote;

import org.json.JSONException;
import org.json.JSONObject;

import com.alienlabz.packagez.model.User;

public class UserRemote extends BaseRemote {
	public static final String REGISTER_URL = BASE_URL + "register/:token:";

	public static void register(User user) {
		String strResult = performGet(REGISTER_URL.replace(":token:", user.fbToken));
		try {
			JSONObject jsonObject = new JSONObject(strResult);
			user.token = jsonObject.getString("token");
			user.save();
		} catch (JSONException e) {
		}
	}

}
