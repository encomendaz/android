package com.alienlabz.packagez.ui.task;

import android.os.AsyncTask;

import com.alienlabz.packagez.model.User;
import com.alienlabz.packagez.remote.UserRemote;

/**
 * Task used to Register an User in our Service!
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class UserRegisterTask extends AsyncTask<User, Void, Boolean> {

	@Override
	protected Boolean doInBackground(User... params) {
		boolean result = true;

		try {
			UserRemote.register(params[0]);
		} catch (Exception e) {
			result = false;
		}

		return result;
	}

}
