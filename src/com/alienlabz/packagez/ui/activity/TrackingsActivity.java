package com.alienlabz.packagez.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.MenuItem;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Track;
import com.alienlabz.packagez.service.CheckStatusService;
import com.alienlabz.packagez.ui.fragment.TrackingsListFragment;

/**
 * Trackings Activity.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class TrackingsActivity extends Activity implements CreateNdefMessageCallback {
	private TrackingsListFragment fragment;
	private String code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		if (intent.getExtras() != null) {
			Bundle bundle = intent.getExtras();
			code = bundle.getString(CheckStatusService.EXTRA_PACKAGE_CODE);
		}

		fragment = new TrackingsListFragment();
		getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();

		setTitle(code);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		fragment.loadTrackings(Track.findByCode(code));
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
		}
		return true;
	}

	@Override
	public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
		return new NdefMessage(new NdefRecord[] { NdefRecord.createApplicationRecord("com.alienlabz.packagez") });
	}

}
