package com.alienlabz.packagez.remote;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alienlabz.packagez.model.Carrier;

/**
 * Remote Service for Carriers.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class CarrierRemote extends BaseRemote {
	public static final String SERVICE_URL = BASE_URL + "carrier/";

	/**
	 * Find all Carriers.
	 * 
	 * @return List of Carriers.
	 */
	public static List<Carrier> findAll() {
		List<Carrier> result = new ArrayList<Carrier>();
		String strResult = performGet(SERVICE_URL + "list");

		try {
			JSONObject object = new JSONObject(strResult);
			JSONArray array = object.getJSONArray("result");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonCarrier = array.getJSONObject(i);
				Carrier carrier = new Carrier();
				carrier.fromJSON(jsonCarrier);
				result.add(carrier);
			}
		} catch (JSONException e) {
		}

		return result;
	}

}
