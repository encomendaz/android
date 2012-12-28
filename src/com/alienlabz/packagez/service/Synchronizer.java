package com.alienlabz.packagez.service;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;

import com.alienlabz.packagez.database.Database;
import com.alienlabz.packagez.model.Carrier;
import com.alienlabz.packagez.model.Pack;
import com.alienlabz.packagez.remote.CarrierRemote;
import com.alienlabz.packagez.remote.PackageRemote;

/**
 * Service responsible to synchronize local data with server's data.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class Synchronizer extends IntentService {
	public static final String SYNC_DELETE = "delete";
	public static final String SYNC_UPDATE = "update";
	public static final String SYNC_INSERT = "insert";
	public static final String SYNC_REFRESH = "refresh";

	public Synchronizer() {
		super("Synchronizer");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Database.getInstance().getWritableDatabase().beginTransaction();
		try {
			syncCarriers();
			syncPackages();
			Database.getInstance().getWritableDatabase().setTransactionSuccessful();
		} finally {
			Database.getInstance().getWritableDatabase().endTransaction();
		}
	}

	public void syncCarriers() {
		List<Carrier> carriers = CarrierRemote.findAll();
		for (Carrier remoteCarrier : carriers) {
			Carrier localCarrier = Carrier.findById(remoteCarrier.id);
			if (localCarrier != null) {
				localCarrier.name = remoteCarrier.name;
				localCarrier.update();
			} else {
				remoteCarrier.insert();
			}
		}

		List<Carrier> localCarriers = Carrier.findAll();
		for (Carrier localCarrier : localCarriers) {
			if (!carriers.contains(localCarrier)) {
				localCarrier.delete();
			}
		}
	}

	public void syncPackages() {
		List<Pack> deletions = Pack.findDeletions();
		for (Pack pack : deletions) {
			PackageRemote.delete(pack);
		}

		List<Pack> insertions = Pack.findInsertions();
		for (Pack pack : insertions) {
			PackageRemote.insert(pack);
		}

		List<Pack> remotePacks = PackageRemote.findAll();
		for (Pack pack : remotePacks) {

		}

	}

}
