package com.alienlabz.packagez.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.User;
import com.alienlabz.packagez.service.Synchronizer;
import com.alienlabz.packagez.ui.task.UserRegisterTask;
import com.facebook.FacebookActivity;
import com.facebook.SessionState;

/**
 * Login Activity.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class LoginActivity extends FacebookActivity {
	private ProgressDialog progressDialog;
	private ImageButton facebookButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		progressDialog = new ProgressDialog(this);

		facebookButton = (ImageButton) findViewById(R.id.facebook_button);
		facebookButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showLoadingDialog();

				List<String> permission = new ArrayList<String>();
				permission.add("user_about_me");
				permission.add("email");
				permission.add("user_location");
				openSessionForRead(null, permission);
			}

		});
	}

	private void showLoadingDialog() {
		progressDialog.setTitle(R.string.loading);
		progressDialog.setMessage(getResources().getString(R.string.contacting_facebook));
		progressDialog.show();
	}

	private void closeLoadingDialog() {
		progressDialog.dismiss();
	}

	@Override
	protected void onSessionStateChange(SessionState state, Exception exception) {
		if (state.isOpened()) {
			String token = getSession().getAccessToken();
			User user = new User();
			user.fbToken = token;
			new UserRegisterTask() {

				protected void onPostExecute(Boolean result) {
					initializeData();
				}

			}.execute(user);
		} else {
			closeLoadingDialog();
		}

		if (exception != null) {
			Toast.makeText(this, R.string.cant_contact_facebook, Toast.LENGTH_LONG).show();
		}

	}

	public void initializeData() {
		Intent intent = new Intent(this, Synchronizer.class);
		startService(intent);
	}

	public void navigateToMain() {

	}
}
