package com.alienlabz.packagez.service;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.alienlabz.packagez.model.Pack;
import com.alienlabz.packagez.model.Status;
import com.alienlabz.packagez.model.Track;
import com.alienlabz.packagez.remote.Webservice;
import com.alienlabz.packagez.util.CursorList;
import com.alienlabz.packagez.util.Strings;

public class CheckStatusService extends IntentService {
	public static final String EXTRA_PACKAGE_CODE = "code";
	public static final String EXTRA_CLIENT_MESSENGER = "messenger";

	public CheckStatusService() {
		super("CheckStatus Service");
	}

	private void updatePackageTrackings(final String code) {
		if (code != null) {
			String status = null;
			String lastDate = null;
			String firstDate = null;

			try {
				final JSONObject jsonObject = Webservice.getTrackings(code);

				// FIXME Mudar esse c—digo para ficar mais leg’vel e "elegante"! :)
				if (jsonObject == null) {
					return;
				}

				JSONArray jsonArray;
				jsonArray = jsonObject.getJSONArray("data");

				if (jsonArray.length() > 0) {
					Track.deleteByCode(code);
				}

				for (int index = 0; index < jsonArray.length(); index++) {
					final JSONObject jsonTrack = jsonArray.getJSONObject(index);

					String description = "";
					if (jsonTrack.has("description")) {
						description = jsonTrack.getString("description");
					}

					String city = "";
					if (jsonTrack.has("city")) {
						city = jsonTrack.getString("city");
					}

					String state = "";
					if (jsonTrack.has("state")) {
						state = jsonTrack.getString("state");
					}

					String date = "";
					if (jsonTrack.has("date")) {
						date = jsonTrack.getString("date");
						if (firstDate == null) {
							firstDate = date;
						}
					}

					Track track = new Track();
					track.code = code;
					track.city = city;
					track.state = state;
					track.description = description;
					track.date = date;
					track.status = Status.fromString(jsonTrack.getString("status"));
					track.insert();

					status = jsonTrack.getString("status");
					lastDate = jsonTrack.getString("date");
				}

				boolean delivered = false;
				if (Pack.DELIVERED.equals(status)) {
					delivered = true;
				}

				Pack pack = new Pack();
				pack.code = code;
				pack.firstDate = firstDate;
				pack.status = Status.fromString(status);
				pack.lastDate = lastDate;
				pack.delivered = delivered;
				pack.update();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void update(final String code) {
		if (Strings.isEmpty(code)) {
			final CursorList<Pack> packList = Pack.findNotDeliveredPackages();
			for (Pack pack : packList) {
				updatePackageTrackings(pack.code);
			}
		} else {
			updatePackageTrackings(code);
		}
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		if (intent.getExtras() != null) {
			final String code = intent.getExtras().getString(EXTRA_PACKAGE_CODE);
			final Messenger messenger = intent.getExtras().getParcelable(EXTRA_CLIENT_MESSENGER);

			update(code);

			Message message = Message.obtain();
			try {
				messenger.send(message);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

}