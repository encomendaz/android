package com.alienlabz.packagez;

import com.alienlabz.packagez.database.Database;

import android.app.Application;

public class App extends Application {
	public static final String PREF_LAST_SYNC = "LAST_SYNC";
			
	public void onCreate() {
		Database.initialize(getApplicationContext());
	}

}
