package com.alienlabz.packagez.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.alienlabz.packagez.database.Database;

public class User {
	public String name;
	public String email;
	public String fbToken;
	public String token;

	public static User getDefault() {
		User result = new User();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Database.context);
		result.token = prefs.getString("user_token", null);
		return result;
	}

	public void save() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Database.context);
		prefs.edit().putString("user_token", token).putString("fb_token", fbToken).commit();
	}

	public boolean isLogged() {
		return false;
	}

}
